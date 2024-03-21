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

import lombok.Value;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UniqueAttributeTest {

    @Value
    static
    class TestElement {
        String field1;
        Integer field2;
    }

    @Value
    public static class Container {
        @UniqueAttribute(attribute = "field1")
        List<TestElement> elements;
    }

    @Test
    public void is_valid_when_field1_is_unique() {
        var container = new Container(List.of(new TestElement("a", 1), new TestElement("b", 2)));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void is_not_valid_when_field1_is_not_unique() {
        var container = new Container(List.of(new TestElement("a", 1), new TestElement("a", 2)));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("attribute field1 must be unique in the list of objects"));
        }
    }

    @Test
    public void is_valid_when_field1_is_null() {
        var container = new Container(List.of(new TestElement(null, 1), new TestElement("a", 2)));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void is_valid_when_list_is_empty() {
        var container = new Container(List.of());
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void is_valid_when_list_is_null() {
        var container = new Container(null);
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void is_valid_when_field1_is_null_for_all_elements() {
        var container = new Container(List.of(new TestElement(null, 1), new TestElement(null, 2)));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void is_valid_when_field1_is_unique_for_all_elements() {
        var container = new Container(List.of(new TestElement("a", 1), new TestElement("b", 2), new TestElement("c", 3)));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void is_valid_when_field2_is_not_unique() {
        var container = new Container(List.of(new TestElement("a", 1), new TestElement("b", 1)));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Container>> violations = validator.validate(container);
            assertThat(violations).isEmpty();
        }
    }
}

