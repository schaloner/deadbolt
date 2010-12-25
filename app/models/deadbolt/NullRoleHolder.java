package models.deadbolt;

import java.util.Collections;
import java.util.List;

/**
 * Null-object implementation of {@link RoleHolder}.
 *
 * @author Steve Chaloner (steve@objectify.be).
 */
public class NullRoleHolder implements RoleHolder
{
    public static final RoleHolder NULL_OBJECT = new NullRoleHolder();

    /**
     * {@inheritDoc}
     */
    public List<Role> getRoles()
    {
        return Collections.emptyList();
    }
}
