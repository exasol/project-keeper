# Developers Guide for Native-Image Java Projects

If you want to configure a new project for native-image builds please check the [corresponding guide](preparing_a_project_for_native_image_builds.md).

When the `native-image` module is enabled for a project, project-keeper configures the project to build a native image in the `package` phase. For that you need to install the GraalVM including the native-image tool.

* Download and unzip [GraalVM](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.0.0.2). Pick the latest version with same major and minor version as the `native-image-maven-plugin` in the `pom.xml`.
* Install native image by running `./bin/gu install native-image` in the unzipped GraalVM directory.
* Set `$JAVA_HOME` to the GraalVM directory.

## Building Without Native Image

Building the native image is quite resource expensive (~4GB RAM, 2 min CPU). If you just want to quickly run the build you can also skip it by adding `-P skipNativeImage` as parameter to `mvn`.