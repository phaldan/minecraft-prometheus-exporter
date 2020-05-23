package de.sldk.mc.logging;

import de.sldk.mc.logging.LoggerFactory.LoggerBinder;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Logger {

    private final LoggerBinder binder;

    Logger(LoggerBinder binder) {
        this.binder = binder;
    }

    public void severe(String message, Throwable e) {
        severe(() -> message, e);
    }

    public void severe(Supplier<String> supplier, Throwable e) {
        log(Level.SEVERE, supplier, e);
    }

    public void severe(String message, Object... parameters) {
        severe(() -> message, parameters);
    }

    public void severe(Supplier<String> supplier, Object... parameters) {
        log(Level.SEVERE, supplier, parameters);
    }

    public void warn(String message, Throwable e) {
        warn(() -> message, e);
    }

    public void warn(Supplier<String> supplier, Throwable e) {
        log(Level.WARNING, supplier, e);
    }

    public void warn(String message, Object... parameters) {
        warn(() -> message, parameters);
    }

    public void warn(Supplier<String> supplier, Object... parameters) {
        log(Level.WARNING, supplier, parameters);
    }

    public void info(String message, Throwable e) {
        info(() -> message, e);
    }

    public void info(Supplier<String> supplier, Throwable e) {
        log(Level.INFO, supplier, e);
    }

    public void info(String message, Object... objects) {
        info(() -> message, objects);
    }

    public void info(Supplier<String> supplier, Object... objects) {
        log(Level.INFO, supplier, objects);
    }

    private void log(Level level, Supplier<String> supplier, Object... parameters) {
        Throwable throwable = null;
        Object[] objects = parameters;
        int lastIndex = parameters.length - 1;
        Object last = parameters.length > 0 ? parameters[lastIndex] : null;
        if (Objects.nonNull(last) && last instanceof Throwable) {
            throwable = (Throwable) last;
            objects = Arrays.copyOfRange(parameters, 0, lastIndex);
        }
        binder.log(level, supplier, throwable, objects);
    }
}
