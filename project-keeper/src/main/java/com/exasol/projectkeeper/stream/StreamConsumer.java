package com.exasol.projectkeeper.stream;

import java.io.IOException;

/**
 * Callback interface used by {@link AsyncStreamReader}.
 */
public interface StreamConsumer {
    /**
     * Called when a new line was read from the input stream.
     * 
     * @param line the read line
     */
    void accept(String line);

    /**
     * Called when reading the input stream finished.
     */
    void readFinished();

    /**
     * Called when reading the input stream failed.
     * 
     * @param ioException the exception.
     */
    void readFailed(IOException ioException);
}
