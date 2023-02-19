package immersivecomputers.computer.memory;

public class Memory {
    public byte[] data;

    public Memory(int size) {
        this.data = new byte[size];
    }

    public byte getData(int addr) throws IndexOutOfBoundsException {
        return this.data[addr];
    }

    public void setData(int addr, byte value) throws IndexOutOfBoundsException {
        this.data[addr] = value;
    }
}
