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

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validator for the {@link UniqueAttribute} annotation.
 */
@Slf4j
public class UniqueAttributeValidator implements ConstraintValidator<UniqueAttribute, Object> {
    private String attribute;

    @Override
    public void initialize(UniqueAttribute constraintAnnotation) {
        this.attribute = constraintAnnotation.attribute();
        log.debug("Initialized UniqueAttributeValidator with attribute {}", attribute);
    }

    @Override
    public boolean isValid(Object objects, ConstraintValidatorContext constraintValidatorContext) {
        if (objects == null) {
            log.debug("List of objects is null; returning true");
            return true;
        }

        try {
            List<Object> values = new ArrayList<>();

            if (objects instanceof Iterable<?> iterable) {
                for (Object object : iterable) {
                    if (object == null) {
                        log.debug("Object in list is null; ignoring it");
                        continue;
                    }

                    // Get attribute value
                    Field field = object.getClass().getDeclaredField(attribute);
                    field.setAccessible(true);

                    var value = field.get(object);
                    if (value == null) {
                        log.debug("Attribute {} is null; ignoring it", attribute);
                        continue;
                    }

                    values.add(field.get(object));
                }
            }

            // Return false if there are duplicates in values
            return values.stream().collect(
                Collectors.groupingBy(e -> e)).entrySet().stream().noneMatch(e -> e.getValue().size() > 1);
        }
        catch (NoSuchFieldException e) {
            throw new ValidationException("Field " + attribute + " does not exist on all objects", e);
        }
        catch (IllegalAccessException e) {
            throw new ValidationException("Could not access field " + attribute, e);
        }
    }
}
