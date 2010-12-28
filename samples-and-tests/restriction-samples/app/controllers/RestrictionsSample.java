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
