package main.java.logger;

public enum LogLevel {
    Error("ERR"),
    Warning("WRN"),
    Debug("DBG");

    private String shortName;

    LogLevel(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
