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
import java.net.URI;

public class AllowedUriSchemesValidator implements ConstraintValidator<AllowedUriSchemes, URI> {

    private String[] allowedSchemes;

    @Override
    public void initialize(AllowedUriSchemes constraintAnnotation) {
        this.allowedSchemes = constraintAnnotation.schemes();
    }

    @Override
    public boolean isValid(URI uri, javax.validation.ConstraintValidatorContext context) {
        if (uri == null) {
            return true; // null is considered valid, if you want to enforce non-null, use @NotNull
        }
        String scheme = uri.getScheme();
        if (scheme != null && !scheme.trim().isEmpty()) { // second member seems superfluous, but keep it for safety
            for (String allowedScheme : allowedSchemes) {
                if (allowedScheme.equalsIgnoreCase(scheme)) {
                    return true; // valid scheme found
                }
            }
        }
        context.disableDefaultConstraintViolation();
        if (scheme != null) {
            scheme = "'" + scheme + "'";
        }
        context.buildConstraintViolationWithTemplate(
                "Invalid URI scheme: " + scheme + "; allowed schemes are: " +
                    java.util.Arrays.stream(allowedSchemes)
                        .map(s -> "'" + s + "'")
                        .collect(java.util.stream.Collectors.joining(", ")))
            .addConstraintViolation();
        return false; // no valid scheme found
    }
}
