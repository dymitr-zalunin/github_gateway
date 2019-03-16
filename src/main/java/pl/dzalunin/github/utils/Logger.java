package pl.dzalunin.github.utils;

public class Logger {

    public static synchronized void log(String msg) {
        System.out.println(Thread.currentThread().getName() + " : " + msg);
    }

    public static synchronized void logStackTrace(Throwable throwable) {
        System.err.println(Thread.currentThread().getName() + " : ");
        throwable.printStackTrace(System.err);
    }

}
