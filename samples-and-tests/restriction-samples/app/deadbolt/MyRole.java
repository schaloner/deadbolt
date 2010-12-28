package deadbolt;

import models.deadbolt.Role;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyRole implements Role
{
    private String name;

    public MyRole(String name)
    {
        this.name = name;
    }

    public String getRoleName()
    {
        return name;
    }
}
