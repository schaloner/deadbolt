/*
 * Copyright 2010-2011 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers.deadbolt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface RestrictedResource
{
    /**
     * The name of the resource.
     *
     * @return the name of the resource
     */
    String[] name();

    /**
     * Indicates if further security checking should be done using the static role checking in cases
     * where a restricted resource check gives a {@link models.deadbolt.AccessResult#NOT_SPECIFIED} result.
     *
     * @return true if Deadbolt should apply @Restrict and @Restrictions checks
     */
    boolean staticFallback() default false;
}
