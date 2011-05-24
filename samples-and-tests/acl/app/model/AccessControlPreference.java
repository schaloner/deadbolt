package model;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
public class AccessControlPreference extends Model
{
    public enum AccessChain { FRIENDS, ANYONE };

    @Enumerated(EnumType.STRING)
    public AccessChain friendVisibility = AccessChain.ANYONE;

    @Enumerated(EnumType.STRING)
    public AccessChain statusVisibility = AccessChain.ANYONE;

    @Enumerated(EnumType.STRING)
    public AccessChain photoVisibility = AccessChain.ANYONE;
}
