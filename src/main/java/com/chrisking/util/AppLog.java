package com.chrisking.util;

import java.io.IOException;
import java.util.logging.*;

public final class AppLog {
    private static boolean initialized = false;

    private AppLog() {}

    public static synchronized void init() {
        if (initialized) return;

        Logger root = Logger.getLogger("");

        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }

        try {
            FileHandler fh = new FileHandler("app.log", true);
            fh.setFormatter(new SimpleFormatter());
            root.addHandler(fh);
        } catch (IOException ioe) {
            ConsoleHandler ch = new ConsoleHandler();
            root.addHandler(ch);
        }

        root.setLevel(Level.INFO);
        initialized = true;
    }

    public static Logger get(Class<?> cls) {
        return Logger.getLogger(cls.getName());
    }
}
