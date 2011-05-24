package security;

import controllers.AclController;
import controllers.deadbolt.RestrictedResourcesHandler;
import model.AccessControlPreference;
import model.AclUser;
import models.deadbolt.AccessResult;
import play.mvc.Http;

import java.security.AccessController;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
public abstract class AccessChainHandler implements RestrictedResourcesHandler
{
    public AccessResult checkAccess(List<String> resourceNames)
    {
        String currentUserName = AclController.getCurrentUserName();
        Http.Request request = Http.Request.current();
        String targetUserName = request.params.get("targetUserName");

        AccessResult accessResult = AccessResult.DENIED;
        if (targetUserName == null || currentUserName.equals(targetUserName))
        {
            // current user is viewing own information
            accessResult = AccessResult.ALLOWED;
        }
        else
        {
            AclUser targetUser = AclUser.getByUserName(targetUserName);

            switch (getAccessChain(targetUser.accessControlPreference))
            {
                case FRIENDS:
                    AclUser currentUser = AclUser.getByUserName(currentUserName);
                    accessResult = targetUser.isFriend(currentUser) ? AccessResult.ALLOWED : AccessResult.DENIED;
                    break;
                case ANYONE:
                    accessResult = AccessResult.ALLOWED;
                    break;
            }
        }

        return accessResult;
    }

    public abstract AccessControlPreference.AccessChain getAccessChain(AccessControlPreference preference);
}
