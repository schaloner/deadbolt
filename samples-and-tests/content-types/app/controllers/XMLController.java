package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.JSON;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.XML;
import play.mvc.With;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@With(Deadbolt.class)
@XML
@Restrict("foo")
public class XMLController extends RootController
{
    public static void xmlControllerResponse()
    {
        render();
    }

    @JSON
    public static void jsonResponseFromXMLControllerResponse()
    {
        render();
    }
}
