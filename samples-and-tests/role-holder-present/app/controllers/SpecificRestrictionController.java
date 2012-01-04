package controllers;

import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import controllers.deadbolt.Unrestricted;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class SpecificRestrictionController extends RoleHolderPresentController
{
    @Restrict("foo")
    public static void foo()
    {
        render("authorised.html");
    }

    @Restrictions({@Restrict("hurdy"),
                   @Restrict("gurdy")})
    public static void bar()
    {
        render("unauthorised.html");
    }
}
