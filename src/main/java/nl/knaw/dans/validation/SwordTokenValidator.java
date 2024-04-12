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

public class SwordTokenValidator implements ConstraintValidator<SwordToken, String> {
    private static final String PREFIX = "sword:";

    @Override
    public boolean isValid(String value, javax.validation.ConstraintValidatorContext context) {
        return value == null || isValidSwordToken(value, context);
    }

    private boolean isValidSwordToken(String value, javax.validation.ConstraintValidatorContext context) {
        if (value.startsWith(PREFIX) && new UuidValidator().isValid(value.substring(PREFIX.length()), context)) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(getErrorMessage(value))
            .addConstraintViolation();
        return false;
    }

    private String getErrorMessage(String value) {
        return value.startsWith(PREFIX) ? "SWORD token must contain a valid UUID after the 'sword:' prefix" : "SWORD token must start with 'sword:' prefix";
    }
}