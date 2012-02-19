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

import models.deadbolt.AccessResult;

import java.util.List;
import java.util.Map;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public interface RestrictedResourcesHandler
{
    /**
     * Check the access of someone, typically the current user, for the named resource.
     *
     * <ul>
     * <li>If {@link AccessResult#NOT_SPECIFIED} is returned and
     * {@link controllers.deadbolt.RestrictedResource#staticFallback()} is false, access is denied.</li>
     * <li>If {@link AccessResult#NOT_SPECIFIED} is returned and
     * {@link controllers.deadbolt.RestrictedResource#staticFallback()} is true, any further Restrict or
     * Restrictions annotations are processed.  Note that if no Restrict or Restrictions annotations are present,
     * access will be allowed.</li>
     * </ul>
     *
     * @param resourceNames the names of the resource
     * @param resourceParameters additional information on the resource
     * @return {@link AccessResult#ALLOWED} if access is permitted.  {@link AccessResult#DENIED} if access is denied.
     * {@link AccessResult#NOT_SPECIFIED} if access is not specified.
     */
    AccessResult checkAccess(List<String> resourceNames,
                             Map<String, String> resourceParameters);
}
