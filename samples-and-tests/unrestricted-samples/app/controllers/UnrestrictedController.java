package controllers;

import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import controllers.deadbolt.Unrestricted;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Unrestricted
public class UnrestrictedController extends RestrictedController
{
    public static void unannotatedMethod()
    {
        render("authorised.html");
    }

    @Restrictions({@Restrict("hurdy"),
                   @Restrict("gurdy")})
    public static void restrictedMethod()
    {
        render("unauthorised.html");
    }
}
