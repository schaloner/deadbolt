/*
 * Copyright 2010-2011 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;
import model.AclUser;
import models.deadbolt.ExternalizedRestrictions;
import models.deadbolt.RoleHolder;
import play.mvc.Controller;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class AclDeadboltHandler extends Controller implements DeadboltHandler
{
    private static final RestrictedResourcesHandler RESTRICTED_RESOURCES_HANDLER = new AclRestrictedResourcesHandler();

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
        return AclUser.getByUserName(userName);
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

    public RestrictedResourcesHandler getRestrictedResourcesHandler()
    {
        return RESTRICTED_RESOURCES_HANDLER;
    }
}
