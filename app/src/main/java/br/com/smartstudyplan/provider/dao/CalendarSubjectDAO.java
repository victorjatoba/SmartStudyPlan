package br.com.smartstudyplan.provider.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import br.com.smartstudyplan.bean.Availability.Period;
import br.com.smartstudyplan.bean.Availability.Weekday;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.bean.StudyPlanDay;
import br.com.smartstudyplan.bean.Subject;
import br.com.smartstudyplan.provider.StudyPlanProvider;
import br.com.smartstudyplan.provider.table.CalendarSubjectTable;
import br.com.smartstudyplan.util.SLog;

/**
 * Esta classe é responsável por realizar as operações do CRUD no content provider, referente ao
 * <code>CalendarSubject</code>.
 */
@EBean( scope = EBean.Scope.Singleton )
public class CalendarSubjectDAO {
    private static final String TAG = "SubjectDAO";

    @RootContext Context mContext;

    private ContentResolver mContentResolver;

    private StudyPlan mStudyPlan;

    public CalendarSubjectDAO(){
        mStudyPlan = new StudyPlan();
    }

    /**
     * Primeiro método a ser chamado após a instancia da classe. Preenche o plano de estudo com
     * <code>CalendarSubject</code> retornado do content provider.
     */
    @AfterInject
    void init(){
        mContentResolver = mContext.getContentResolver();

        final StudyPlan studyPlan = findStudyPlan();
        if( studyPlan != null ){
            synchronized ( mStudyPlan ) {
                mStudyPlan = studyPlan;
            }
        }
    }

    /**
     * Retorna o plano de estudo contendo todos os <code>CalendarSubject</code>.
     *
     * @return o plano de estudo
     */
    public StudyPlan getStudyPlan(){
        return mStudyPlan;
    }

    /**
     * Retorna um dia do plano do estudo contendo os <code>CalendarSubject</code> referente àquele
     * dia da semana.
     *
     * @param weekday o dia da semana
     * @return o dia do plano de estudo
     */
    public StudyPlanDay getStudyPlanDay( Weekday weekday ){
        synchronized ( mStudyPlan ){
            return mStudyPlan.getStudyPlanDay( weekday );
        }
    }

    /**
     * Salva um novo <code>CalendarSubject</code> no content provider.
     *
     * @param weekday o dia da semana
     * @param period o período
     * @param calendarSubject o <code>CalendarSubject</code>
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean add( Weekday weekday, Period period, CalendarSubject calendarSubject ){
        if( calendarSubject.getId() > 0 ){
            return update( weekday, period, calendarSubject );
        }

        ContentValues contentValues = calendarSubject.getContentValues();

        contentValues.put( CalendarSubjectTable.WEEKDAY.getColumnName(),       weekday.getValue() );
        contentValues.put( CalendarSubjectTable.PERIOD.getColumnName(),        period.getValue() );

        Uri newUri = mContentResolver
                .insert( StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT, contentValues );

        int id = 0;
        if( newUri != null ){
            id = Integer.valueOf(newUri.getLastPathSegment());
        }

        if( id > 0 ){
            calendarSubject.setId( id );
            mStudyPlan.addCalendarSubject( weekday, period, calendarSubject );

            return true;
        }

        return false;
    }

    /**
     * Salva o plano de estudo no content provider.
     *
     * @param studyPlan o plano de estudo
     */
    public void setStudyPlan( StudyPlan studyPlan ){
        if( studyPlan != null ){
            removeAll();
            for( Weekday weekday : Weekday.values() ){
                for( Period period : Period.values() ){
                    studyPlan.getCalendarSubjectsByWeekdayAndPeriod( weekday, period );
                    if( studyPlan.getCalendarSubjectsByWeekdayAndPeriod( weekday, period ).size() > 0 ){
                        addAll( weekday, period, studyPlan.getCalendarSubjectsByWeekdayAndPeriod( weekday, period ) );
                    }
                }
            }
        }
    }

