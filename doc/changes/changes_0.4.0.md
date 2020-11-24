#Project keeper maven plugin 0.4.0, released 2020-11-24

Code name: `udf_coverage` module and template improvements

## Features / Enhancements

* #19: Added a configurable list for excluding files from validation
* #35: Updated settings for the code formatter 
* #41: Added validation for udf_coverage
* #36: Moved `assembly/all-dependencies.xml` to `src/assembly/all-dependencies.xml`

## Refactoring 

* #43 Refactored tests for PomValidator

## Dependency Updates:

* Added `junit:junit` 4.13.1
* Updated `org.testcontainers:testcontainers` from 1.14.3 to 1.15.0
* Updated `org.testcontainers:junit-jupiter` from 1.14.3 to 1.15.0