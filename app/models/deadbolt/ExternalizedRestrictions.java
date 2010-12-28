package models.deadbolt;

import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public interface ExternalizedRestrictions
{
    List<ExternalizedRestriction> getExternalisedRestrictions();
}
