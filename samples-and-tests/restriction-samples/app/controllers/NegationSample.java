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
import controllers.deadbolt.Restrictions;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
@With(Deadbolt.class)
public class NegationSample extends Controller
{
    public static void index()
    {
        render();
    }

    @Restrict("!oof")
    public static void singleNegatedRoleOnRoleNotHeld_authorised()
    {
        render("authorised.html");
    }

    @Restrictions({@Restrict("foo"), @Restrict("!bar")})
    public static void twoRestrictsWithOneRoleValidAndOtherNegated_authorised()
    {
        render("authorised.html");
    }

    @Restrict("!foo")
    public static void singleNegatedRoleOnHeldRole_unauthorised()
    {
        render("unauthorised.html");
    }

    @Restrictions({@Restrict("!foo"), @Restrict("!bar")})
    public static void twoRestrictsWithBothHeldRolesNegated_unauthorised()
    {
        render("unauthorised.html");
    }
}
