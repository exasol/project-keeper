# Design

## Project Structure Definition
`dsn~project-structure-definition~1`

The project structure is defined by a file structure in the resources folder of this plugin.
There are two folders: `require_exist` and `require_exact`. 
For files in the `require_exist` folder, this plugin only checks if they exist.
If they don't exist it creates the file with the content from the file in `require_exist` as a template.

For the files in the `require_exact` this plugin also validates that the content is exactly the same.

Rationale:
This way of defining the project structure makes it very convenient to add new content.
Now knowledge about the internal implementation of this plugin is required.
That allows users to quickly add or modify the template.

It is not a problem that both folders overlap. In case a file is defined in both directories, 
this plugin validates it as if it would only exist in `require_exact`. 

Covers:

* `req~repository-structure~1`
* `req~convenient-repository-structure~1`

Needs: impl

## Maven Verify Goal
`dsn~mvn-verify-goal~1`

This plugin defines a maven goal named `verify` that checks if the project matches the defined structure.
The default maven lifecycle phase for this goal is the `package` phase.

Covers:

* `req~verify-repo-structure~1`

Needs: impl, itest

## Maven fit Gaol
`dsn~mvn-fit-pahse~1`

This plugin defines a maven goal named `fit` that creates or updates the project files so that they match 
the required project structure. 

Covers:

* `req~fit-repo-structure~1`
* `req~verify-repo-structure~1`

Needs: impl, itest