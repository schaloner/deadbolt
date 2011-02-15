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
public class RestrictionsSample extends Controller
{
    public static void index()
    {
        render();
    }

    @Restrictions(@Restrict("foo"))
    public static void singleRestrictWithSingleRole_authorised()
    {
        render("authorised.html");
    }

    @Restrictions(@Restrict({"foo", "bar"}))
    public static void singleRestrictWithTwoRoles_authorised()
    {
        render("authorised.html");
    }

    @Restrictions({@Restrict({"foo"}), @Restrict({"rab"})})
    public static void twoRestrictsWithOnlyOneValid_authorised()
    {
        render("authorised.html");
    }

    @Restrictions(@Restrict({"rab"}))
    public static void singleRestrictWithSingleRole_unauthorised()
    {
        render("unauthorised.html");
    }

    @Restrictions(@Restrict({"oof", "rab"}))
    public static void singleRestrictWithTwoRoles_unauthorised()
    {
        render("unauthorised.html");
    }

    @Restrictions({@Restrict({"oof"}), @Restrict({"rab"})})
    public static void twoRestrictsWithOnlyNeitherValid_unauthorised()
    {
        render("unauthorised.html");
    }
}
