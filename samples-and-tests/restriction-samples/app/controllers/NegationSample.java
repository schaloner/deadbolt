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
