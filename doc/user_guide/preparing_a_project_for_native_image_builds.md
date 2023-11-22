# Preparing a project for native-image builds

PK can configure a project to use Graal VM for building platform specific executables for Windows, Mac OS and Linux. Currently, only for amd64 architecture.

## How To

* Add the `native_image` module to your `.project-keeper.yml`.
* Add integration tests that use the native image. This step is important because the native image can fail where the JAR works fine. A typical reason is that the native-image does not fully support reflection out of the box.
* If tests fail because of reflection, add the classes you need reflection for to the generated `src/main/reflect-config.json`. For details see the [Java docs](https://docs.oracle.com/cd/F44923_01/enterprise/20/docs/reference-manual/native-image/Reflection/).
* Tag the tests that use the native image with `@Tag("native-image")`.
* Follow the [Developer Guide for Native-Image Java Projects](building_a_native_image_project.md) for building the project.
