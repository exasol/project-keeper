package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import com.exasol.errorreporting.ExaError;

/**
 * This class prints a message to standard out, enclosed with start and end tokens.
 */
public class ResponseCoder {
    private static final String START_TOKEN = "###SerializedResponseStart###";
    private static final String END_TOKEN = "###SerializedResponseEnd###";

    /**
     * Create a new instance.
     */
    public ResponseCoder() {
        // Required to specify javadoc comment.
    }

    /**
     * Prints the given response to standard out, enclosed with start and end tokens.
     * 
     * @param response the response to print.
     */
    @SuppressWarnings("java:S106") // printing is intended here.
    public void printResponse(final String response) {
        if (response.contains(START_TOKEN) || response.contains(END_TOKEN)) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("F-PK-SMC-5").message("Response contained invalid token.").toString());
        }
        System.out.println(START_TOKEN);
        System.out.println(response);
        System.out.println(END_TOKEN);
    }

    /**
     * Decode a response.
     * 
     * @param response complete output of the crawler maven plugin
     * @return extracted response
     */
    public String decodeResponse(final String response) {
        final int startIndex = response.indexOf(START_TOKEN);
        final int responseStartIndex = startIndex + START_TOKEN.length() + 1;
        final int endIndex = response.indexOf(END_TOKEN);
        if (startIndex == -1 || endIndex == -1 || responseStartIndex > endIndex) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-SMC-79")
                    .message("Invalid response from crawler plugin: {{output}}.", response).ticketMitigation()
                    .toString());
        }
        return response.substring(responseStartIndex, endIndex).trim();
    }
}
