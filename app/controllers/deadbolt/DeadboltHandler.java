package controllers.deadbolt;

import models.deadbolt.RoleHolder;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public interface DeadboltHandler
{
    /**
     * Invoked immediately before controller or view restrictions are checked.  This forms the integration with any
     * authentication actions that may need to occur.
     */
    void beforeRoleCheck();

    /**
     * Gets the current {@link RoleHolder}, e.g. the current user.
     *
     * @return the current role holder
     */
    RoleHolder getRoleHolder();

    /**
     * Invoked when an access failure is detected on <i>controllerClassName</i>.
     *
     * @param controllerClassName the name of the controller access was denied to
     */
    void onAccessFailure(String controllerClassName);

    /**
     * Gets the accessor used to determine restrictions from an external source.
     * @return
     */
    ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor();
}
