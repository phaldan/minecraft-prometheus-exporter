package de.sldk.mc.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoggerTest {

    private java.util.logging.Logger backup;
    private java.util.logging.Logger mock;
    private Logger logger;

    @BeforeEach
    public void setup() {
        backup = LoggerFactory.getJavaLogger();
        mock = mock(java.util.logging.Logger.class);
        LoggerFactory.setLogger(mock);
        logger = LoggerFactory.getLogger();
    }

    @AfterEach
    public void teardown() {
        LoggerFactory.setLogger(backup);
    }

    public static Stream<Arguments> providerIsLoggable() {
        return Stream.of(
                Arguments.of(Level.SEVERE, (BiConsumer<Logger, String>) Logger::severe),
                Arguments.of(Level.WARNING, (BiConsumer<Logger, String>) Logger::warn),
                Arguments.of(Level.INFO, (BiConsumer<Logger, String>) Logger::info)
        );
    }

    @ParameterizedTest
    @MethodSource("providerIsLoggable")
    public void testIsLoggable(Level level, BiConsumer<Logger, String> consumer) {
        when(mock.isLoggable(eq(level))).thenReturn(false);
        verify(mock, times(0)).getName();
        verify(mock, times(0)).log(any(LogRecord.class));
        consumer.apply(logger, "test message");
    }

    public static Stream<Arguments> providerOnlyMessage() {
        return Stream.of(
                Arguments.of(Level.SEVERE, (BiConsumer<Logger, String>) Logger::severe),
                Arguments.of(Level.WARNING, (BiConsumer<Logger, String>) Logger::warn),
                Arguments.of(Level.INFO, (BiConsumer<Logger, String>) Logger::info)
        );
    }

    @ParameterizedTest
    @MethodSource("providerOnlyMessage")
    public void testOnlyWithMessage(Level level, BiConsumer<Logger, String> consumer) {
        when(mock.isLoggable(eq(level))).thenReturn(true);
        when(mock.getName()).thenReturn("ExampleLogger");
        doAnswer(invocation -> {
            LogRecord record = invocation.getArgument(0);
            assertRecord(record, level, "test message", "ExampleLogger");
            assertThat(record.getParameters()).isEmpty();
            assertThat(record.getThrown()).isNull();
            return null;
        }).when(mock).log(any(LogRecord.class));
        consumer.apply(logger, "test message");
    }

    public static Stream<Arguments> providerWithException() {
        return Stream.of(
                Arguments.of(Level.SEVERE, (ThreeConsumer<Logger, String, Exception>) Logger::severe),
                Arguments.of(Level.WARNING, (ThreeConsumer<Logger, String, Exception>) Logger::warn),
                Arguments.of(Level.INFO, (ThreeConsumer<Logger, String, Exception>) Logger::info)
        );
    }

    @ParameterizedTest
    @MethodSource("providerWithException")
    public void testWithException(Level level, ThreeConsumer<Logger, String, Exception> consumer) {
        when(mock.isLoggable(eq(level))).thenReturn(true);
        when(mock.getName()).thenReturn("ExampleLogger");
        Exception exception = new Exception("test");
        doAnswer(invocation -> {
            LogRecord record = invocation.getArgument(0);
            assertRecord(record, level, "test message", "ExampleLogger");
            assertThat(record.getParameters()).isEmpty();
            assertThat(record.getThrown()).isEqualTo(exception);
            return null;
        }).when(mock).log(any(LogRecord.class));
        consumer.apply(logger, "test message", exception);
    }

    public static Stream<Arguments> providerWithParameter() {
        return Stream.of(
                Arguments.of(Level.SEVERE, (ThreeConsumer<Logger, String, Integer>) Logger::severe),
                Arguments.of(Level.WARNING, (ThreeConsumer<Logger, String, Integer>) Logger::warn),
                Arguments.of(Level.INFO, (ThreeConsumer<Logger, String, Integer>) Logger::info)
        );
    }

    @ParameterizedTest
    @MethodSource("providerWithParameter")
    public void testWithParameter(Level level, ThreeConsumer<Logger, String, Integer> consumer) {
        when(mock.isLoggable(eq(level))).thenReturn(true);
        when(mock.getName()).thenReturn("ExampleLogger");
        doAnswer(invocation -> {
            LogRecord record = invocation.getArgument(0);
            assertRecord(record, level, "test message", "ExampleLogger");
            assertThat(record.getParameters()).isEqualTo(new Object[]{ 123 });
            assertThat(record.getThrown()).isNull();
            return null;
        }).when(mock).log(any(LogRecord.class));
        consumer.apply(logger, "test message", 123);
    }

    public static Stream<Arguments> providerWithParameterAndException() {
        return Stream.of(
                Arguments.of(Level.SEVERE, (FourConsumer<Logger, String, Integer, Exception>) Logger::severe),
                Arguments.of(Level.WARNING, (FourConsumer<Logger, String, Integer, Exception>) Logger::warn),
                Arguments.of(Level.INFO, (FourConsumer<Logger, String, Integer, Exception>) Logger::info)
        );
    }

    @ParameterizedTest
    @MethodSource("providerWithParameterAndException")
    public void testWithParameterAndException(Level level, FourConsumer<Logger, String, Integer, Exception> consumer) {
        when(mock.isLoggable(eq(level))).thenReturn(true);
        when(mock.getName()).thenReturn("ExampleLogger");
        Exception exception = new Exception("test");
        doAnswer(invocation -> {
            LogRecord record = invocation.getArgument(0);
            assertRecord(record, level, "test {0}", "ExampleLogger");
            assertThat(record.getParameters()).isEqualTo(new Object[]{ 123 });
            assertThat(record.getThrown()).isEqualTo(exception);
            return null;
        }).when(mock).log(any(LogRecord.class));
        consumer.apply(logger, "test {0}", 123, exception);
    }

    private void assertRecord(LogRecord record, Level level, String message, String logger) {
        assertThat(record.getLevel()).isEqualTo(level);
        assertThat(record.getMessage()).isEqualTo(message);
        assertThat(record.getLoggerName()).isEqualTo(logger);
        assertThat(record.getThreadID()).isGreaterThan(0);
        assertThat(record.getMillis()).isNotNull();
        assertThat(record.getSequenceNumber()).isNotNull();
        assertThat(record.getResourceBundle()).isNull();
        assertThat(record.getResourceBundleName()).isNull();
        assertThat(record.getSourceClassName()).isNull();
        assertThat(record.getSourceMethodName()).isNull();
    }

    @FunctionalInterface
    interface FourConsumer<One, Two, Three, Four> {
        void apply(One one, Two two, Three three, Four four);
    }

    @FunctionalInterface
    interface ThreeConsumer<One, Two, Three> {
        void apply(One one, Two two, Three three);
    }

    @FunctionalInterface
    interface BiConsumer<One, Two> {
        void apply(One one, Two two);
    }
}