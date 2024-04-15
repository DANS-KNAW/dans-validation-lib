/*
 * Copyright (C) 2024 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.validation;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MutuallyExclusiveTest {

    @MutuallyExclusive(fields = { "field1", "field2" })
    private static class TestObject {
        private String field1;
        private String field2;
    }

    @MutuallyExclusive(fields = { "field1", "fieldXXX" })
    private static class TestObjectMisconfigured {
        private String field1;
        private String field2;
    }

    @Test
    public void should_return_true_when_one_of_two_fields_is_null() {
        var testObject = new TestObject();
        testObject.field1 = "value";
        testObject.field2 = null;

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void should_return_false_when_two_of_two_fields_are_non_null() {
        var testObject = new TestObject();
        testObject.field1 = "value";
        testObject.field2 = "value";

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("The fields [field1, field2] are mutually exclusive"));
        }
    }

    @Test
    public void should_return_true_when_two_of_two_fields_are_null() {
        var testObject = new TestObject();
        testObject.field1 = null;
        testObject.field2 = null;

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void should_throw_exception_when_field_does_not_exist() {
        var testObject = new TestObjectMisconfigured();
        testObject.field1 = "value";

        var exception = assertThrows(ValidationException.class, () -> {
            try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = factory.getValidator();
                validator.validate(testObject);
            }
        });
        assertThat(exception.getCause().getMessage()).isEqualTo("Programming error: field fieldXXX does not exist or is not accessible");
    }

}