    /**
     * Salva uma lista de <code>CalendarSubject</code> no content provider.
     *
     * @param weekday o dia da semana
     * @param period o período
     * @param calendarSubjectList a lista de <code>CalendarSubject</code>
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean addAll( Weekday weekday, Period period, List<CalendarSubject> calendarSubjectList ){
        boolean result = true;
        int i = 0;
        for( CalendarSubject calendarSubject : calendarSubjectList ){
            calendarSubject.setId(0);
            result &= add( weekday, period, calendarSubject );
            i++;
        }

        return result;
    }

    /**
     * Atualiza um CalendarSubject no content provider.
     *
     * @param weekday o dia da semana
     * @param period o período
     * @param calendarSubject o <code>CalendarSubject</code> a ser atualizado
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean update( Weekday weekday, Period period, CalendarSubject calendarSubject ){
        ContentValues contentValues = calendarSubject.getContentValues();

        contentValues.put( CalendarSubjectTable.WEEKDAY.getColumnName(), weekday.getValue() );
        contentValues.put( CalendarSubjectTable.PERIOD.getColumnName(),  period.getValue() );

        int rows = mContentResolver.update(StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT,
                contentValues,
                CalendarSubjectTable.WHERE_ID,
                new String[]{ String.valueOf( calendarSubject.getId() ) } );

        if( rows > 0 ){
            synchronized ( mStudyPlan ){
                int position = 0;
                for( int i = 0; i < mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period).size(); i++ ){
                    if( mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period).get(i).getId() == calendarSubject.getId() ){
                        position = i;
                        break;
                    }
                }

                mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period).remove(position);
                mStudyPlan.getCalendarSubjectsByWeekdayAndPeriod(weekday, period).add(position, calendarSubject);

                return true;
            }
        }

        return false;
    }

    /**
     * Busca todos os CalendarSubject do content provider.
     *
     * @return o plano de estudo contendo todos os <code>CalendarSubject</code>
     */
    private StudyPlan findStudyPlan() {
        StudyPlan studyPlan = null;
        final Cursor cursor = mContentResolver.query( StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT,
                CalendarSubjectTable.SQL_SELECT, null, null, null );

        if ( cursor != null ) {
            if ( cursor.moveToFirst() ) {

                studyPlan = new StudyPlan();

                SLog.d( TAG, "Loading all audits" );

                int idIndex = cursor.getColumnIndex( CalendarSubjectTable._ID.getColumnName() );
                int idSubjectIndex = cursor.getColumnIndex( CalendarSubjectTable.SUBJECT_ID.getColumnName() );
                int idWeekdayIndex = cursor.getColumnIndex( CalendarSubjectTable.WEEKDAY.getColumnName() );
                int idPeriodIndex = cursor.getColumnIndex( CalendarSubjectTable.PERIOD.getColumnName() );
                int idTimeIndex = cursor.getColumnIndex( CalendarSubjectTable.TIME.getColumnName() );

                do{
                    final CalendarSubject calendarSubject = new CalendarSubject();

                    Subject subject = new Subject();
                    subject.setId( cursor.getLong( idSubjectIndex ) );

                    Weekday weekday = Weekday.getWeekday( cursor.getInt( idWeekdayIndex ) );
                    Period  period  = Period.getPeriod(cursor.getInt(idPeriodIndex));

                    calendarSubject.setId( cursor.getLong( idIndex ) );
                    calendarSubject.setSubject( subject );

                    calendarSubject.setDuration(cursor.getFloat(idTimeIndex));

                    studyPlan.addCalendarSubject( weekday, period, calendarSubject );
                }
                while ( cursor.moveToNext() ) ;
            }
            cursor.close();
        }

        return studyPlan;
    }

    /**
     * Este método é chamado quando fecha o aplicativo.
     */
    public void finish(){
        mStudyPlan = null;
    }

    /**
     * Remove os <code>CalendarSubject</code> do content provider por dia da semana e período.
     *
     * @param weekday o dia da semana
     * @param period o período
     * @return <code>true</code> se feito com sucesso, <code>false</code> caso contrário
     */
    public boolean removeCalendarSubjectByWeekdayAndPeriod( Weekday weekday, Period period ){
        int deletedRows = mContentResolver.delete( StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT,
                CalendarSubjectTable.WHERE_WEEKDAY_AND_PERIOD,
                new String[]{ String.valueOf( weekday.getValue() ), String.valueOf( period.getValue() ) } );

        boolean isDeleted = deletedRows > 0;

        if( isDeleted ){
            synchronized ( mStudyPlan ){
                mStudyPlan.setCalendarSubjectsByWeekdayAndPeriod( weekday, period,
                        new ArrayList<CalendarSubject>() );
            }
        }

        return isDeleted;
    }

    /**
     * Remove todos os <code>CalendarSubject</code> do content provider.
     */
    public void removeAll() {
        int value = mContentResolver.delete( StudyPlanProvider.CONTENT_URI_CALENDAR_SUBJECT, null, null );
        mStudyPlan = new StudyPlan();
        SLog.d(TAG, "Deleted " + value + " calendar subjects.");
    }
}
