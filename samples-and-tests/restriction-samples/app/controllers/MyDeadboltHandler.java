package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import deadbolt.MyExternalizedRestrictionsAccessor;
import deadbolt.MyRoleHolder;
import models.deadbolt.RoleHolder;
import play.Logger;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyDeadboltHandler extends Deadbolt.DeadboltHandler
{
    static RoleHolder getRoleHolder()
    {
        return new MyRoleHolder();
    }

    static void onAccessFailure(String controllerClassName)
    {
        Logger.error("Hit an authorisation issue when trying to access [{}]", controllerClassName);
        forbidden();
    }

    static ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return new MyExternalizedRestrictionsAccessor();
    }
}
