package de.sldk.mc.logging;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LoggerFactory {

    private static final LoggerBinder BINDER = new LoggerBinder();

    public static void setLogger(java.util.logging.Logger logger) {
        BINDER.setLogger(logger);
    }

    static java.util.logging.Logger getJavaLogger() {
        return BINDER.getLogger();
    }

    public static Logger getLogger()  {
        return new Logger(BINDER);
    }

    static class LoggerBinder {
        private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("PrometheusExporter");

        private LoggerBinder() {
        }

        private void setLogger(java.util.logging.Logger logger) {
            this.logger = logger;
        }

        java.util.logging.Logger getLogger() {
            return logger;
        }

        void log(Level level, Supplier<String> supplier, Throwable throwable, Object... parameters) {
            if (!logger.isLoggable(level)) {
                return;
            }

            String message = supplier.get();
            LogRecord record = new LogRecord(level, message);
            record.setLoggerName(logger.getName());
            record.setThrown(throwable);
            record.setParameters(parameters);
            logger.log(record);
        }
    }
}
