package controllers;

import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import models.User;
import models.deadbolt.ExternalizedRestrictions;
import models.deadbolt.RoleHolder;
import play.mvc.Controller;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class MyDeadboltHandler extends Controller implements DeadboltHandler
{
    public void beforeRoleCheck()
    {
        // Note that if you provide your own implementation of Secure's Security class you would refer to that instead
        if (!Secure.Security.isConnected())
        {
            try
            {
                if (!session.contains("username"))
                {
                    flash.put("url", "GET".equals(request.method) ? request.url : "/");
                    Secure.login();
                }
            }
            catch (Throwable t)
            {
                // handle this in an app-specific way
            }
        }
    }

    public RoleHolder getRoleHolder()
    {
        String userName = Secure.Security.connected();
        return User.getByUserName(userName);
    }

    public void onAccessFailure(String controllerClassName)
    {
       forbidden();
    }

    public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return new ExternalizedRestrictionsAccessor()
        {
            public ExternalizedRestrictions getExternalizedRestrictions(String name)
            {
                return null;
            }
        };
    }
}
