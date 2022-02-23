package com.exasol.projectkeeper.shared.dependencies;

import lombok.*;

/**
 * License.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {
    /** Name of the license */
    private String name;
    /** Link to the license */
    private String url;
}
