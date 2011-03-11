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

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;
import deadbolt.MyExternalizedRestrictionsAccessor;
import models.MyRoleHolder;
import models.deadbolt.RoleHolder;
import play.Logger;

/**
 * @author Steve Chaloner (steve@objectify.be).
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
        return new MyExternalizedRestrictionsAccessor();
    }

    public RestrictedResourcesHandler getRestrictedResourcesHandler()
    {
        return new MyRestrictedResourcesHandler();
    }
}