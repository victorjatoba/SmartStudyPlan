package br.com.smartstudyplan.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.smartstudyplan.bean.Availability.Period;

/**
 * Esta classe encapsula as informações do plano de estudo de um dia da semana, separado por
 * período do dia.
 */
public class StudyPlanDay {

    private HashMap<Integer, List<CalendarSubject>> daySubjects;

    public StudyPlanDay(){
        daySubjects = new HashMap<>(3);

        daySubjects.put( Period.MORNING.getValue(),   new ArrayList<CalendarSubject>());
        daySubjects.put( Period.AFTERNOON.getValue(), new ArrayList<CalendarSubject>());
        daySubjects.put( Period.NIGHT.getValue(),     new ArrayList<CalendarSubject>());
    }

    public void addCalendarSubject( Period period, CalendarSubject subject ){
        daySubjects.get(period.getValue()).add( subject );
    }

    public void setCalendarSubjectsByPeriod(Period period, List<CalendarSubject> subjects){
        daySubjects.get(period.getValue()).clear();
        daySubjects.get(period.getValue()).addAll(subjects);
    }

    public void setAllCalendarSubjects( List<CalendarSubject> morning,
                                        List<CalendarSubject> afternoon,
                                        List<CalendarSubject> night ){
        daySubjects.get(Period.MORNING.getValue()).clear( );
        daySubjects.get(Period.AFTERNOON.getValue()).clear( );
        daySubjects.get(Period.NIGHT.getValue()).clear( );

        daySubjects.get(Period.MORNING.getValue()).addAll(morning);
        daySubjects.get(Period.AFTERNOON.getValue()).addAll(afternoon);
        daySubjects.get(Period.NIGHT.getValue()).addAll(night);
    }

    public List<CalendarSubject> getCalendarSubjectsByPeriod( Period period ){
        return daySubjects.get( period.getValue() );
    }

    public HashMap<Integer, List<CalendarSubject>> getAll(){
        return daySubjects;
    }

    @Override
    public String toString() {
        return "\nMorning: \n" + daySubjects.get( Period.MORNING.getValue() ).toString() + "\n\n" +
               "Afternoon: \n" + daySubjects.get( Period.AFTERNOON.getValue() ).toString() + "\n\n" +
               "Night: \n" + daySubjects.get( Period.NIGHT.getValue() ).toString() + "\n\n\n";
    }
}
