package controllers.deadbolt;

import models.deadbolt.ExternalizedRestrictions;
import models.deadbolt.NullRoleHolder;
import models.deadbolt.RoleHolder;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public abstract class AbstractDeadboltHandler implements DeadboltHandler
{
    /**
     * {@inheritDoc}
     */
    @Override
    public RoleHolder getRoleHolder()
    {
        return NullRoleHolder.NULL_OBJECT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAccessFailure(String controllerClassName)
    {
        Deadbolt.forbidden();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return null;
    }
}
