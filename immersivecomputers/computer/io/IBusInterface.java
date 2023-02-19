package immersivecomputers.computer.io;

import immersivecomputers.computer.bus.BusMap;

public interface IBusInterface {
    BusMap getMap();

    void onData(short address, byte data);

    byte getData(short address);
}
