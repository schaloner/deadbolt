package controllers;

import controllers.deadbolt.RestrictedResourcesHandler;
import model.AccessControlPreference;
import models.deadbolt.AccessResult;
import security.AccessChainHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public class AclRestrictedResourcesHandler implements RestrictedResourcesHandler
{
    private static Map<String, RestrictedResourcesHandler> HANDLERS = new HashMap<String, RestrictedResourcesHandler>();

    static
    {
        HANDLERS.put("friends",
                     new AccessChainHandler()
                     {
                         public AccessControlPreference.AccessChain getAccessChain(AccessControlPreference preference)
                         {
                             return preference.friendVisibility;
                         }
                     });
        HANDLERS.put("photos",
                     new AccessChainHandler()
                     {
                         public AccessControlPreference.AccessChain getAccessChain(AccessControlPreference preference)
                         {
                             return preference.photoVisibility;
                         }
                     });

        // we can always see status updates, regardless of the user's preference
        HANDLERS.put("statusUpdates",
                     new RestrictedResourcesHandler()
                     {
                         public AccessResult checkAccess(List<String> resourceNames)
                         {
                             return AccessResult.ALLOWED;
                         }
                     });
    }

    public AccessResult checkAccess(List<String> resourceNames)
    {
        // here we assume the relevant resource name is first in the list, but you might want to check what else is in
        // there if you're defining multiple names per resource
        RestrictedResourcesHandler handler = HANDLERS.get(resourceNames.get(0));
        return handler.checkAccess(resourceNames);
    }
}
