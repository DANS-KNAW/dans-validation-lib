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
import java.lang.reflect.Field;

public class GreaterThanValidator implements ConstraintValidator<GreaterThan, Object> {
    private String field1;
    private String field2;

    @Override
    public void initialize(GreaterThan constraintAnnotation) {
        field1 = constraintAnnotation.field1();
        field2 = constraintAnnotation.field2();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Field field1Instance = object.getClass().getDeclaredField(field1);
            Field field2Instance = object.getClass().getDeclaredField(field2);
            field1Instance.setAccessible(true);
            field2Instance.setAccessible(true);

            Comparable field1Value = (Comparable) field1Instance.get(object);
            Comparable field2Value = (Comparable) field2Instance.get(object);

            if (field1Value == null || field2Value == null) {
                return false;
            }

            return field1Value.compareTo(field2Value) > 0;
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
