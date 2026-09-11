Getting started
===============

Adding the dependency
---------------------

To use this parent POM in a Maven project, add the following to your `pom.xml`:

```xml

<dependency>
    <groupId>nl.datastations</groupId>
    <artifactId>dans-validation-lib</artifactId>
    <version>{{ project_version }}</version>
</dependency>
```

Using a validator annotation
-----------------------------

The constraints in this library are standard JSR-380 (Bean Validation) annotations. Annotate a field with, for example, `@Uuid`, and validate the object with a
regular `javax.validation.Validator`.

```java
import javax.validation.Validation;

import nl.knaw.dans.validation.Uuid;

class DepositRequest {

    @Uuid
    private String depositId;

    public DepositRequest(String depositId) {
        this.depositId = depositId;
    }
}

class Main {
    public static void main(String[] args) {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();

            var request = new DepositRequest("not-a-uuid");
            var violations = validator.validate(request);

            for (var violation : violations) {
                System.out.println(violation.getMessage());
            }
        }
    }
}
```

Many frameworks use Bean Validation under the hood, so you can also use these annotations in Spring Boot, Dropwizard, and other frameworks.

