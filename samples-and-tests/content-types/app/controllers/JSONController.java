package controllers;

import controllers.deadbolt.JSON;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.XML;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@JSON
@Restrict("foo")
public class JSONController extends RootController
{
    public static void jsonControllerResponse()
    {
        render();
    }

    @XML
    public static void xmlResponseFromJSONControllerResponse()
    {
        render();
    }

}
