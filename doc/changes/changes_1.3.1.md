# Project keeper maven plugin 1.3.1, released 2021-10-14

Code name:Fixed Changes File Generation

## Summary

In this release we fixed a bug that caused a crash when a released project switched to maven dependency management. Now PK prints a warning and lists all dependencies as new in that case.

## Bug Fixes

* #187: Fixed changes file generation for projects switching to maven