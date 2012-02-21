package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;
import models.MyRoleHolder;
import models.deadbolt.RoleHolder;
import play.Logger;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class MyDeadboltHandler implements DeadboltHandler
{
    public void beforeRoleCheck()
    {
        // Ensure the current user is logged in, and redirect accordingly
    }

    public RoleHolder getRoleHolder()
    {
        return new MyRoleHolder();
    }

    /**
     * Custom handling of access failure.  Note that further information on the request can be taken from
     * Http.Request.current(), such as actionMethod, etc.
     *
     * @param controllerClassName the name of the controller class.
     */
    public void onAccessFailure(String controllerClassName)
    {
        Logger.error("Hit an authorisation issue when trying to access [%s]", controllerClassName);
        Deadbolt.forbidden();
    }

    public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return null;
    }

    public RestrictedResourcesHandler getRestrictedResourcesHandler()
    {
        return null;
    }
}