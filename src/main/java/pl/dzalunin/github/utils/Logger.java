package pl.dzalunin.github.utils;

public class Logger {

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void logStackTrace(Throwable throwable) {
        throwable.printStackTrace(System.err);
    }

}
