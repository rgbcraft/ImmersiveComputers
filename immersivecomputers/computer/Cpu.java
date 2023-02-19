package immersivecomputers.computer;

import immersivecomputers.computer.bus.Bus;
import immersivecomputers.main.Main;

import java.util.Arrays;

public class Cpu {
    public enum Registers {
        R0(0),
        R1(1),
        R2(2),
        R3(3),
        R4(4),
        R5(5),
        R6(6),
        R7(7);

        public final int addr;

        Registers(int addr) {
            this.addr = addr;
        }
    }

    public enum ControlRegisters {
        CTC(0),
        CBS(1),
        CUL(2),
        CUO(3),
        CEM(4),
        CPC(5),
        CSP(6),
        CSB(7);

        public final int addr;

        ControlRegisters(int addr) {
            this.addr = addr;
        }
    }

    public short[] registers;
    public short[] control_registers;

    public boolean f_equal, f_greater, f_less, f_overflow, f_carry, halted;

    public Bus bus;

    public Cpu() {
        this.halted = true;
        this.bus = new Bus(this);
        this.bus.write((short) 0x400, (byte) 0b01100000);
        this.bus.write((short) 0x401, (byte) 0b00011000);
        this.bus.write((short) 0x402, (byte) 0b00000000);
        this.bus.write((short) 0x403, (byte) 0b00000001);
        this.bus.write((short) 0x404, (byte) 0b00000100);
        this.bus.write((short) 0x405, (byte) 0b00011000);
        this.bus.write((short) 0x406, (byte) 0b00000100);
        this.bus.write((short) 0x407, (byte) 0b00000000);
    }

    public void boot() {
        this.registers = new short[8];
        this.control_registers = new short[8];
        this.control_registers[5] = 0x400;
        this.halted = false;

        Main.EXECUTOR.addProcessor(this);
    }

    public void setRegister(int register, short data) {
        this.registers[register] = data;
    }

    public short getRegister(int register) {
        return this.registers[register];
    }

    public void setControl(int register, short data) {
        if (register != 4) {
            if (control_registers[4] == 1) {
                if (register != 2 && register != 3 && register != 0) {
                    control_registers[register] = data;
                } else {
                    throw new IllegalArgumentException("Can't write kernel mode registers in user mode");
                }
            } else {
                control_registers[register] = data;
            }
        } else {
            throw new IllegalArgumentException("Can't write execution mode register!");
        }
    }

    public short getControl(int register) throws IllegalArgumentException {
        if (register != 4) {
            if (control_registers[4] == 1) {
                if (register != 2 && register != 3 && register != 0) {
                    return control_registers[register];
                } else {
                    throw new IllegalArgumentException("Can't read kernel mode registers in user mode");
                }
            } else {
                return control_registers[register];
            }
        } else {
            throw new IllegalArgumentException("Can't read execution mode register");
        }
    }

    public short translateAddress(short virtual) {
        short offset = this.control_registers[3];
        return (short) (virtual + offset);
    }

    public byte readMemory(short addr) throws IndexOutOfBoundsException {
        if (this.control_registers[4] == 0) {
            return this.bus.read(addr);
        } else {
            short address = translateAddress(addr);
            if (address <= this.control_registers[2]) {
                return this.bus.read(address);
            } else {
                throw new IndexOutOfBoundsException("Exceeded userspace memory in user mode");
            }
        }
    }

    public void writeMemory(short addr, byte value) throws IndexOutOfBoundsException {
        if (this.control_registers[4] == 0) {
            this.bus.write(addr, value);
        } else {
            short address = translateAddress(addr);
            if (address <= this.control_registers[2]) {
                this.bus.write(address, value);
            } else {
                throw new IndexOutOfBoundsException("Exceeded userspace memory in user mode");
            }
        }
    }

    public short readWord(short addr) throws IndexOutOfBoundsException {
        if (this.control_registers[4] == 0) {
            return (short) ((this.bus.read(addr) << 8) | this.bus.read((short) (addr + 1)));
        } else {
            short address = translateAddress(addr);
            if (address <= this.control_registers[2]) {
                return (short) ((this.bus.read(address) << 8) | this.bus.read((short) (address + 1)));
            } else {
                throw new IndexOutOfBoundsException("Exceeded userspace memory in user mode");
            }
        }
    }

    public void writeWord(short addr, short value) throws IndexOutOfBoundsException {
        if (this.control_registers[4] == 0) {
            this.bus.write(addr, (byte) ((value & 0xFF00) >> 8));
            this.bus.write((short) (addr+1), (byte) (value & 0xFF));
        } else {
            short address = translateAddress(addr);
            if (address <= this.control_registers[2]) {
                this.bus.write(address, (byte) ((value & 0xFF00) >> 8));
                this.bus.write((short) (address+1), (byte) (value & 0xFF));
            } else {
                throw new IndexOutOfBoundsException("Exceeded userspace memory in user mode");
            }
        }
    }

