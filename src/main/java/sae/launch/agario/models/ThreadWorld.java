package sae.launch.agario.models;

public class ThreadWorld extends Thread {
    private Game game;
    private Runnable task;

    public ThreadWorld(Game game, Runnable runnable) {
        super();
        this.game = game;
        this.task = runnable;
    }

    @Override
    public void run() {
        final int TARGET_FPS = 5;
        final int OPTIMAL_TIME = 1000 / TARGET_FPS;
        while (true) {
            long lastTime = System.nanoTime();


            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;

            long delta = elapsedTime / 1000000;

            task.run();

            long sleepTime = OPTIMAL_TIME - delta;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}


