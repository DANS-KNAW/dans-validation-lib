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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;

/**
 * Validator for {@link ExistingFile} annotation for {@link File} objects.
 */
public class ExistingFileValidatorForFile implements ConstraintValidator<ExistingFile, File> {
    private boolean isDirectory;

    @Override
    public void initialize(ExistingFile constraintAnnotation) {
        this.isDirectory = constraintAnnotation.isDirectory();
    }

    @Override
    public boolean isValid(File file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null) {
            // Use NotNullValidator for null check
            return true;
        }

        if (file.exists()) {
            boolean valid = isDirectory == file.isDirectory();
            if (!valid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                    isDirectory ? "File is not a directory" : "File is not a regular file"
                ).addConstraintViolation();
            }
            return valid;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("File does not exist").addConstraintViolation();
        return false;
    }
}
