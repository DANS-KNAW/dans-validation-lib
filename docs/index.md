Description
===========

Library with JavaBean validator annotations.

This library provides a set of ready-to-use JavaBean validator annotations. These annotations can be used to validate the fields of a JavaBean. Typically, this
is used in the configuration classes and DTOs (Data Transfer Objects) of DANS microservices.

Using the library
-----------------

To use this library in a Maven-based project, add the following to your `pom.xml`.

### 1. Declare the DANS maven repository

```xml

<repositories>
    <!-- possibly other repository declarations here ... -->
    <repository>
        <id>DANS</id>
        <releases>
            <enabled>true</enabled>
        </releases>
        <url>https://maven.dans.knaw.nl/releases/</url>
    </repository>
</repositories>
```

### 2. Include a dependency on this library

```xml

<dependency>
    <groupId>nl.knaw.dans</groupId>
    <artifactId>dans-validation-lib</artifactId>
    <version>{version}</version> <!-- <=== FILL LIBRARY VERSION TO USE HERE -->
</dependency>
```
