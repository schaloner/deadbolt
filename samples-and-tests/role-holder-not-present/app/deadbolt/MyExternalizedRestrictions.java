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
package deadbolt;

import models.deadbolt.ExternalizedRestriction;
import models.deadbolt.ExternalizedRestrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyExternalizedRestrictions implements ExternalizedRestrictions
{
    private final List<ExternalizedRestriction> restrictions = new ArrayList<ExternalizedRestriction>();

    public MyExternalizedRestrictions(ExternalizedRestriction... restrictions)
    {
        this.restrictions.addAll(Arrays.asList(restrictions));
    }

    public List<ExternalizedRestriction> getExternalisedRestrictions()
    {
        return restrictions;
    }
}
