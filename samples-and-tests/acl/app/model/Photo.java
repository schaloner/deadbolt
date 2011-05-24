package model;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class Photo extends Model
{
    public String description;
}
