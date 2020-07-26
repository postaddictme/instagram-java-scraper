package me.postaddict.instagram.scraper;

import org.apache.log4j.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Logger {

    private static final ThreadLocal<Logger> instancePull = ThreadLocal.withInitial(() -> null);
    private static org.apache.log4j.Logger LOG4J = LogManager.getLogger(Logger.class);
    private static int rpStrLength = 120;
    private static ThreadLocal<String> testIdCache = ThreadLocal.withInitial(() -> null);
    private List<Object> collectionLogs = Collections.synchronizedList(new ArrayList<>());

    private Logger() {
    }

    /**
     * Implementation of the Singleton pattern
     *
     * @return Logger instance
     */
    public synchronized static Logger getInstance() {
        return Optional.ofNullable(instancePull.get()).orElseGet(() -> {
            instancePull.set(new Logger());
            return instancePull.get();
        });
    }

    /**
     * Logging a step number
     */
    public String step(String message) {
        int msgLength = message.length();
        message = message.length() < rpStrLength ? message + String.format("%1$" + (rpStrLength - msgLength) + "s", " ") : message;
        LOG4J.info(addThreadIdToMessage(String.format("%n%n >>>>>> %s <<<<<< %n", message)));
        return message;
    }

    /**
     * log info
     *
     * @param message
     */
    public void info(Object message) {
        LOG4J.info(addThreadIdToMessage(message));
    }

    /**
     * Log error
     *
     * @param message
     * @param throwable
     */
    public void error(Object message, Throwable throwable) {
        LOG4J.error(addThreadIdToMessage(message), throwable);
    }

    /**
     * Log debug
     *
     * @param message
     */
    public void error(Object message) {
        LOG4J.error(addThreadIdToMessage(message));
    }

    /**
     * Log debug without throwable
     *
     * @param message
     */
    public void debug(Object message) {
        LOG4J.debug(addThreadIdToMessage(message));
    }

    /**
     * Warning log
     *
     * @param message Message
     */
    public void warn(String message) {
        LOG4J.warn(addThreadIdToMessage(message));
    }

    /**
     * Fatal log
     *
     * @param message Message
     */
    public void fatal(final String message) {
        LOG4J.fatal(addThreadIdToMessage(message));
    }

    /**
     * Collection Logs
     *
     * @param message Message
     */
    public void addToCollectionLogs(Object message) {
        collectionLogs.add(addThreadIdToMessage(message));
    }

    public void printCollectionLogs() {
        collectionLogs = instancePull.get().collectionLogs.stream().map(message -> String.format("%n" + message)).collect(Collectors.toList());
        info(collectionLogs);
    }

    private Object addThreadIdToMessage(Object message) {
        Long currentThreadId = Thread.currentThread().getId();
        if (testIdCache.get() != null) {
            return testIdCache.get() + "{Thread ID:" + currentThreadId + "} " + message;
        }
        return "{Thread ID:" + currentThreadId + "} " + message;
    }
}

