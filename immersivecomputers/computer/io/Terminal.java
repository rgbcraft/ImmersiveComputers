package immersivecomputers.computer.io;

import immersivecomputers.computer.bus.Bus;
import immersivecomputers.computer.bus.BusMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Terminal implements IBusInterface {
    public List<Byte> content;
    public short address;
    public Queue<Byte> keyQueue;
    public Bus bus;
    public BusMap map;

    public Terminal() {
        content = new ArrayList<>();
        keyQueue = new ArrayDeque<>();
        address = 0;
        map = new BusMap((short) 0x0, (short) 0x1);
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public void pressKey(byte digit) {
        if (this.bus != null) {
            this.bus.interrupt(InterruptVectors.TERMINAL_VECTOR);
            this.keyQueue.add(digit);
        }
    }

    @Override
    public BusMap getMap() {
        return map;
    }

    @Override
    public void onData(short address, byte data) {
        if (address == 0x0) {
            if (data == 0x7F) {
                if (content.size() > 0) {
                    content.remove(content.size() - 1);
                }
            } else {
                content.add(data);
            }
        }
    }

    @Override
    public byte getData(short address) {
        if (address == 0x1) {
            if (this.keyQueue.size() > 0) {
                return this.keyQueue.poll();
            } else {
                return 0;
            }
        } else return 0;
    }
}
