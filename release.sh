#!/bin/bash

version=2.4.2

target_dir=$HOME/Downloads/pk-release/

rm -rf "$target_dir"
mkdir "$target_dir"
mvn clean deploy -Dgpg.skip=false
cp maven-project-crawler/target/project-keeper-java-project-crawler-*.jar "$target_dir"
cp maven-project-crawler/target/project-keeper-java-project-crawler-*-javadoc.jar "$target_dir"
cp maven-project-crawler/target/project-keeper-java-project-crawler-*-sources.jar "$target_dir"
cp project-keeper/target/project-keeper-core-$version-javadoc.jar "$target_dir"
cp project-keeper/target/project-keeper-core-$version-sources.jar "$target_dir"
cp project-keeper/target/project-keeper-core-$version.jar "$target_dir"
cp project-keeper-maven-plugin/target/project-keeper-maven-plugin-$version.jar "$target_dir"
cp project-keeper-maven-plugin/target/project-keeper-maven-plugin-$version-javadoc.jar "$target_dir"
cp project-keeper-maven-plugin/target/project-keeper-maven-plugin-$version-sources.jar "$target_dir"
cp shared-model-classes/target/project-keeper-shared-model-classes-$version.jar "$target_dir"
cp shared-model-classes/target/project-keeper-shared-model-classes-$version-javadoc.jar "$target_dir"
cp shared-model-classes/target/project-keeper-shared-model-classes-$version-sources.jar "$target_dir"
