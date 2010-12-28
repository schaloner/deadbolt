package deadbolt;

import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import models.deadbolt.ExternalizedRestrictions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyExternalizedRestrictionsAccessor implements ExternalizedRestrictionsAccessor
{
    private final Map<String, ExternalizedRestrictions> restrictions = new HashMap<String, ExternalizedRestrictions>();

    public MyExternalizedRestrictionsAccessor()
    {
        restrictions.put("standard",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("foo"),
                                                        new MyExternalizedRestriction("bar")));
        restrictions.put("one-role-missing",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("foo"),
                                                        new MyExternalizedRestriction("rab")));
        restrictions.put("admin",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("admin")));
        restrictions.put("exclude-restricted",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("!restricted")));
    }

    public ExternalizedRestrictions getExternalizedRestrictions(String name)
    {
        return restrictions.get(name);
    }
}
