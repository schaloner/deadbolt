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
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class AclApplicationRole extends Model implements Role
{
    @Required
    public String name;

    public AclApplicationRole(String name)
    {
        this.name = name;
    }

    public String getRoleName()
    {
        return name;
    }

    public static AclApplicationRole getByName(String name)
    {
        return AclApplicationRole.find("byName", name).first();
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
