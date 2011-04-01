package controllers.deadbolt.reports;

import controllers.deadbolt.RestrictedResource;
import models.deadbolt.reports.ControllerReport;
import models.deadbolt.reports.MethodReport;
import models.deadbolt.reports.ProjectReport;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Provides a report on the restrictions imposed with Deadbolt.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
public class DeadboltReporter extends Controller
{
    @Before
    public static void disableInProduction()
    {
        if (Play.mode == Play.Mode.PROD)
        {
            error(404, "Page not found");
        }
    }

    public static void report()
    {
        ProjectReport projectReport = new ProjectReport();
        projectReport.projectName = Play.configuration.getProperty("application.name");

        List<Class> controllers = Play.classloader.getAssignableClasses(Controller.class);
        for (Class controller : controllers)
        {
            ControllerReport controllerReport = getControllerReport(controller);
            projectReport.addControllerReport(controllerReport);
        }

        render(projectReport);
    }

    private static ControllerReport getControllerReport(Class controller)
    {
        ControllerReport controllerReport = new ControllerReport(controller.getCanonicalName());

        Method[] methods = controller.getMethods();
        for (Method method : methods)
        {
            if (Modifier.isStatic(method.getModifiers()) && !method.getDeclaringClass().equals(Controller.class))
            {
                MethodReport methodReport = getMethodReport(method);
                controllerReport.addMethodReport(methodReport);
            }
        }

        return controllerReport;
    }

    private static MethodReport getMethodReport(Method method)
    {
        MethodReport methodReport = new MethodReport(method.getName());
        RestrictedResource restrictedResource = getAnnotation(RestrictedResource.class,
                                                              method);
        if (restrictedResource != null)
        {
            methodReport.addDynamicCheck(String.format("Dynamic check with key [%s]", "" + Arrays.toString(restrictedResource.name())));
            if (restrictedResource.staticFallback())
            {
                methodReport.addDynamicCheck("If dynamic check returns AccessResult.NOT_SPECIFIED, static security checks will be applied");
            }
            else
            {
                methodReport.addDynamicCheck("If dynamic check returns AccessResult.NOT_SPECIFIED, access to this method will be blocked");
            }
        }

        return methodReport;
    }

    private static <T extends Annotation> T getAnnotation(Class<T> clazz,
                                                          Method method)
    {
        T annotation = null;
        if (method.isAnnotationPresent(clazz))
        {
            annotation = method.getAnnotation(clazz);
        }
        else
        {
            Class<?> declaringClass = method.getDeclaringClass();
            if (declaringClass.isAnnotationPresent(clazz))
            {
                annotation = declaringClass.getAnnotation(clazz);
            }
        }

        return annotation;
    }
}
