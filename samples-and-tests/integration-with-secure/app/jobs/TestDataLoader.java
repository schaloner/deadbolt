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
