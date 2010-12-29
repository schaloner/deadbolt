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
import models.deadbolt.NullRoleHolder;
import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Util;
import play.utils.Java;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks authorisation based on role names.
 *
 * @author Steve Chaloner (steve@objectify.be).
 */
public class Deadbolt extends Controller
{
    public static final String GET_ROLE_HOLDER = "getRoleHolder";

    public static final String ON_ACCESS_FAILURE = "onAccessFailure";

    public static final String EXTERNALISED_RESTRICTIONS_ACCESSOR = "getExternalizedRestrictionsAccessor";

    /**
     * Checks access to a class or method based on any {@link controllers.deadbolt.Restrict} or
     * {@link controllers.deadbolt.Restrictions} annotations.
     */
    @Before
    static void checkRestrictions() throws Throwable
    {
        RoleHolder roleHolder = (RoleHolder) DeadboltHandler.invoke(GET_ROLE_HOLDER);

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
                    (ExternalizedRestrictionsAccessor) DeadboltHandler.invoke(EXTERNALISED_RESTRICTIONS_ACCESSOR);
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

        DeadboltHandler.invoke(ON_ACCESS_FAILURE,
                               controllerClassName);

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
        RoleHolder roleHolder = (RoleHolder) DeadboltHandler.invoke(GET_ROLE_HOLDER);

        return roleHolder != null &&
               roleHolder.getRoles() != null &&
               hasAllRoles(roleHolder,
                           roleNames.toArray(new String[roleNames.size()]));
    }

    /**
     * This class provides hooks via the getRoleHolder and onAccessFailure methods.  Create a new class that extends from
     * this one and override these methods in order to get the whole thing to work.
     */
    public static class DeadboltHandler extends Controller
    {
        private static final ExternalizedRestrictionsAccessor NULL_ERA = new ExternalizedRestrictionsAccessor()
        {
            public ExternalizedRestrictions getExternalizedRestrictions(String name)
            {
                return null;
            }
        };

        static RoleHolder getRoleHolder()
        {
            return NullRoleHolder.NULL_OBJECT;
        }

        static void onAccessFailure(Class<? extends Controller> controllerClass)
        {
            forbidden();
        }

        static ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
        {
            return NULL_ERA;
        }

        private static Object invoke(String m, Object... args) throws Throwable
        {
            Class roleAccessor;
            List<Class> classes = Play.classloader.getAssignableClasses(DeadboltHandler.class);
            if (classes.size() == 0)
            {
                roleAccessor = DeadboltHandler.class;
            }
            else
            {
                roleAccessor = classes.get(0);
            }

            try
            {
                return Java.invokeStaticOrParent(roleAccessor,
                                                 m,
                                                 args);
            }
            catch (InvocationTargetException e)
            {
                throw e.getTargetException();
            }
        }
    }
}
