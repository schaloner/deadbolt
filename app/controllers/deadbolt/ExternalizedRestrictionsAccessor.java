package controllers.deadbolt;

import models.deadbolt.ExternalizedRestrictions;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public interface ExternalizedRestrictionsAccessor
{
    ExternalizedRestrictions getExternalizedRestrictions(String name);
}
