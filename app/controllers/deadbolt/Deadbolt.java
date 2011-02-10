/*
 * Copyright 2009-2010 Steve Chaloner
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
package controllers.deadbolt;

import models.deadbolt.ExternalizedRestriction;
import models.deadbolt.ExternalizedRestrictions;
import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.Logger;
import play.Play;
import play.exceptions.ConfigurationException;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks authorisation based on role names.
 *
 * @author Steve Chaloner (steve@objectify.be).
 */
public class Deadbolt extends Controller
{
    public static final String DEADBOLT_HANDLER_KEY = "deadbolt.handler";

    private static DeadboltHandler DEADBOLT_HANDLER;

    static
    {
        String handlerName = Play.configuration.getProperty(DEADBOLT_HANDLER_KEY);
        if (handlerName == null)
        {
            throw new ConfigurationException("deadbolt.handler must be defined");
        }

        try
        {
            Class<DeadboltHandler> clazz = (Class<DeadboltHandler>)Class.forName(handlerName);
            DEADBOLT_HANDLER = clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new ConfigurationException(String.format("Unable to create DeadboltHandler instance: [%s]",
                                                           e.getMessage()));
        }
    }

    /**
     * Checks access to a class or method based on any {@link controllers.deadbolt.Restrict} or
     * {@link controllers.deadbolt.Restrictions} annotations.
     */
    @Before
    static void checkRestrictions() throws Throwable
    {
        RoleHolder roleHolder = DEADBOLT_HANDLER.getRoleHolder();

        handleRestrict(roleHolder);
        handleRestrictions(roleHolder);
        handleExternalRestrictions(roleHolder);
    }

    @Util
    static void handleExternalRestrictions(RoleHolder roleHolder) throws Throwable
    {
        ExternalRestrictions externalRestrictions = getActionAnnotation(ExternalRestrictions.class);
        if (externalRestrictions == null)
        {
            externalRestrictions = getControllerInheritedAnnotation(ExternalRestrictions.class);
        }

        if (externalRestrictions != null)
        {
            ExternalizedRestrictionsAccessor externalisedRestrictionsAccessor =
                    DEADBOLT_HANDLER.getExternalizedRestrictionsAccessor();
            boolean roleOk = false;

            for (String externalRestrictionTreeName : externalRestrictions.value())
            {
                ExternalizedRestrictions externalizedRestrictions =
                        externalisedRestrictionsAccessor.getExternalizedRestrictions(externalRestrictionTreeName);
                if (externalizedRestrictions != null)
                {
                    List<ExternalizedRestriction> restrictions = externalizedRestrictions.getExternalisedRestrictions();
                    for (ExternalizedRestriction restriction : restrictions)
                    {
                        List<String> roleNames = restriction.getRoleNames();
                        roleOk |= checkRole(roleHolder,
                                            roleNames.toArray(new String[roleNames.size()]));
                    }
                }
            }

            if (!roleOk)
            {
                accessFailed();
            }
        }
    }

    @Util
    static void handleRestrictions(RoleHolder roleHolder) throws Throwable
    {
        Restrictions restrictions = getActionAnnotation(Restrictions.class);
        if (restrictions == null)
        {
            restrictions = getControllerInheritedAnnotation(Restrictions.class);
        }

        if (restrictions != null)
        {
            Restrict[] restrictArray = restrictions.value();
            boolean roleOk = false;
            for (int i = 0; !roleOk && i < restrictArray.length; i++)
            {
                roleOk |= checkRole(roleHolder,
                                    restrictArray[i].value());
            }
            if (!roleOk)
            {
                accessFailed();
            }
        }
    }

    @Util
    static void handleRestrict(RoleHolder roleHolder) throws Throwable
    {
        Restrict restrict = getActionAnnotation(Restrict.class);
        if (restrict == null)
        {
            restrict = getControllerInheritedAnnotation(Restrict.class);
        }

        if (restrict != null)
        {
            if (!checkRole(roleHolder,
                           restrict.value()))
            {
                accessFailed();
            }
        }
    }

    @Util
    static boolean checkRole(RoleHolder roleHolder,
                             String[] roleNames)
    {
        boolean roleOk = true;
        if (!hasAllRoles(roleHolder,
                         roleNames))
        {
            roleOk = false;
        }
        return roleOk;
    }

    /**
     * Generic access failure forwarding point.
     */
    static void accessFailed() throws Throwable
    {
        String controllerClassName = getControllerClass().getName();
        Logger.debug("Deadbolt: Access failure on [%s]",
                     controllerClassName);

        DEADBOLT_HANDLER.onAccessFailure(controllerClassName);

    }

    /**
     * Checks if the current user has all of the specified roles.
     *
     * @param roleHolder the object requiring authorisation
     * @param roleNames the role names
     * @return true iff the current user has all of the specified roles
     */
    public static boolean hasAllRoles(RoleHolder roleHolder,
                                      String[] roleNames)
    {
        boolean hasRole = false;
        List<? extends Role> roles = roleHolder.getRoles();

        if (roles != null)
        {
            List<String> heldRoles = new ArrayList<String>();
            for (Role role : roles)
            {
                heldRoles.add(role.getRoleName());
            }

            boolean roleCheckResult = true;
            for (int i = 0; roleCheckResult && i < roleNames.length; i++)
            {
                boolean invert = false;
                String roleName = roleNames[i];
                if (roleName.startsWith("!"))
                {
                    invert = true;
                    roleName = roleName.substring(1);
                }
                roleCheckResult = heldRoles.contains(roleName);

                if (invert)
                {
                    roleCheckResult = !roleCheckResult;
                }
            }
            hasRole = roleCheckResult;
        }
        return hasRole;
    }

    /**
     * Checks if the current user has the specified role.
     *
     * @param roleNames the names of the required roles
     * @return true iff the current user has the specified role
     */
    public static boolean hasRoles(List<String> roleNames) throws Throwable
    {
        RoleHolder roleHolder = DEADBOLT_HANDLER.getRoleHolder();

        return roleHolder != null &&
               roleHolder.getRoles() != null &&
               hasAllRoles(roleHolder,
                           roleNames.toArray(new String[roleNames.size()]));
    }

    public static void forbidden()
    {
        Controller.forbidden();
    }
}
