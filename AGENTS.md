# Instructions for Agents

## Generated Workflows

Do not edit generated files in `.github/workflows/` directly.

* To customize a generated workflow for this repository, configure the workflow in `.project-keeper.yml`. See the workflow-customization section of `doc/user_guide/user_guide.md`.
* To change Project Keeper's default generated workflow content, edit the corresponding template in `project-keeper/src/main/resources/templates/.github/workflows/` (or `non_maven_templates` for non-Maven workflows).
* After changing templates or `.project-keeper.yml`, install the local Project Keeper build and regenerate managed files:

  ```sh
  mvn install -DossindexSkip=true -Dduplicate-finder.skip=true -DskipTests -DskipITs -Dproject-keeper.skip=true -Dossindex.skip=true -Dmaven.javadoc.skip=true -Djacoco.skip=true -Derror-code-crawler.skip=true -Dopenfasttrace.skip=true -T 1C
  mvn com.exasol:project-keeper-maven-plugin:fix --projects .
  ```
