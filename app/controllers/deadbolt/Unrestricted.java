package controllers.deadbolt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a resource is unprotected.  This annotation is not inherited - it must be present on any controller or
 * method that is unrestricted. If a method or controller is marked as unrestricted, that will take precedence over
 * any restrictions.
 *
 * <p>
 *     An unrestricted controller may still have restricted methods.  A restricted controller may have
 * unrestricted methods.
 * </p>
 *
 * <ul>
 *     <li>For methods, access is unrestricted</li>
 *     <li>For controllers, access is unrestricted even if the controller extends a controller that is protected</li>
 * </ul>
 * @author Steve Chaloner (steve@objectify.be)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Unrestricted
{
}
