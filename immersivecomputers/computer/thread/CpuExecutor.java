package immersivecomputers.computer.thread;

import immersivecomputers.computer.Cpu;

public class CpuExecutor {
    private final int MAX_THREADS = 4;
    private final Thread[] threads;
    public boolean started = false;

    public CpuExecutor() {
        threads = new Thread[MAX_THREADS];
    }

    public void addProcessor(Cpu cpu) {
        if (!started) {
            this.start();
        }

        Thread chosen = null;

        for (Thread runner : threads) {
            if (chosen == null) {
                chosen = runner;
            } else {
                if (((CpuRunner) runner).tasks < ((CpuRunner) chosen).tasks) {
                    chosen = runner;
                }
            }
        }

        if (chosen == null) {
            threads[0] = new CpuRunner();
            chosen = threads[0];
        }

        ((CpuRunner)chosen).addProcessor(cpu);
    }

    public void init() {
        for (int i=0; i < MAX_THREADS; i++) {
            this.threads[i] = new CpuRunner();
        }
    }

    public void start() {
        started = true;

        for (int i=0; i < MAX_THREADS; i++) {
            this.threads[i].start();
        }
    }

    public void shutdown() {
        for (int i=0; i < MAX_THREADS; i++) {
            threads[i].interrupt();
        }
    }

}
