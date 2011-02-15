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
package models;

import models.deadbolt.Role;
import models.deadbolt.RoleHolder;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class User extends Model implements RoleHolder
{
    @Required
    public String userName;

    public String fullName;

    @Required
    @ManyToOne
    public ApplicationRole role;

    public User(String userName,
                String fullName,
                ApplicationRole role)
    {
        this.userName = userName;
        this.fullName = fullName;
        this.role = role;
    }

    public static User getByUserName(String userName)
    {
        return find("byUserName", userName).first();
    }

    @Override
    public String toString()
    {
        return this.userName;
    }


    public List<? extends Role> getRoles()
    {
        return Arrays.asList(role);
    }
}
