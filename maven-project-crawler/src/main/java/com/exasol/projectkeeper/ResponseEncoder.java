package com.exasol.projectkeeper;

import com.exasol.errorreporting.ExaError;

/**
 * This class prints a message to standard out, enclosed with start and end tokens.
 */
public class ResponseEncoder {
    private static final String START_TOKEN = "###SerializedResponseStart###";
    private static final String END_TOKEN = "###SerializedResponseEnd###";

    /**
     * Prints the given response to standard out, enclosed with start and end tokens.
     * 
     * @param response the response to print.
     */
    @SuppressWarnings("java:S106") // printing is intended here.
    public void printResponse(final String response) {
        if (response.contains(START_TOKEN) || response.contains(END_TOKEN)) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("F-PK-MPC-1").message("Response contained invalid token.").toString());
        }
        System.out.println(START_TOKEN);
        System.out.println(response);
        System.out.println(END_TOKEN);
    }
}
