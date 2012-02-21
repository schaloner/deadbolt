package controllers;

import controllers.deadbolt.Deadbolt;
import play.mvc.Controller;
import play.mvc.With;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@With(Deadbolt.class)
public abstract class RootController extends Controller
{
}
