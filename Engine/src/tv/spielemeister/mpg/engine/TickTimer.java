package tv.spielemeister.mpg.engine;

public abstract class TickTimer implements Runnable {

    Thread timerThread = null;

    private double ns, delta = 0;
    private boolean running = false;

    public TickTimer(double tps){
        ns = 1000000000 / tps;
    }

    public void start(){
        timerThread = new Thread(this);
        timerThread.setDaemon(true);
        running = false;
        timerThread.start();
    }

    public void stop(){
        running = false;
        timerThread = null;
    }

    public abstract void tick();

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long now;
        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                delta--;
            }
        }
    }

}
