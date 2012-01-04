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
package controllers;

import controllers.deadbolt.RestrictedResourcesHandler;
import models.deadbolt.AccessResult;

import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class MyRestrictedResourcesHandler implements RestrictedResourcesHandler
{
    /**
     * Various things can be done here, such as checking in a database table that maps the resource name to the
     * current user name or a group the user is in.  There are two rules and one guideline for an easy life here:
     * <ol>
     *     <li>If access is allowed, return {@link models.deadbolt.AccessResult#ALLOWED}</li>
     *     <li>If access is denied, return {@link models.deadbolt.AccessResult#DENIED}</li>
     *     <li>If access is not specified in, e.g. the database you can choose to return {@link models.deadbolt.AccessResult#ALLOWED}
     *     or {@link models.deadbolt.AccessResult#DENIED} if you have a hard policy for this situation; alternatively you can return
     *     {@link models.deadbolt.AccessResult#NOT_SPECIFIED} and allow further processing.</li>
     * </ol>
     *
     * {@inheritDoc}
     */
    public AccessResult checkAccess(List<String> resourceNames)
    {
        // This could be hitting a database to check the resource name against that of the current user, but for pure
        // convenience it's hard-coded here.

        AccessResult result = AccessResult.DENIED;
        if ("resourceA".equals(resourceNames.get(0)))
        {
            result = AccessResult.ALLOWED;
        }
        else if ("resourceB".equals(resourceNames.get(0)))
        {
            result = AccessResult.NOT_SPECIFIED;
        }
        return result;
    }
}
