package controllers;

import model.AccessControlPreference;
import model.AclUser;
import play.mvc.Controller;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class AccessControlPreferences extends AclController
{
    public static void index()
    {
        AclUser user = AclUser.getByUserName(getCurrentUserName());
        AccessControlPreference preference = user.accessControlPreference;
        render(preference);
    }

    public static void update(AccessControlPreference preference)
    {
        preference.save();
        AclApplication.index(null);
    }
}
