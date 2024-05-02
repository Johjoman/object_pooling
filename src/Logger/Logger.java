package Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
* Logger class developed by Joseph Nemeth and taken from another project.
*/
public class Logger {
    private static final String LOG_FILE_NAME = "object_pool_log-%s.txt";
    private static String LOG_FILE_NAME_NO_DIR;
    private static String DIRECTORY;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static Logger instance = null;

    public enum LogLevel {
        CRITICAL,
        WARNING,
        INFO
    }

    private final String logFileName;

    private Logger(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LocalDateTime now = LocalDateTime.now();
        DIRECTORY = directory;
        LOG_FILE_NAME_NO_DIR = String.format(LOG_FILE_NAME, now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));
        logFileName = String.format(directory + File.separator + LOG_FILE_NAME, now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger("logs");
        }
        return instance;
    }

    public void log(String message, LogLevel logLevel) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String callingClassName = "PSVM";
        if (stackTraceElements.length > 2) {
            callingClassName = stackTraceElements[2].getClassName();
        }

        String logMessage = String.format("[%s] [%s] [%s] %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")),
                logLevel.toString(),
                callingClassName,
                message);
        try {
            System.out.println(logMessage);
            writeToFile(logMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }



    public void read(LogLevel logLevel) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogLevel messageLogLevel = getLogLevelFromLine(line);
                if (messageLogLevel != null && messageLogLevel.ordinal() <= logLevel.ordinal()) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read log file: " + e.getMessage());
        }
    }

    public void read(LogLevel logLevel, Class<?> clazz) {
        String classNameToFilter = clazz.getName();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LogLevel messageLogLevel = getLogLevelFromLine(line);
                String classNameInLog = getClassNameFromLine(line);

                if (messageLogLevel != null
                        && messageLogLevel.ordinal() <= logLevel.ordinal()
                        && classNameInLog.equals(classNameToFilter)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read log file: " + e.getMessage());
        }
    }

    public void filterLogsByClass(LogLevel logLevel, Class<?> clazz) {
        String classNameToFilter = clazz.getSimpleName();
        String filteredLogFileName = DIRECTORY + File.separator + String.format("%s-%s", classNameToFilter, LOG_FILE_NAME_NO_DIR);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filteredLogFileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                LogLevel messageLogLevel = getLogLevelFromLine(line);
                String classNameInLog = getClassNameFromLine(line);

                if (messageLogLevel != null
                        && messageLogLevel.ordinal() <= logLevel.ordinal()
                        && classNameInLog.equals(classNameToFilter)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to filter log file: " + e.getMessage());
        }
    }

    private String getClassNameFromLine(String line) {
        int startIndex = line.indexOf("]", line.indexOf("]") + 1) + 2;
        int endIndex = line.indexOf(" ", startIndex);
        return line.substring(startIndex, endIndex);
    }

    private LogLevel getLogLevelFromLine(String line) {
        if (line.contains("[CRITICAL]")) {
            return LogLevel.CRITICAL;
        } else if (line.contains("[WARNING]")) {
            return LogLevel.WARNING;
        } else if (line.contains("[INFO]")) {
            return LogLevel.INFO;
        } else {
            return null;
        }
    }

    private void writeToFile(String logMessage) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            writer.write(logMessage);
            writer.newLine();
        }
    }
}
