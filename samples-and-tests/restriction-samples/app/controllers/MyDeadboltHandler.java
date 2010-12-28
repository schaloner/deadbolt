package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import deadbolt.MyExternalizedRestrictionsAccessor;
import deadbolt.MyRoleHolder;
import models.deadbolt.RoleHolder;
import play.Logger;
import play.mvc.Controller;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyDeadboltHandler extends Deadbolt.DeadboltHandler
{
    static RoleHolder getRoleHolder()
    {
        return new MyRoleHolder();
    }

    static void onAccessFailure(Class<? extends Controller> controllerClass)
    {
        Logger.error("Hit an authorisation issue when trying to access [{}]", controllerClass);
        forbidden();
    }

    static ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return new MyExternalizedRestrictionsAccessor();
    }
}
