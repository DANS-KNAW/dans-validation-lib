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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class SwordTokenTest {

    @Test
    public void should_return_false_if_no_sword_prefix() {
        var testObject = new Object() {

            @SwordToken
            private final String swordToken = "a8348df2-768d-4995-acc8-0ea878b05078";
        };

        // Validate
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("SWORD token must start with 'sword:' prefix"));
        }
    }

    @Test
    public void should_return_false_if_invalid_uuid() {
        var testObject = new Object() {

            @SwordToken
            private final String swordToken = "sword:not-a-uuid";
        };

        // Validate
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).hasSize(1);
            assertThat(violations).allMatch(v -> v.getMessage().equals("SWORD token must contain a valid UUID after the 'sword:' prefix"));
        }
    }

    @Test
    public void should_return_true_if_valid_sword_token() {
        var testObject = new Object() {

            @SwordToken
            private final String swordToken = "sword:a8348df2-768d-4995-acc8-0ea878b05078";
        };

        // Validate
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).isEmpty();
        }
    }
    
    @Test
    public void should_return_true_if_null() {
        var testObject = new Object() {

            @SwordToken
            private final String swordToken = null;
        };

        // Validate
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).isEmpty();
        }
    }

}
