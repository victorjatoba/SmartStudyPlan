package br.com.smartstudyplan;

import android.test.ActivityInstrumentationTestCase2;

import br.com.smartstudyplan.activity.EaseOfLearningActivity_;
import br.com.smartstudyplan.bean.EaseOfLearning;
import br.com.smartstudyplan.manager.StudyPlanManager;
import br.com.smartstudyplan.manager.StudyPlanManager_;

/**
 * Created by Victor on 18/12/2014.
 *
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class EaseOfLearningTest extends ActivityInstrumentationTestCase2<EaseOfLearningActivity_> {

    private StudyPlanManager mStudyPlanManager;
    private EaseOfLearning easeOfLearning;

    public EaseOfLearningTest() {
        super(EaseOfLearningActivity_.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mStudyPlanManager = StudyPlanManager_.getInstance_(getInstrumentation().getTargetContext());

        easeOfLearning = new EaseOfLearning();
    }

    public void testCheckboxMorningTrue(){
        easeOfLearning.setEaseOfLearningMorning(true);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningMorning(), easeOfLearning.isEaseOfLearningMorning());
    }

    public void testCheckboxMorningFalse(){
        easeOfLearning.setEaseOfLearningMorning(false);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningMorning(), easeOfLearning.isEaseOfLearningMorning());
    }

    public void testCheckboxAfternoonTrue(){
        easeOfLearning.setEaseOfLearningAfternoon(true);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningAfternoon(), easeOfLearning.isEaseOfLearningAfternoon());
    }

    public void testCheckboxAfternoonFalse(){
        easeOfLearning.setEaseOfLearningAfternoon(false);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningAfternoon(), easeOfLearning.isEaseOfLearningAfternoon());
    }

    public void testCheckboxNightTrue(){
        easeOfLearning.setEaseOfLearningNight(true);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningNight(), easeOfLearning.isEaseOfLearningNight());
    }

    public void testCheckboxNightFalse(){
        easeOfLearning.setEaseOfLearningNight(false);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningNight(), easeOfLearning.isEaseOfLearningNight());
    }

    public void testCheckboxMorningNightTrueAfternoonFalse(){
        easeOfLearning.setEaseOfLearningMorning(true);
        easeOfLearning.setEaseOfLearningNight(true);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningMorning(), easeOfLearning.isEaseOfLearningMorning());
        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningAfternoon(), easeOfLearning.isEaseOfLearningAfternoon());
        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningNight(), easeOfLearning.isEaseOfLearningNight());
    }

    public void testCheckboxAllTrue(){
        easeOfLearning.setEaseOfLearningMorning(true);
        easeOfLearning.setEaseOfLearningAfternoon(true);
        easeOfLearning.setEaseOfLearningNight(true);

        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningMorning(), easeOfLearning.isEaseOfLearningMorning());
        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningAfternoon(), easeOfLearning.isEaseOfLearningAfternoon());
        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningNight(), easeOfLearning.isEaseOfLearningNight());
    }

    public void testCheckboxAllFalse(){
        mStudyPlanManager.saveEaseOfLearning(easeOfLearning);

        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningMorning(), easeOfLearning.isEaseOfLearningMorning());
        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningAfternoon(), easeOfLearning.isEaseOfLearningAfternoon());
        assertEquals(mStudyPlanManager.getEaseOfLearning().isEaseOfLearningNight(), easeOfLearning.isEaseOfLearningNight());
    }
}
