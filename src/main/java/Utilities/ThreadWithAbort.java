package Utilities;

public class ThreadWithAbort extends Thread {

    private boolean mIsRunning = true;

    public ThreadWithAbort(String name) {
        super(name);
    }

    public boolean getIsRunning()
    {
        return mIsRunning;
    }

    public void abort()
    {
        mIsRunning = false;
    }
}