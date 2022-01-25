package com.exasol.projectkeeper;

public interface Logger {

    public void info(String message);

    public void warn(String message);

    public void error(String message);
}
