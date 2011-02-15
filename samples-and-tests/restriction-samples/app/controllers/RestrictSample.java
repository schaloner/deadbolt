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
import controllers.deadbolt.Restrict;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
@With(Deadbolt.class)
public class RestrictSample extends Controller
{
    public static void index()
    {
        render();
    }

    @Restrict("foo")
    public static void singleRoleName_authorised()
    {
        render("authorised.html");
    }

    @Restrict({"foo", "bar"})
    public static void twoRoleNames_authorised()
    {
        render("authorised.html");
    }

    @Restrict("oof")
    public static void singleRoleName_unauthorised()
    {
        render("unauthorised.html");
    }

    @Restrict({"foo", "rab"})
    public static void twoRoleNames_unauthorised()
    {
        render("unauthorised.html");
    }
}
