package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import deadbolt.MyExternalizedRestrictionsAccessor;
import models.MyRoleHolder;
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

    /**
     * Custom handling of access failure.  Note that further information on the request can be taken from
     * Http.Request.current(), such as actionMethod, etc.
     *
     * @param controllerClassName the name of the controller class.
     */
    static void onAccessFailure(String controllerClassName)
    {
        Logger.error("Hit an authorisation issue when trying to access [%s]", controllerClassName);
        forbidden();
    }

    static ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return new MyExternalizedRestrictionsAccessor();
    }
}
