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
import java.io.File;

/**
 * Validator for {@link ExistingFile} annotation on {@link String} objects.
 */
public class ExistingFileValidatorForString implements ConstraintValidator<ExistingFile, String> {
    private ExistingFileValidatorForFile fileValidator;

    @Override
    public void initialize(ExistingFile constraintAnnotation) {
        this.fileValidator = new ExistingFileValidatorForFile();
        this.fileValidator.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String filePath, javax.validation.ConstraintValidatorContext context) {
        if (filePath == null) {
            // Use NotNullValidator for null check
            return true;
        }

        return fileValidator.isValid(new File(filePath), context);
    }
}
