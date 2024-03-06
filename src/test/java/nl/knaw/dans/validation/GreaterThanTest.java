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

import io.dropwizard.util.DataSize;
import lombok.Value;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class GreaterThanTest {

    @Value
    @GreaterThan(field1 = "field1", field2 = "field2")
    private static class TestObject {
        DataSize field1;
        DataSize field2;
    }

    @Test
    public void testIsValid() {
        var testObject = new TestObject(DataSize.gigabytes(2), DataSize.gigabytes(1));

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void testIsNotValid() {
        var testObject = new TestObject(DataSize.gigabytes(1), DataSize.gigabytes(2));

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("field1 must be larger than field2"));
        }
    }

    @Test
    public void testIsNotValidWhenField1IsNull() {
        var testObject = new TestObject(null, DataSize.gigabytes(2));

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("field1 must be larger than field2"));
        }
    }

    @Test
    public void testIsNotValidWhenField2IsNull() {
        var testObject = new TestObject(DataSize.gigabytes(2), null);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("field1 must be larger than field2"));
        }
    }

    @Test
    public void testIsNotValidWhenBothFieldsAreNull() {
        var testObject = new TestObject(null, null);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("field1 must be larger than field2"));
        }
    }

    @Test
    public void testIsNotValidWhenField1IsEqualToField2() {
        var testObject = new TestObject(DataSize.gigabytes(2), DataSize.gigabytes(2));

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("field1 must be larger than field2"));
        }
    }

    @Test
    public void testIsValidUnitIsDifferent() {
        var testObject = new TestObject(DataSize.gigabytes(2), DataSize.megabytes(2));

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<TestObject>> violations = validator.validate(testObject);

            assertThat(violations).isEmpty();
        }
    }
}
