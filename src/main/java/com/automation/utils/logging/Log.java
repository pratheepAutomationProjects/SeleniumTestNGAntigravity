package com.automation.utils.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread-safe logging utility wrapping Apache Log4j2.
 */
public final class Log {

    private static final Logger logger = LogManager.getLogger(Log.class);

    private Log() {}

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String format, Object... args) {
        logger.info(String.format(format, args));
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void warn(String format, Object... args) {
        logger.warn(String.format(format, args));
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String format, Object... args) {
        logger.error(String.format(format, args));
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void debug(String format, Object... args) {
        logger.debug(String.format(format, args));
    }

    public static void fatal(String message) {
        logger.fatal(message);
    }

    public static void fatal(String format, Object... args) {
        logger.fatal(String.format(format, args));
    }
}
