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
package jobs;

import models.ApplicationRole;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@OnApplicationStart
public class TestDataLoader extends Job
{
    @Override
    public void doJob() throws Exception
    {
        if (ApplicationRole.getByName("superadmin") == null)
        {
            new ApplicationRole("superadmin").save();
        }
        if (ApplicationRole.getByName("standard-user") == null)
        {
            new ApplicationRole("standard-user").save();
        }

        if (User.getByUserName("steve") == null)
        {
            User user = new User("steve",
                                 "Steve Chaloner",
                                 ApplicationRole.getByName("superadmin"));
            user.save();
        }
        if (User.getByUserName("jim") == null)
        {
            User user = new User("jim",
                                 "Jim Bob",
                                 ApplicationRole.getByName("standard-user"));
            user.save();
        }
    }
}
