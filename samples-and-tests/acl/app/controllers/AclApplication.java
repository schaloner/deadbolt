package controllers;

import controllers.deadbolt.Deadbolt;
import model.AclUser;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;

public class AclApplication extends AclController
{

    public static void index(String targetUserName)
    {
        AclUser targetUser;
        if (targetUserName == null)
        {
            targetUserName = getCurrentUserName();
        }
        targetUser = AclUser.getByUserName(targetUserName);
        render(targetUser);
    }
}