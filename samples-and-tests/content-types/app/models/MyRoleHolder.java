package models;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class MyRoleHolder implements RoleHolder
{
    public List<? extends Role> getRoles()
    {
        return Collections.emptyList();
    }
}
