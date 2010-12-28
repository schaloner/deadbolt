package deadbolt;

import models.deadbolt.ExternalizedRestriction;
import models.deadbolt.ExternalizedRestrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyExternalizedRestrictions implements ExternalizedRestrictions
{
    private final List<ExternalizedRestriction> restrictions = new ArrayList<ExternalizedRestriction>();

    public MyExternalizedRestrictions(ExternalizedRestriction... restrictions)
    {
        this.restrictions.addAll(Arrays.asList(restrictions));
    }

    public List<ExternalizedRestriction> getExternalisedRestrictions()
    {
        return restrictions;
    }
}
