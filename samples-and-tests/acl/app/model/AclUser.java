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
package model;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class AclUser extends Model implements RoleHolder
{
    @Required
    public String userName;

    public String fullName;

    @Required
    @ManyToOne
    public AclApplicationRole role;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    public List<AclUser> friends;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Photo> photos;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<StatusUpdate> statusUpdates;

    @OneToOne(cascade = CascadeType.ALL)
    public AccessControlPreference accessControlPreference;

    public AclUser(String userName,
                   String fullName,
                   AclApplicationRole role)
    {
        this.userName = userName;
        this.fullName = fullName;
        this.role = role;
        this.accessControlPreference = new AccessControlPreference();
    }

    public static AclUser getByUserName(String userName)
    {
        return find("byUserName", userName).first();
    }

    public List<? extends Role> getRoles()
    {
        return Arrays.asList(role);
    }

    public void addPhoto(Photo photo)
    {
        if (photos == null)
        {
            photos = new ArrayList<Photo>();
        }
        photos.add(photo);
    }

    public void addStatusUpdate(StatusUpdate statusUpdate)
    {
        if (statusUpdates == null)
        {
            statusUpdates = new ArrayList<StatusUpdate>();
        }
        statusUpdates.add(statusUpdate);
    }

    public void addFriend(AclUser friend)
    {
        if (friends == null)
        {
            friends = new ArrayList<AclUser>();
        }
        friends.add(friend);
    }

    public boolean isFriend(AclUser user)
    {
        return friends != null && friends.contains(user);
    }

    @Override
    public String toString()
    {
        return this.userName;
    }
}
