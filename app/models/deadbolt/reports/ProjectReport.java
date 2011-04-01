package models.deadbolt.reports;

import models.deadbolt.reports.ControllerReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class ProjectReport
{
    public String projectName;

    public final List<ControllerReport> controllerReports = new ArrayList<ControllerReport>();

    public void addControllerReport(ControllerReport controllerReport)
    {
        controllerReports.add(controllerReport);
    }
}
