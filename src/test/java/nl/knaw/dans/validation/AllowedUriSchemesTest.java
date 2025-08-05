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

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class AllowedUriSchemesTest {

    @Test
    public void should_return_true_for_valid_uri_schemes() throws Exception {
        var testObject = new Object() {

            @AllowedUriSchemes(schemes = { "http", "https" })
            private final URI validUri = URI.create("http://example.com");
        };

        // Validate
        try (var factory = javax.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void should_return_false_for_invalid_uri_scheme() throws Exception {
        var testObject = new Object() {

            @AllowedUriSchemes(schemes = { "http", "https" })
            private final URI invalidUri = URI.create("ftp://example.com");
        };

        // Validate
        try (var factory = javax.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Invalid URI scheme: 'ftp'; allowed schemes are: 'http', 'https'");
        }
    }

    @Test
    public void should_return_true_for_null_uri() throws Exception {
        var testObject = new Object() {

            @AllowedUriSchemes(schemes = { "http", "https" })
            private final URI nullUri = null; // Null is considered valid
        };

        // Validate
        try (var factory = javax.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).isEmpty(); // Null should not produce any violations
        }
    }

    @Test
    public void should_return_false_for_uri_with_no_scheme() throws Exception {
        var testObject = new Object() {

            @AllowedUriSchemes(schemes = { "http", "https" })
            private final URI noSchemeUri = URI.create("example.com"); // No scheme specified
        };

        // Validate
        try (var factory = javax.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Invalid URI scheme: null; allowed schemes are: 'http', 'https'");
        }
    }

    @Test
    public void should_return_false_for_empty_uri() throws Exception {
        var testObject = new Object() {

            @AllowedUriSchemes(schemes = { "http", "https" })
            private final URI emptyUri = URI.create(""); // Empty URI
        };

        // Validate
        try (var factory = javax.validation.Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Invalid URI scheme: null; allowed schemes are: 'http', 'https'");
        }
    }
}
