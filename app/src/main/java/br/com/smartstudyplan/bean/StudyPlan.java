package br.com.smartstudyplan.bean;

import java.util.HashMap;
import java.util.List;

import br.com.smartstudyplan.bean.Availability.Period;
import br.com.smartstudyplan.bean.Availability.Weekday;

/**
 * Esta classe encapsula as informações do plano de estudo completo, separado por dia da semana.
 */
public class StudyPlan {

    private HashMap<Integer, StudyPlanDay> studyPlanDays;

    public StudyPlan(){
        studyPlanDays = new HashMap<>(7);

        studyPlanDays.put( Weekday.SUNDAY.getValue(),    new StudyPlanDay() );
        studyPlanDays.put( Weekday.MONDAY.getValue(),    new StudyPlanDay() );
        studyPlanDays.put( Weekday.TUESDAY.getValue(),   new StudyPlanDay() );
        studyPlanDays.put( Weekday.WEDNESDAY.getValue(), new StudyPlanDay() );
        studyPlanDays.put( Weekday.THURSDAY.getValue(),  new StudyPlanDay() );
        studyPlanDays.put( Weekday.FRIDAY.getValue(),    new StudyPlanDay() );
        studyPlanDays.put( Weekday.SATURDAY.getValue(),  new StudyPlanDay() );
    }

    public void addCalendarSubject( Weekday weekday, Period period, CalendarSubject subject ){
        studyPlanDays.get(weekday.getValue()).addCalendarSubject( period, subject );
    }

    public void setCalendarSubjectsByWeekdayAndPeriod( Weekday weekday, Period period,
                                                       List<CalendarSubject> subjects){
        studyPlanDays.get(weekday.getValue()).setCalendarSubjectsByPeriod( period, subjects );
    }

    public void setAllCalendarSubjectsByWeekday( Weekday weekday,
                                        List<CalendarSubject> morning,
                                        List<CalendarSubject> afternoon,
                                        List<CalendarSubject> night ){
        studyPlanDays.get(weekday.getValue()).setAllCalendarSubjects( morning, afternoon, night );
    }

    public StudyPlanDay getStudyPlanDay( Weekday weekday ){
        return studyPlanDays.get( weekday.getValue() );
    }

    public List<CalendarSubject> getCalendarSubjectsByWeekdayAndPeriod( Weekday weekday, Period period ){
        return studyPlanDays.get( weekday.getValue() ).getCalendarSubjectsByPeriod( period );
    }

    @Override
    public String toString() {
        return "\n\n" + studyPlanDays.toString();
    }

}
