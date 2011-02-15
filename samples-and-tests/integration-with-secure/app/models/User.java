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
