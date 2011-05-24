package controllers;

import controllers.deadbolt.Deadbolt;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@With(Deadbolt.class)
public class AclController extends Controller
{
    @Util
    public static String getCurrentUserName()
    {
        return Secure.Security.connected();
    }
}
