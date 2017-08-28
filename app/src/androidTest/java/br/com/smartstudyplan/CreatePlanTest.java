package br.com.smartstudyplan;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.smartstudyplan.activity.CreatePlanActivity_;
import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.bean.StudyPlanDay;
import br.com.smartstudyplan.manager.ASPGAManager;
import br.com.smartstudyplan.manager.StudyPlanManager;
import ec.app.aspga.bean.AspgaContext;
import ec.app.aspga.bean.Period;
import ec.app.aspga.bean.PeriodAvailable;
import ec.app.aspga.bean.Student;
import ec.app.aspga.bean.Subject;

/**
 * Created by Victor on 01/02/2015.
 *
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 *
 */
public class CreatePlanTest extends ActivityInstrumentationTestCase2<CreatePlanActivity_> {

    private Instrumentation mInstrumentation;
    private StudyPlan mStudyPlan;
    private AspgaContext mAspgaContext;
    private Subject[] mSubjects;

    public CreatePlanTest() {
        super(CreatePlanActivity_.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAspgaContext = AspgaContext.getInstance();

        Student student = new Student();
        student.setName("Default");
        student.setHoursToLeisure(0);

        mAspgaContext.setStudent(new Student());

        mSubjects = new Subject[6];

        Subject subject0 = new Subject();
        subject0.setId(0);
        subject0.setName("A");
        subject0.setDifficulty((byte) 20);
        mSubjects[0] = subject0;

        Subject subject1 = new Subject();
        subject1.setId(1);
        subject1.setName("B");
        subject1.setDifficulty((byte) 40);
        mSubjects[1] = subject1;

        Subject subject2 = new Subject();
        subject2.setId(2);
        subject2.setName("C");
        subject2.setDifficulty((byte) 60);
        mSubjects[2] = subject2;

        Subject subject3 = new Subject();
        subject3.setId(3);
        subject3.setName("D");
        subject3.setDifficulty((byte) 80);
        mSubjects[3] = subject3;

        Subject subject4 = new Subject();
        subject4.setId(4);
        subject4.setName("E");
        subject4.setDifficulty((byte) 100);
        mSubjects[4] = subject4;

        Subject subject5 = new Subject();
        subject5.setId(5);
        subject5.setName("F");
        subject5.setDifficulty((byte) 0);
        mSubjects[5] = subject5;

        mAspgaContext.setSubjects(mSubjects);

        PeriodAvailable dayPeriodAvailable = new PeriodAvailable();
        Period[] periodsAvailable = new Period[7];
        Period periodWeekDay = new Period();
        periodWeekDay.setMorning('N');
        periodWeekDay.setAfternoon('B');
        periodWeekDay.setNight('B');
        int i = 0;
        for (; i<5; i++) {
            periodsAvailable[i] = periodWeekDay;
        }
        Period periodWeekend = new Period();
        periodWeekend.setMorning('N');
        periodWeekend.setAfternoon('S');
        periodWeekend.setNight('N');
        for (; i<7; i++) {
            periodsAvailable[i] = periodWeekend;
        }
        dayPeriodAvailable.setStudyCycle(periodsAvailable);

        mAspgaContext.setDayPeriodAvailable(dayPeriodAvailable);

        PeriodAvailable intellectualAvailable = new PeriodAvailable();
        Period[] periodsIntellectual = new Period[7];
        Period intPeriod = new Period();
        intPeriod.setMorning('B');
        intPeriod.setAfternoon('B');
        intPeriod.setNight('G');
        for (i=0; i<7; i++) {
            periodsIntellectual[i] = intPeriod;
        }
        intellectualAvailable.setStudyCycle(periodsIntellectual);

        mAspgaContext.setIntelectualAvailable(intellectualAvailable);

        mInstrumentation = getInstrumentation();

        mStudyPlan = ASPGAManager.createPlanWithECJ(mInstrumentation.getTargetContext());

    }

    public void testStudyPlanNotNull() {
        assertNotNull(mStudyPlan);
    }

    public void testNoStudyInTheMorning() {
        boolean noStudyInTheMorning = mStudyPlan.getStudyPlanDay(Availability.Weekday.SUNDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty() &&
                mStudyPlan.getStudyPlanDay(Availability.Weekday.MONDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty() &&
                mStudyPlan.getStudyPlanDay(Availability.Weekday.TUESDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty() &&
                mStudyPlan.getStudyPlanDay(Availability.Weekday.WEDNESDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty() &&
                mStudyPlan.getStudyPlanDay(Availability.Weekday.THURSDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty() &&
                mStudyPlan.getStudyPlanDay(Availability.Weekday.FRIDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty() &&
                mStudyPlan.getStudyPlanDay(Availability.Weekday.SATURDAY).getCalendarSubjectsByPeriod(Availability.Period.MORNING).isEmpty();

        assertTrue(noStudyInTheMorning);
    }

    public void testStudyInTheNight() {
        boolean studyInTheNight = mStudyPlan.getStudyPlanDay(Availability.Weekday.SUNDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty() ||
                mStudyPlan.getStudyPlanDay(Availability.Weekday.MONDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty() ||
                mStudyPlan.getStudyPlanDay(Availability.Weekday.TUESDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty() ||
                mStudyPlan.getStudyPlanDay(Availability.Weekday.WEDNESDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty() ||
                mStudyPlan.getStudyPlanDay(Availability.Weekday.THURSDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty() ||
                mStudyPlan.getStudyPlanDay(Availability.Weekday.FRIDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty() ||
                mStudyPlan.getStudyPlanDay(Availability.Weekday.SATURDAY).getCalendarSubjectsByPeriod(Availability.Period.NIGHT).isEmpty();

        assertTrue(studyInTheNight);
    }

    public void testValidStudyTimePerPeriod() {
        boolean timeIsValid = true;
        float totalPeriodStudyTime;
        checkValid:
            for (int i=0; i<7; i++) {
                for(int f=0; f<3; f++) {
                    totalPeriodStudyTime = 0;
                    List<CalendarSubject> subjects = mStudyPlan.getStudyPlanDay(Availability.Weekday.getWeekday(i)).getCalendarSubjectsByPeriod(Availability.Period.getPeriod(f));
                    for(CalendarSubject subject : subjects) {
                        totalPeriodStudyTime += subject.getDuration();
                    }
                    if(totalPeriodStudyTime > 5) {
                        timeIsValid = false;
                        break checkValid;
                    }
                }
            }

        assertTrue(timeIsValid);
    }

    public void testValidStudyTimePerSubject() {
        boolean timeIsValid = true;
        checkValid:
            for (int i=0; i<7; i++) {
                for(int f=0; f<3; f++) {
                    List<CalendarSubject> subjects = mStudyPlan.getStudyPlanDay(Availability.Weekday.getWeekday(i)).getCalendarSubjectsByPeriod(Availability.Period.getPeriod(f));
                    for(CalendarSubject subject : subjects) {
                        if(subject.getDuration() <= 0 || subject.getDuration() > 5 ) {
                            timeIsValid = false;
                            break checkValid;
                        }
                    }
                }
            }

        assertTrue(timeIsValid);
    }

    public void testContainsAllSubjects() {
        boolean containsAllSubjects = false;
        List<Boolean> subjectIdsContained = new ArrayList<>(6);
        for(int i=0; i<6; i++) {
            subjectIdsContained.add(false);
        }

        checkValid:
            for (int i=0; i<7; i++) {
                for(int f=0; f<3; f++) {
                    List<CalendarSubject> subjects = mStudyPlan.getStudyPlanDay(Availability.Weekday.getWeekday(i)).getCalendarSubjectsByPeriod(Availability.Period.getPeriod(f));
                    for(CalendarSubject subject : subjects) {
                        subjectIdsContained.set((int) subject.getSubject().getId(), true);
                        if( !subjectIdsContained.contains(false) ) {
                            containsAllSubjects = true;
                            break checkValid;
                        }
                    }
                }
            }

        assertTrue(containsAllSubjects);
    }

}
