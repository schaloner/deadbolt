/*
 * Copyright 2010-2011 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jobs;

import model.AclApplicationRole;
import model.AclUser;
import model.Photo;
import model.StatusUpdate;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@OnApplicationStart
public class AclTestDataLoader extends Job
{
    @Override
    public void doJob() throws Exception
    {
        roles();
        users();
        defineRelationships();
        content();
    }

    private void roles()
    {
        if (AclApplicationRole.getByName("standard-user") == null)
        {
            new AclApplicationRole("standard-user").save();
        }
    }

    private void users()
    {
        if (AclUser.getByUserName("steve") == null)
        {
            AclUser user = new AclUser("steve",
                                       "Steve",
                                       AclApplicationRole.getByName("standard-user"));
            user.save();
        }
        if (AclUser.getByUserName("greet") == null)
        {
            AclUser user = new AclUser("greet",
                                       "Greet",
                                       AclApplicationRole.getByName("standard-user"));
            user.save();
        }
        if (AclUser.getByUserName("christophe") == null)
        {
            AclUser user = new AclUser("christophe",
                                       "Christophe",
                                       AclApplicationRole.getByName("standard-user"));
            user.save();
        }
        if (AclUser.getByUserName("ansje") == null)
        {
            AclUser user = new AclUser("ansje",
                                       "Ansje",
                                       AclApplicationRole.getByName("standard-user"));
            user.save();
        }
    }

    private void defineRelationships()
    {
        AclUser steve = AclUser.getByUserName("steve");
        if (isEmpty(steve.friends))
        {
            steve.addFriend(AclUser.getByUserName("greet"));
            steve.save();
        }

        AclUser greet = AclUser.getByUserName("greet");
        if (isEmpty(greet.friends))
        {
            greet.addFriend(AclUser.getByUserName("ansje"));
            greet.save();
        }

        AclUser ansje = AclUser.getByUserName("ansje");
        if (isEmpty(ansje.friends))
        {
            ansje.addFriend(AclUser.getByUserName("christophe"));
            ansje.save();
        }

        AclUser christophe = AclUser.getByUserName("christophe");
        if (isEmpty(christophe.friends))
        {
            christophe.addFriend(AclUser.getByUserName("steve"));
            christophe.save();
        }
    }


    private void content()
    {
        List<AclUser> users = Arrays.asList(AclUser.getByUserName("steve"),
                                            AclUser.getByUserName("greet"),
                                            AclUser.getByUserName("christophe"),
                                            AclUser.getByUserName("ansje"));

        for (AclUser user : users)
        {
            if (isEmpty(user.photos))
            {
                for (int i = 0; i < 5; i++)
                {
                    Photo photo = new Photo();
                    photo.description = user.fullName + "'s photo #" + i;
                    user.addPhoto(photo);
                }
            }
            if (isEmpty(user.statusUpdates))
            {
                for (int i = 0; i < 5; i++)
                {
                    StatusUpdate statusUpdate = new StatusUpdate();
                    statusUpdate.status = user.fullName + " is feeling " + i;
                    user.addStatusUpdate(statusUpdate);
                }
            }
            user.save();
        }
    }

    private boolean isEmpty(Collection c)
    {
        return c == null || c.isEmpty();
    }
}
 