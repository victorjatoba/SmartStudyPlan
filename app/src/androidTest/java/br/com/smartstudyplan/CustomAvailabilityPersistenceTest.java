package br.com.smartstudyplan;

import android.test.ActivityInstrumentationTestCase2;

import br.com.smartstudyplan.activity.CustomAvailabilityActivity_;
import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.manager.StudyPlanManager_;

/**
 * Created by Victor on 18/12/2014.
 *
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class CustomAvailabilityPersistenceTest extends ActivityInstrumentationTestCase2<CustomAvailabilityActivity_> {

    private Availability mAvailability;
    private StudyPlanManager mStudyPlanManager;

    public CustomAvailabilityPersistenceTest() {
        super(CustomAvailabilityActivity_.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mStudyPlanManager = StudyPlanManager_.getInstance_(getInstrumentation().getTargetContext());

        mAvailability = new Availability();

    }

    public void testSaveAvailability(){
        mStudyPlanManager.saveAvailabilities(mAvailability);
        mAvailability = mStudyPlanManager.getAvailabilities();
        assertEquals(mAvailability.getAvailable(Availability.Weekday.SUNDAY, Availability.Period.MORNING), Availability.Available.NONE);
    }

    public void testMondayAfternoonFullTime(){
        mAvailability.setValue(Availability.Weekday.MONDAY, Availability.Period.AFTERNOON, Availability.Available.FULL_TIME);

        mStudyPlanManager.saveAvailabilities(mAvailability);
        mAvailability = mStudyPlanManager.getAvailabilities();
        assertEquals(mAvailability.getAvailable(Availability.Weekday.MONDAY, Availability.Period.AFTERNOON), Availability.Available.FULL_TIME);
    }

    public void testTuesdayNightPartTime(){
        mAvailability.setValue(Availability.Weekday.TUESDAY, Availability.Period.NIGHT, Availability.Available.PART_TIME);

        mStudyPlanManager.saveAvailabilities(mAvailability);
        mAvailability = mStudyPlanManager.getAvailabilities();
        assertEquals(mAvailability.getAvailable(Availability.Weekday.TUESDAY, Availability.Period.NIGHT), Availability.Available.PART_TIME);
    }

    public void testWednesday(){
        mAvailability.setValue(Availability.Weekday.WEDNESDAY, Availability.Period.MORNING, Availability.Available.NONE);
        mAvailability.setValue(Availability.Weekday.WEDNESDAY, Availability.Period.AFTERNOON, Availability.Available.FULL_TIME);
        mAvailability.setValue(Availability.Weekday.WEDNESDAY, Availability.Period.NIGHT, Availability.Available.PART_TIME);

        mStudyPlanManager.saveAvailabilities(mAvailability);
        mAvailability = mStudyPlanManager.getAvailabilities();
        assertEquals(mAvailability.getAvailable(Availability.Weekday.WEDNESDAY, Availability.Period.MORNING), Availability.Available.NONE);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.WEDNESDAY, Availability.Period.AFTERNOON), Availability.Available.FULL_TIME);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.WEDNESDAY, Availability.Period.NIGHT), Availability.Available.PART_TIME);
    }

    public void testThursdayNoAfternoonSet(){
        mAvailability.setValue(Availability.Weekday.THURSDAY, Availability.Period.MORNING, Availability.Available.PART_TIME);
        mAvailability.setValue(Availability.Weekday.THURSDAY, Availability.Period.NIGHT, Availability.Available.PART_TIME);

        mStudyPlanManager.saveAvailabilities(mAvailability);
        mAvailability = mStudyPlanManager.getAvailabilities();
        assertEquals(mAvailability.getAvailable(Availability.Weekday.THURSDAY, Availability.Period.MORNING), Availability.Available.PART_TIME);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.THURSDAY, Availability.Period.AFTERNOON), Availability.Available.NONE);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.THURSDAY, Availability.Period.NIGHT), Availability.Available.PART_TIME);
    }

    public void testFridaySaturday(){
        mAvailability.setValue(Availability.Weekday.FRIDAY, Availability.Period.AFTERNOON, Availability.Available.FULL_TIME);
        mAvailability.setValue(Availability.Weekday.FRIDAY, Availability.Period.NIGHT, Availability.Available.PART_TIME);
        mAvailability.setValue(Availability.Weekday.SATURDAY, Availability.Period.MORNING, Availability.Available.PART_TIME);
        mAvailability.setValue(Availability.Weekday.SATURDAY, Availability.Period.AFTERNOON, Availability.Available.FULL_TIME);

        mStudyPlanManager.saveAvailabilities(mAvailability);
        mAvailability = mStudyPlanManager.getAvailabilities();
        assertEquals(mAvailability.getAvailable(Availability.Weekday.FRIDAY, Availability.Period.MORNING), Availability.Available.NONE);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.FRIDAY, Availability.Period.AFTERNOON), Availability.Available.FULL_TIME);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.FRIDAY, Availability.Period.NIGHT), Availability.Available.PART_TIME);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.SATURDAY, Availability.Period.MORNING), Availability.Available.PART_TIME);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.SATURDAY, Availability.Period.AFTERNOON), Availability.Available.FULL_TIME);
        assertEquals(mAvailability.getAvailable(Availability.Weekday.SATURDAY, Availability.Period.NIGHT), Availability.Available.NONE);
    }

}
