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
import java.net.URI;
import java.util.UUID;

/**
 * Validator for the {@link UrnUuid} annotation for {@link URI} objects.
 */
public class UrnUuidValidatorForUri implements ConstraintValidator<UrnUuid, URI> {

    @Override
    public boolean isValid(URI uri, ConstraintValidatorContext constraintValidatorContext) {
        if (uri != null) {
            if (!uri.getScheme().equals("urn")) {
                return false;
            }
            if (!uri.getSchemeSpecificPart().startsWith("uuid:")) {
                return false;
            }
            try {
                UUID.fromString(uri.getSchemeSpecificPart().substring("uuid:".length()));
            }
            catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true; // If null is not allowed, this should be checked by the @NotNull annotation
    }
}
