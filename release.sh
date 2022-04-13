#!/bin/bash
rm -rf ~/Downloads/pk-release/
mkdir ~/Downloads/pk-release/
mvn clean deploy -Dgpg.skip=false
cp maven-project-crawler/target/project-keeper-java-project-crawler-*.jar ~/Downloads/pk-release/
cp maven-project-crawler/target/project-keeper-java-project-crawler-*-javadoc.jar ~/Downloads/pk-release/
cp maven-project-crawler/target/project-keeper-java-project-crawler-*-sources.jar ~/Downloads/pk-release/
cp project-keeper/target/project-keeper-core-2.3.0-javadoc.jar ~/Downloads/pk-release/
cp project-keeper/target/project-keeper-core-2.3.0-sources.jar ~/Downloads/pk-release/
cp project-keeper/target/project-keeper-core-2.3.0.jar ~/Downloads/pk-release/
cp project-keeper-maven-plugin/target/project-keeper-maven-plugin-2.3.0.jar ~/Downloads/pk-release/
cp project-keeper-maven-plugin/target/project-keeper-maven-plugin-2.3.0-javadoc.jar ~/Downloads/pk-release/
cp project-keeper-maven-plugin/target/project-keeper-maven-plugin-2.3.0-sources.jar ~/Downloads/pk-release/
cp shared-model-classes/target/project-keeper-shared-model-classes-2.3.0.jar ~/Downloads/pk-release/
cp shared-model-classes/target/project-keeper-shared-model-classes-2.3.0-javadoc.jar ~/Downloads/pk-release/
cp shared-model-classes/target/project-keeper-shared-model-classes-2.3.0-sources.jar ~/Downloads/pk-release/
