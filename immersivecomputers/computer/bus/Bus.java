package immersivecomputers.computer.bus;

import immersivecomputers.computer.Cpu;
import immersivecomputers.computer.io.IBusInterface;
import immersivecomputers.computer.memory.Memory;

public class Bus {
    public IBusInterface[] io;
    public Memory memory;
    public Cpu cpu;

    public Bus(Cpu cpu) {
        this.io = new IBusInterface[0xFFF];
        this.memory = new Memory(0xFFFF);
        this.cpu = cpu;
    }

    public void write(short address, byte data) {
        if (address < 0x400) {
            io[address].onData(address, data);
        } else {
            memory.setData(address, data);
        }
    }

    public void map(IBusInterface in) {
        for (short i=in.getMap().memory_start; i < in.getMap().memory_end; i++) {
            this.io[i] = in;
        }
    }

    public byte read(short address) {
        if (address < 0x400) {
            return io[address].getData(address);
        } else {
            return memory.getData(address);
        }
    }

    public void interrupt(short vector) {
        cpu.interrupt(vector);
    }
}
