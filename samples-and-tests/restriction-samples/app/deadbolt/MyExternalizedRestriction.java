package deadbolt;

import models.deadbolt.ExternalizedRestriction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyExternalizedRestriction implements ExternalizedRestriction
{
    private final List<String> roleNames = new ArrayList<String>();

    public MyExternalizedRestriction(String... roleNames)
    {
        this.roleNames.addAll(Arrays.asList(roleNames));
    }

    public List<String> getRoleNames()
    {
        return roleNames;
    }
}
