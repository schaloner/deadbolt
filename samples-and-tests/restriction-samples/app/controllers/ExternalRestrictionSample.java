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
