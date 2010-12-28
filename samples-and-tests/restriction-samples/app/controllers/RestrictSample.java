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
