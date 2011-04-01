package models.deadbolt.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class MethodReport
{
    public final String name;

    public final List<String> dynamicChecks = new ArrayList<String>();

    public final List<String> staticChecks = new ArrayList<String>();

    public MethodReport(String name)
    {
        this.name = name;
    }

    public void addStaticCheck(String staticCheck)
    {
        staticChecks.add(staticCheck);
    }

    public void addDynamicCheck(String dynamicCheck)
    {
        staticChecks.add(dynamicCheck);
    }

}
