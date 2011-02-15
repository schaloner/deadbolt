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

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalRestrictions;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
@With(Deadbolt.class)
public class ExternalRestrictionSample extends Controller
{
    public static void index()
    {
        render();
    }

    @ExternalRestrictions("standard")
    public static void standard_authorised()
    {
        render("authorised.html");
    }

    @ExternalRestrictions("one-role-missing")
    public static void oneRoleMissing_authorised()
    {
        render("authorised.html");
    }

    @ExternalRestrictions("admin")
    public static void admin_unauthorised()
    {
        render("unauthorised.html");
    }

    @ExternalRestrictions("exclude-restricted")
    public static void excludeRestricted_unauthorised()
    {
        render("unauthorised.html");
    }
}