    public short fetchWord() {
        short addr = this.control_registers[5];
        this.control_registers[5] += 2;
        return this.readWord(addr);
    }

    public short getOperand(byte op, boolean control) {
        byte mode = (byte) ((op & 0x38) >> 3);
        byte reg = (byte) (op & 0x7);
        short addr, x;
        if (!control) {
            switch (mode) {
                case 0:
                    return this.registers[reg];
                case 1:
                    return this.readWord(this.registers[reg]);
                case 2:
                    addr = this.registers[reg];
                    this.registers[reg]++;
                    return this.readWord(addr);
                case 3:
                    return this.fetchWord();
                case 4:
                    x = fetchWord();
                    return this.readWord((short) (this.registers[reg]+x));
                case 5:
                    return this.readMemory(this.registers[reg]);
                case 6:
                    addr = this.registers[reg];
                    this.registers[reg]++;
                    return this.readMemory(addr);
                case 7:
                    x = fetchWord();
                    return this.readMemory((short) (this.registers[reg]+x));
            }
        } else {
            if (reg == 5) {
                switch (mode) {
                    case 0:
                        return this.fetchWord();
                    case 1:
                        return this.readWord(this.fetchWord());
                    case 2:
                        return this.readWord((short) (this.fetchWord() + this.control_registers[5]));
                }
            } else if (reg == 6) {
                switch (mode) {
                    case 3:
                        return this.readWord(this.control_registers[6]);
                    case 4:
                        return this.readWord(this.control_registers[6]++);
                    case 5:
                        return this.readWord(--this.control_registers[6]);
                    case 6:
                        return this.readWord((short) (this.control_registers[6]+this.fetchWord()));
                }
            } else {
                switch (mode) {
                    case 0:
                        return this.control_registers[reg];
                    case 1:
                        return this.readWord(this.control_registers[reg]);
                    case 2:
                        addr = this.control_registers[reg];
                        this.control_registers[reg]++;
                        return addr;
                    case 3:
                        return this.fetchWord();
                    case 4:
                        x = fetchWord();
                        return this.readWord((short) (this.control_registers[reg] + x));
                    case 5:
                        return this.readMemory(this.control_registers[reg]);
                    case 6:
                        addr = this.control_registers[reg];
                        this.control_registers[reg]++;
                        return this.readMemory(addr);
                    case 7:
                        x = fetchWord();
                        return this.readMemory((short) (this.control_registers[reg] + x));
                }
            }
        }

        throw new IllegalArgumentException("Invalid operand");
    }

    public void setOperand(byte op, short value, boolean control) {
        byte mode = (byte) ((op & 0x38) >> 3);
        byte reg = (byte) (op & 0x7);
        short addr, x;
        if (!control) {
            switch (mode) {
                case 0:
                    this.registers[reg] = value;
                    return;
                case 1:
                    this.writeWord(this.registers[reg], value);
                    return;
                case 2:
                    addr = this.registers[reg];
                    this.registers[reg]++;
                    this.writeWord(addr, value);
                    return;
                case 4:
                    x = fetchWord();
                    this.writeWord((short) (this.registers[reg]+x), value);
                    return;
                case 5:
                    this.writeMemory(this.registers[reg], (byte) (value & 0x00FF));
                    return;
                case 6:
                    addr = this.registers[reg];
                    this.registers[reg]++;
                    this.writeMemory(addr, (byte) (value & 0x00FF));
                    return;
                case 7:
                    x = fetchWord();
                    this.writeMemory((short) (this.registers[reg]+x), (byte) (value & 0x00FF));
                    return;
            }
        } else {
            if (reg == 5) {
                switch (mode) {
                    case 1:
                        this.writeWord(this.fetchWord(), value);
                        return;
                    case 2:
                        this.writeWord((short) (this.fetchWord() + this.control_registers[5]), value);
                        return;
                }
            } else if (reg == 6) {
                switch (mode) {
                    case 3:
                        this.writeWord(this.control_registers[6], value);
                        return;
                    case 4:
                        this.writeWord(this.control_registers[6]++, value);
                        return;
                    case 5:
                        this.writeWord(--this.control_registers[6], value);
                        return;
                    case 6:
                        this.writeWord((short) (this.control_registers[6]+this.fetchWord()), value);
                        return;
                }
            } else {
                switch (mode) {
                    case 0:
                        this.control_registers[reg] = value;
                        return;
                    case 1:
                        this.writeWord(this.control_registers[reg], value);
                        return;
                    case 2:
                        addr = this.control_registers[reg];
                        this.control_registers[reg]++;
                        this.writeWord(addr, value);
                        return;
                    case 4:
                        x = fetchWord();
                        this.writeWord((short) (this.control_registers[reg] + x), value);
                        return;
                    case 5:
                        this.writeMemory(this.control_registers[reg], (byte) (value & 0x00FF));
                        return;
                    case 6:
                        addr = this.control_registers[reg];
                        this.control_registers[reg]++;
                        this.writeMemory(addr, (byte) (value & 0x00FF));
                        return;
                    case 7:
                        x = fetchWord();
                        this.writeMemory((short) (this.control_registers[reg] + x), (byte) (value & 0x00FF));
                        return;
                }
            }
        }

        throw new IllegalArgumentException("Invalid operand");
    }

