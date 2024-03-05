package com.exasol.projectkeeper.validators.release;

import static java.util.Collections.emptyList;

import java.nio.file.Path;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.exasol.errorreporting.ErrorMessageBuilder;
import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

class ChangesFileReleaseValidator implements Validator {
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private final ChangesFile changesFile;
    private final Path changesFilePath;
    private final Clock clock;

    ChangesFileReleaseValidator(final Path changesFilePath, final ChangesFile changesFile) {
        this(changesFilePath, changesFile, Clock.systemUTC());
    }

    ChangesFileReleaseValidator(final Path changesFilePath, final ChangesFile changesFile, final Clock clock) {
        this.changesFilePath = changesFilePath;
        this.changesFile = changesFile;
        this.clock = clock;
    }

    @Override
    public List<ValidationFinding> validate() {
        return Stream.of(validateReleaseDate()) //
                .flatMap(List::stream).toList();
    }

    // [impl->dsn~verify-release-mode.verify-release-date~1]
    private List<ValidationFinding> validateReleaseDate() {
        final Optional<LocalDate> releaseDate = changesFile.getParsedReleaseDate();
        if (releaseDate.isEmpty()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-182").message(
                    "Release date {{release date}} has invalid format in {{changes file path}}",
                    changesFile.getReleaseDate(), changesFilePath));
        }
        final LocalDate today = today();
        if (!releaseDate.get().equals(today)) {
            return finding(ExaError.messageBuilder("E-PK-CORE-183").message(
                    "Release date {{actual date}} must be today {{today}} in {{changes file path}}", releaseDate.get(),
                    today, changesFilePath));
        }
        return noFindings();
    }

    private LocalDate today() {
        return LocalDate.ofInstant(clock.instant(), UTC_ZONE);
    }

    private List<ValidationFinding> noFindings() {
        return emptyList();
    }

    private List<ValidationFinding> finding(final ErrorMessageBuilder message) {
        return List.of(SimpleValidationFinding.withMessage(message.toString()).build());
    }
}
