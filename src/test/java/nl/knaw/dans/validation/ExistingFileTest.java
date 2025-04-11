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
import javax.validation.ValidatorFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExistingFileTest {

    private static class TestRegularFile {
        private TestRegularFile(String filePath) {
            this.filePath = filePath;
            this.file = filePath == null ? null : new File(filePath);
            this.path = filePath == null ? null : Paths.get(filePath);
        }

        @ExistingFile
        private String filePath;
        @ExistingFile
        private File file;
        @ExistingFile
        private Path path;
    }

    private static class TestDirectory {
        private TestDirectory(String filePath) {
            this.filePath = filePath;
            this.file = filePath == null ? null : new File(filePath);
            this.path = filePath == null ? null : Paths.get(filePath);
        }

        @ExistingFile(isDirectory = true)
        private String filePath;
        @ExistingFile(isDirectory = true)
        private File file;
        @ExistingFile(isDirectory = true)
        private Path path;
    }

    @Test
    public void should_return_true_when_file_exists() {
        // Given
        TestRegularFile testObject = new TestRegularFile("src/test/resources/ExistingFileTest/afile.txt");

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void should_return_false_when_file_does_not_exist() {
        // Given
        TestRegularFile testObject = new TestRegularFile("src/test/resources/ExistingFileTest/afileXXX.txt");

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).hasSize(3);
            assertTrue(violations.iterator().next().getMessage().contains("does not exist"));
        }
    }

    @Test
    public void should_return_true_when_file_path_is_null() {
        // Given
        TestRegularFile testObject = new TestRegularFile(null);

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void should_return_false_when_file_is_a_directory() {
        // Given
        TestRegularFile testObject = new TestRegularFile("src/test/resources/ExistingFileTest/adir");

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).hasSize(3);
            assertThat(violations).allMatch(v -> v.getMessage().contains("is not a regular file"));
        }
    }

    @Test
    public void should_return_true_when_file_is_directory_and_directory_is_required() {
        // Given
        TestDirectory testObject = new TestDirectory("src/test/resources/ExistingFileTest/adir");

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Test
    public void should_return_true_when_file_is_regular_file_but_directory_is_required() {
        // Given
        TestDirectory testObject = new TestDirectory("src/test/resources/ExistingFileTest/afile.txt");

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).hasSize(3);
            assertThat(violations).allMatch(v -> v.getMessage().contains("is not a directory"));
        }
    }

    @Test
    public void should_work_with_absolute_path() {
        // Given
        TestRegularFile testObject = new TestRegularFile(new File("src/test/resources/ExistingFileTest/afile.txt").getAbsolutePath());

        // When
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            var validator = factory.getValidator();
            var violations = validator.validate(testObject);
            // Then
            assertThat(violations).isEmpty();
        }
    }

}
