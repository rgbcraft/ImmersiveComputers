package immersivecomputers.computer.thread;

import immersivecomputers.computer.Cpu;

import java.util.concurrent.ConcurrentLinkedQueue;

public class CpuRunner extends Thread {

    private final ConcurrentLinkedQueue<Cpu> processors;
    public int tasks = 0;

    public CpuRunner() {
        processors = new ConcurrentLinkedQueue<>();
    }

    public void addProcessor(Cpu cpu) {
        this.processors.add(cpu);
        tasks++;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            for (Cpu cpu : processors) {
                if (cpu.halted) {
                    processors.remove(cpu);
                    tasks--;
                    continue;
                }

                cpu.execute();
            }
        }
    }
}
