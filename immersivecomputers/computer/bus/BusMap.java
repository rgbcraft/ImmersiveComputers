package immersivecomputers.computer.bus;

public class BusMap {
    public short memory_start, memory_end;

    public BusMap(short start, short end) {
        this.memory_start = start;
        this.memory_end = end;
    }
}
