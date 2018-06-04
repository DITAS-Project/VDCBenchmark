package model;

public class RequestMetaData {
    private int varianz;
    private int threads;
    private int delay;

    public int getVarianz() {
        return varianz;
    }

    public void setVarianz(int varianz) {
        this.varianz = varianz;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
