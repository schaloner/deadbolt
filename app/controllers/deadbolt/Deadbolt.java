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
package controllers.deadbolt;

import models.deadbolt.AccessResult;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Checks authorisation based on role names.
 *
 * @author Steve Chaloner (steve@objectify.be).
 */
public class Deadbolt extends Controller
{
    private enum RestrictionType { NONE, DYNAMIC, STATIC, BASIC }

    public static final String DEADBOLT_HANDLER_KEY = "deadbolt.handler";

    public static final String CACHE_USER_KEY = "deadbolt.cache-user-per-request";

    public static final String CACHE_PER_REQUEST = "deadbolt.cache-user";

    public static final String DEFAULT_RESPONSE_FORMAT = "deadbolt.default-response-format";

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

    private static RoleHolder getRoleHolder()
    {
        RoleHolder roleHolder = (RoleHolder)request.args.get(CACHE_PER_REQUEST);
        if (roleHolder == null)
        {
            roleHolder = DEADBOLT_HANDLER.getRoleHolder();
            if (Boolean.valueOf(Play.configuration.getProperty(CACHE_USER_KEY, "false")))
            {
                request.args.put(CACHE_PER_REQUEST,
                                 roleHolder);
            }
        }

        return roleHolder;
    }

    /**
     * Checks access to a class or method based on any {@link controllers.deadbolt.Restrict} or
     * {@link controllers.deadbolt.Restrictions} annotations.
     */
    @Before
    static void checkRestrictions() throws Throwable
    {
        DEADBOLT_HANDLER.beforeRoleCheck();

        RoleHolder roleHolder = getRoleHolder();
        RestrictionType restrictionType = getRestrictionType();
        if (restrictionType == RestrictionType.DYNAMIC)
        {
            handleDynamicChecks(roleHolder);
        }
        else if (restrictionType == RestrictionType.STATIC)
        {
            handleStaticChecks(roleHolder);
        }
        else if (restrictionType == RestrictionType.BASIC)
        {
            handleRoleHolderPresent(roleHolder);
        }
    }

    @Util
    static void handleDynamicChecks(RoleHolder roleHolder) throws Throwable
    {
        handleRestrictedResources(roleHolder);
    }

    @Util
    static void handleRoleHolderPresent(RoleHolder roleHolder) throws Throwable
    {
        if (roleHolder == null)
        {
            RoleHolderPresent roleHolderPresent = getActionAnnotation(RoleHolderPresent.class);
            if (roleHolderPresent == null)
            {
                roleHolderPresent = getControllerInheritedAnnotation(RoleHolderPresent.class);
            }

            if (roleHolderPresent != null)
            {
                accessFailed();
            }
        }
    }

    @Util
    static void handleStaticChecks(RoleHolder roleHolder) throws Throwable
    {
        handleRestrict(roleHolder);
        handleRestrictions(roleHolder);
        handleExternalRestrictions(roleHolder);
    }

    @Util
    static void handleRestrictedResources(RoleHolder roleHolder) throws Throwable
    {
        Unrestricted actionUnrestricted = getActionAnnotation(Unrestricted.class);
        if (actionUnrestricted == null)
        {
            RestrictedResource restrictedResource = getActionAnnotation(RestrictedResource.class);
            if (restrictedResource == null)
            {
                actionUnrestricted = getControllerAnnotation(Unrestricted.class);
                if (actionUnrestricted == null)
                {
                    restrictedResource = getControllerInheritedAnnotation(RestrictedResource.class);
                }
            }

            if (restrictedResource != null)
            {
                RestrictedResourcesHandler restrictedResourcesHandler = DEADBOLT_HANDLER.getRestrictedResourcesHandler();

                if (restrictedResourcesHandler == null)
                {
                    Logger.fatal("A RestrictedResource is specified but no RestrictedResourcesHandler is available.  Denying access to resource.");
                }
                else
                {
                    String[] names = restrictedResource.name();
                    AccessResult accessResult = restrictedResourcesHandler.checkAccess(names == null ? Collections.<String>emptyList() : Arrays.asList(names),
                                                                                       Collections.<String, String>emptyMap());
                    switch (accessResult)
                    {
                        case DENIED:
                            accessFailed();
                            break;
                        case NOT_SPECIFIED:
                            if (restrictedResource.staticFallback())
                            {
                                Logger.info("Access for [%s] not defined for current user - processing further with other Deadbolt annotations",
                                            (Object[])names);
                                handleStaticChecks(roleHolder);
                            }
                            else
                            {
                                accessFailed();
                            }
                            break;
                        default:
                            Logger.debug("RestrictedResource - access allowed for [%s]",
                                         (Object[])names);
                    }
                }
            }
        }
    }

