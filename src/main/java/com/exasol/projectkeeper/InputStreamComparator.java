package com.exasol.projectkeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This class compares if two {@link InputStream}s are equal.
 */
public class InputStreamComparator {

    /**
     * Compare if the two given {@link InputStream}s are equal.
     *
     * @param inputStream1 first {@link InputStream} to compare
     * @param inputStream2 second {@link InputStream} to compare
     * @return {@code true} if streams are equal
     * @throws IOException if reading fails
     */
    public boolean isEqual(final InputStream inputStream1, final InputStream inputStream2) throws IOException {
        final byte[] buffer1 = new byte[1024 * 10];
        final byte[] buffer2 = new byte[1024 * 10];
        int numRead1 = 0;
        int numRead2 = 0;
        while (true) {
            numRead1 = inputStream1.read(buffer1);
            numRead2 = inputStream2.read(buffer2);
            if (numRead1 > -1) {
                if (numRead2 != numRead1 || !Arrays.equals(buffer1, buffer2)) {
                    return false;
                }
                // continue since the streams are equal until here
            } else { // stream 1 was empty
                return numRead2 == -1;
            }
        }
    }
}
