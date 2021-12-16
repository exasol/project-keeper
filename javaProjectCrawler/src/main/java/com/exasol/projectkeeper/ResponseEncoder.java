package com.exasol.projectkeeper;

import com.exasol.errorreporting.ExaError;

public class ResponseEncoder {
    private static final String START_TOKEN = "###SerializedResponseStart###";
    private static final String END_TOKEN = "###SerializedResponseEnd###";

    @SuppressWarnings("java:S106") // printing is intended here.
    public void printResponse(String response) {
        if (response.contains(START_TOKEN) || response.contains(END_TOKEN)) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-JPC-1")
                    .message("Response contained invalid token.").toString().toString());
        }
        System.out.println(START_TOKEN);
        System.out.println(response);
        System.out.println(END_TOKEN);
    }
}
