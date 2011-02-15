package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import play.mvc.Controller;
import play.mvc.With;

@With(Deadbolt.class)
public class Application extends Controller
{
    @Restrictions({@Restrict("superadmin"), @Restrict("admin"), @Restrict("operator")})
    public static void index()
    {
        render();
    }
}