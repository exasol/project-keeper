package com.exasol.projectkeeper.github;

import com.exasol.errorreporting.ExaError;

// [impl->dsn~verify-modes.output-parameters~1]
class KeyValuePairFormatter {
    private static final String EOF_MARKER = "EOF";

    String format(final String key, final String value) {
        final String trimmedKey = key.trim();
        if (trimmedKey.isEmpty() || !trimmedKey.equals(key)) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-190")
                    .message("Key {{key}} is empty or contains leading or trailing whitespace.", key).toString());
        }
        if (value == null) {
            return key + "=";
        }
        if (value.contains("\n")) {
            return key + "<<" + EOF_MARKER + "\n" //
                    + value + "\n" //
                    + EOF_MARKER;
        }
        return key + "=" + value;
    }
}