    @Util
    static void handleExternalRestrictions(RoleHolder roleHolder) throws Throwable
    {
        Unrestricted actionUnrestricted = getActionAnnotation(Unrestricted.class);
        if (actionUnrestricted == null)
        {
            ExternalRestrictions externalRestrictions = getActionAnnotation(ExternalRestrictions.class);
            if (externalRestrictions == null)
            {
                actionUnrestricted = getControllerAnnotation(Unrestricted.class);
                if (actionUnrestricted == null)
                {
                    externalRestrictions = getControllerInheritedAnnotation(ExternalRestrictions.class);
                }
            }

            if (externalRestrictions != null)
            {
                ExternalizedRestrictionsAccessor externalisedRestrictionsAccessor =
                        DEADBOLT_HANDLER.getExternalizedRestrictionsAccessor();

                boolean roleOk = false;
                if (externalisedRestrictionsAccessor == null)
                {
                    Logger.fatal("@ExternalRestrictions are specified but no ExternalizedRestrictionsAccessor is available.  Denying access to resource.");
                }
                else
                {
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
                }
                if (!roleOk)
                {
                    accessFailed();
                }
            }
        }
    }

    @Util
    static void handleRestrictions(RoleHolder roleHolder) throws Throwable
    {
        Unrestricted actionUnrestricted = getActionAnnotation(Unrestricted.class);
        if (actionUnrestricted == null)
        {
            Restrictions restrictions = getActionAnnotation(Restrictions.class);
            if (restrictions == null)
            {
                actionUnrestricted = getControllerAnnotation(Unrestricted.class);
                if (actionUnrestricted == null)
                {
                    restrictions = getControllerInheritedAnnotation(Restrictions.class);
                }
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
    }

    @Util
    static void handleRestrict(RoleHolder roleHolder) throws Throwable
    {
        Unrestricted actionUnrestricted = getActionAnnotation(Unrestricted.class);
        if (actionUnrestricted == null)
        {
            Restrict restrict = getActionAnnotation(Restrict.class);
            if (restrict == null)
            {
                actionUnrestricted = getControllerAnnotation(Unrestricted.class);
                if (actionUnrestricted == null)
                {
                    restrict = getControllerInheritedAnnotation(Restrict.class);
                }
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

        String responseFormat = null;
        if (getActionAnnotation(JSON.class) != null)
        {
            responseFormat = "json";
        }
        else if (getActionAnnotation(XML.class) != null)
        {
            responseFormat = "xml";
        }
        else if (getControllerAnnotation(JSON.class) != null)
        {
            responseFormat = "json";
        }
        else if (getControllerAnnotation(XML.class) != null)
        {
            responseFormat = "xml";
        }
        else
        {
            String defaultResponseFormat = Play.configuration.getProperty(DEFAULT_RESPONSE_FORMAT);
            if (!isEmpty(defaultResponseFormat))
            {
                responseFormat = defaultResponseFormat;
            }
        }

        if (!isEmpty(responseFormat))
        {
            request.format = responseFormat;
        }

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
        if (roleHolder != null)
        {
            List<? extends Role> roles = roleHolder.getRoles();

            if (roles != null)
            {
                List<String> heldRoles = new ArrayList<String>();
                for (Role role : roles)
                {
                    if (role != null)
                    {
                        heldRoles.add(role.getRoleName());
                    }
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
        }
        return hasRole;
    }

    public static boolean isRoleHolderPresent()
    {
        return getRoleHolder() != null;
    }

    private static RestrictionType getRestrictionType()
    {
        RestrictionType restrictionType = RestrictionType.NONE;

        if (getActionAnnotation(Restrict.class) != null
            || getActionAnnotation(Restrictions.class) != null
            || getActionAnnotation(ExternalRestrictions.class) != null)
        {
            restrictionType = RestrictionType.STATIC;
        }
        else if (getActionAnnotation(RestrictedResource.class) != null)
        {
            restrictionType = RestrictionType.DYNAMIC;
        }
        else if (getActionAnnotation(RoleHolderPresent.class) != null)
        {
            restrictionType = RestrictionType.BASIC;
        }

        if (restrictionType == RestrictionType.NONE
                && getControllerAnnotation(Unrestricted.class) == null)
        {
            if (getControllerInheritedAnnotation(Restrict.class) != null
                || getControllerInheritedAnnotation(Restrictions.class) != null
                || getControllerInheritedAnnotation(ExternalRestrictions.class) != null)
            {
                restrictionType = RestrictionType.STATIC;
            }
            else if (getControllerInheritedAnnotation(RestrictedResource.class) != null)
            {
                restrictionType = RestrictionType.DYNAMIC;
            }
            else if (getControllerInheritedAnnotation(RoleHolderPresent.class) != null)
            {
                restrictionType = RestrictionType.BASIC;
            }
        }

        return restrictionType;
    }

    /**
     * Checks if the current user has the specified role.
     *
     * @param roleNames the names of the required roles
     * @return true iff the current user has the specified role
     */
    public static boolean hasRoles(List<String> roleNames) throws Throwable
    {
        DEADBOLT_HANDLER.beforeRoleCheck();

        RoleHolder roleHolder = getRoleHolder();

        return roleHolder != null &&
               roleHolder.getRoles() != null &&
               hasAllRoles(roleHolder,
                           roleNames.toArray(new String[roleNames.size()]));
    }

    public static boolean checkRestrictedResource(List<String> resourceKeys,
                                                  Map<String, String> resourceParameters,
                                                  Boolean allowUnspecified)
    {
        DEADBOLT_HANDLER.beforeRoleCheck();

        RestrictedResourcesHandler restrictedResourcesHandler = DEADBOLT_HANDLER.getRestrictedResourcesHandler();
        boolean accessedAllowed = false;

        if (restrictedResourcesHandler == null)
        {
            Logger.fatal("A RestrictedResource is specified but no RestrictedResourcesHandler is available.  Denying access to resource.");
        }
        else
        {
            AccessResult accessResult = restrictedResourcesHandler.checkAccess(resourceKeys,
                                                                               resourceParameters);
            switch (accessResult)
            {
                case ALLOWED:
                    accessedAllowed = true;
                    break;
                case NOT_SPECIFIED:
                    allowUnspecified = allowUnspecified != null && allowUnspecified;
                    Logger.info("Access for [%s] not defined for current user - specified behaviour is [%s]",
                                resourceKeys,
                                allowUnspecified ? "allow" : "deny");
                    if (allowUnspecified)
                    {
                        accessedAllowed = true;
                    }
                    break;
                default:
                    Logger.debug("RestrictedResource - access allowed for [%s]",
                                 resourceKeys);
            }
        }

        return accessedAllowed;
    }

    public static boolean checkExternalizedRestriction(List<String> externalRestrictions)
    {
        DEADBOLT_HANDLER.beforeRoleCheck();

        boolean roleOk = false;
        if (externalRestrictions != null)
        {
            ExternalizedRestrictionsAccessor externalisedRestrictionsAccessor =
                    DEADBOLT_HANDLER.getExternalizedRestrictionsAccessor();
            RoleHolder roleHolder = getRoleHolder();

            if (externalisedRestrictionsAccessor == null)
            {
                Logger.fatal("@ExternalRestrictions are specified but no ExternalizedRestrictionsAccessor is available.  Denying access to resource.");
            }
            else
            {
                for (String externalRestrictionTreeName : externalRestrictions)
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
            }
        }
        return roleOk;
    }

    @Util
    public static void forbidden()
    {
        Controller.forbidden();
    }

    private static boolean isEmpty(String s)
    {
        return s == null || s.trim().length() == 0;
    }}
