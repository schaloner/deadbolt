package models.deadbolt.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class ControllerReport
{
    public final String name;

    public final List<MethodReport> methodReports = new ArrayList<MethodReport>();

    public ControllerReport(String name)
    {
        this.name = name;
    }

    public void addMethodReport(MethodReport methodReport)
    {
        methodReports.add(methodReport);
    }
}