    public void jump(short addr) {
        this.control_registers[5] = addr;
    }

    public void interrupt(short cause) {
        this.control_registers[0] = cause;
    }
    public void execute() {
        System.out.println("Registri: " + Arrays.toString(this.registers) + " " + Arrays.toString(this.control_registers));

        short inst = fetchWord();

        byte op1 = (byte) ((inst & 0xfc0) >> 6);
        byte op2 = (byte) (inst & 0x3f);

        switch ((inst & 0xf000) >> 12) {
            // mov
            case 0x1:
                setOperand(op1, getOperand(op2, false), false);
                return;
            case 0x2:
                setOperand(op1, getOperand(op2, false), true);
                return;
            case 0x3:
                if (getOperand(op1, false) == getOperand(op2, false)) this.f_equal = true;
                if (getOperand(op1, false) > getOperand(op2, false)) this.f_greater = true;
                if (getOperand(op1, false) < getOperand(op2, false)) this.f_less = true;
                return;
            case 0x4:
                if (getOperand(op1, true) == getOperand(op2, false)) this.f_equal = true;
                if (getOperand(op1, true) > getOperand(op2, false)) this.f_greater = true;
                if (getOperand(op1, true) < getOperand(op2, false)) this.f_less = true;
                return;
            case 0x5:
                setOperand(op1, (short) (getOperand(op1, false) + getOperand(op2, false)), false);
                return;
            case 0x6:
                setOperand(op1, (short) (getOperand(op1, false) - getOperand(op2, false)), false);
                return;
            case 0x7:
                setOperand(op1, (short) (getOperand(op1, false) * getOperand(op2, false)), false);
                return;
            case 0x8:
                setOperand(op1, (short) (getOperand(op1, false) / getOperand(op2, false)), false);
                return;
            case 0x9:
                setOperand(op1, (short) (getOperand(op1, false) ^ getOperand(op2, false)), false);
                return;
            case 0xA:
                setOperand(op1, (short) (getOperand(op1, false) | getOperand(op2, false)), false);
                return;
            case 0xB:
                setOperand(op1, (short) (getOperand(op1, false) & getOperand(op2, false)), false);
                return;
            case 0xC:
                setOperand(op1, (short) (getOperand(op1, false) >> getOperand(op2, false)), false);
                return;
            case 0xD:
                setOperand(op1, (short) (getOperand(op1, false) << getOperand(op2, false)), false);
                return;
        }

        op1 = (byte) ((inst & 0x3f));
        switch ((inst & 0xffc) >> 6) {
            case 0x10:
                this.jump(getOperand(op1, false));
                return;
            case 0x11:
                setOperand(op1, (short) 0x0, false);
                return;
            case 0x12:
                setOperand(op1, (short) ((short) ((getOperand(op1, false) & 0xFF) << 8) | ((getOperand(op1, false) & 0xFF00) >> 8)), false);
                return;
            case 0x13:
                setOperand(op1, (short) (getOperand(op1, false) + 1), false);
                return;
            case 0x14:
                setOperand(op1, (short) (getOperand(op1, false) - 1), false);
                return;
            case 0x15:
                setOperand(op1, (short) (~getOperand(op1, false)), false);
            case 0x1A:
                if (!this.f_equal) {
                    jump(getOperand(op1, false));
                }
                return;
            case 0x1B:
                if (this.f_equal) {
                    jump(getOperand(op1, false));
                }
                return;
            case 0x1C:
                if (this.f_greater || this.f_equal) {
                    jump(getOperand(op1, false));
                }
                return;
            case 0x1D:
                if (this.f_less || this.f_equal) {
                    jump(getOperand(op1, false));
                }
                return;
            case 0x1E:
                if (this.f_greater) {
                    jump(getOperand(op1, false));
                }
                return;
            case 0x1F:
                if (this.f_less) {
                    jump(getOperand(op1, false));
                }
                return;
            case 0x20:
                this.writeWord(--this.control_registers[6], this.control_registers[5]);
                this.jump(getOperand(op1, false));
                return;

            case 0x22:
                this.interrupt(getOperand(op1, false));
                return;
        }

        switch (inst) {
            case 0x21:
                this.jump(this.readWord(this.control_registers[6]++));
                return;
            case 0x23:
                this.halted = true;
                this.jump((short) 0);
                this.registers = new short[8];
                this.control_registers = new short[8];
                return;
            case 0x24:
                return;
            case 0x25:
                boot();
        }
    }

}
