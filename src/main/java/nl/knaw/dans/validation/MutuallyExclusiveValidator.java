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

public class MutuallyExclusiveValidator implements ConstraintValidator<MutuallyExclusive, Object> {
    private String[] fields;

    @Override
    public void initialize(MutuallyExclusive constraintAnnotation) {
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        int count = 0;
        for (String field : fields) {
            try {
                var fieldInstance = object.getClass().getDeclaredField(field);
                fieldInstance.setAccessible(true);
                if (fieldInstance.get(object) != null) {
                    count++;
                }
            }
            catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalStateException("Programming error: field " + field + " does not exist or is not accessible");
            }
        }
        return count <= 1;
    }
}
