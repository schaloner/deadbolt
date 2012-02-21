package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.JSON;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.XML;
import play.mvc.Controller;
import play.mvc.With;

public class ContentTypeApplication extends RootController
{
    public static void index()
    {
        render();
    }

    @Restrict("foo")
    @XML
    public static void xmlContent()
    {
        render();
    }

    @Restrict("foo")
    @JSON
    public static void jsonContent()
    {
        render();
    }
}