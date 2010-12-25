package models.deadbolt;

import java.util.List;

/**
 * Implementors of this class should
 * @author Steve Chaloner (steve@objectify.be).
 */
public interface RoleHolder
{
    /**
     * A list of {@link Role}s held by the role holder.
     *
     * @return a list of roles
     */
    List<? extends Role> getRoles();
}
