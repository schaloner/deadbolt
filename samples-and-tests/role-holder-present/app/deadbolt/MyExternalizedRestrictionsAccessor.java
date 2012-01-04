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

import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import models.deadbolt.ExternalizedRestrictions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steve Chaloner (steve@objectify.be).
 */
public class MyExternalizedRestrictionsAccessor implements ExternalizedRestrictionsAccessor
{
    private final Map<String, ExternalizedRestrictions> restrictions = new HashMap<String, ExternalizedRestrictions>();

    public MyExternalizedRestrictionsAccessor()
    {
        restrictions.put("standard",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("foo"),
                                                        new MyExternalizedRestriction("bar")));
        restrictions.put("one-role-missing",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("foo"),
                                                        new MyExternalizedRestriction("rab")));
        restrictions.put("admin",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("admin")));
        restrictions.put("exclude-restricted",
                         new MyExternalizedRestrictions(new MyExternalizedRestriction("!restricted")));
    }

    public ExternalizedRestrictions getExternalizedRestrictions(String name)
    {
        return restrictions.get(name);
    }
}
