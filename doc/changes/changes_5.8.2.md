# Project Keeper 5.8.2, unreleased

Code name: Minimal GitHub REST Client

## Summary

Project Keeper now uses a minimal Java HTTP client to read GitHub issue state during release verification.

## Features

* #799: Replace `jcabi-github` with a minimal GitHub REST client and remove its transitive JAXB dependency.

## Security

Removed the temporary OSS Index suppression for CVE-2026-92877 after removing the affected `jcabi-github` dependency.
