package models;

import models.deadbolt.Role;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class ApplicationRole extends Model implements Role
{
    @Required
    public String name;

    public ApplicationRole(String name)
    {
        this.name = name;
    }

    public String getRoleName()
    {
        return name;
    }

    public static ApplicationRole getByName(String name)
    {
        return ApplicationRole.find("byName", name).first();
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
