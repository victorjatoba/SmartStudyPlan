package br.com.smartstudyplan.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.List;

import br.com.smartstudyplan.bean.Availability.Period;
import br.com.smartstudyplan.bean.Availability.Weekday;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.Step;
import br.com.smartstudyplan.bean.StudyPlan;
import br.com.smartstudyplan.manager.StudyPlanManager_;
import br.com.smartstudyplan.preferences.UserPreferences_;
import br.com.smartstudyplan.util.SLog;

/**
 * Está classe é responsável por receber a solicitação para criar a notificação e verificar se
 * a notificação deve ser exibida.
 */
public class NotificationAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationAlarmReceiver";

    private static final int START_MORNING   = 6;
    private static final int START_AFTERNOON = 12;
    private static final int START_NIGHT     = 18;
    private static final int END_NIGHT       = 24;

    private StudyPlanManager_ mStudyPlanManager;

    private UserPreferences_  mUserPreferences;
    private SharedPreferences mDefaultPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        if( isNotificationEnabled( context ) == false ){
            SLog.d( TAG, "Notification is disabled." );
            return ;
        }

        if( isPlanCreated( context ) == false ){
            SLog.d( TAG, "Notification is not created." );
            return ;
        }

        Calendar calendar = Calendar.getInstance();

        Period  period  = null;
        Weekday weekday = null;

        if( ( calendar.get(Calendar.HOUR_OF_DAY) >= START_MORNING ) &&
            ( calendar.get(Calendar.HOUR_OF_DAY) <  START_AFTERNOON ) ){
            period = Period.AFTERNOON;
            weekday = Weekday.getWeekday(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        }
        else if( ( calendar.get(Calendar.HOUR_OF_DAY) >= START_AFTERNOON ) &&
                 ( calendar.get(Calendar.HOUR_OF_DAY) <  START_NIGHT ) ){
            period = Period.NIGHT;
            weekday = Weekday.getWeekday(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        }
        else if( ( calendar.get(Calendar.HOUR_OF_DAY) >= START_NIGHT ) &&
                 ( calendar.get(Calendar.HOUR_OF_DAY) <  END_NIGHT ) ) {
            period = Period.MORNING;
            weekday = Weekday.getWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        }

        if( ( period == null ) || ( weekday == null ) ){
            return ;
        }

        showNotification( context, period, weekday );
    }

    /**
     * Verifica se a notificação está habilitada pelo usuário.
     *
     * @param context contexto da aplicação
     * @return <code>true</code> caso esteja habilitado, <code>false</code> caso contrário
     */
    private boolean isNotificationEnabled( Context context ){
        if( mDefaultPreferences == null ){
            mDefaultPreferences = PreferenceManager.getDefaultSharedPreferences( context.getApplicationContext() );
        }

        return mDefaultPreferences.getBoolean("notification", false);
    }

    /**
     * Verifica se o plano de estudo já foi gerado.
     *
     * @param context contexto da aplicação
     * @return <code>true</code> caso o plano já foi gerado, <code>false</code> caso contrário.
     */
    private boolean isPlanCreated( Context context ){
        if( mUserPreferences == null ){
            mUserPreferences = new UserPreferences_( context );
        }

        return mUserPreferences.step().get() == Step.STEP_GENERATE_COMPLETED;
    }

    /**
     * Verifica se no período específico tem alguma matéria. Caso positivo, chama o sistema de
     * geração da notificação passando a exibi-las.
     *
     * @param context contexto da aplicação
     * @param period o período do dia
     * @param weekday o dia da semana
     */
    private void showNotification( Context context, Period period, Weekday weekday ){
        if( mStudyPlanManager == null ){
            mStudyPlanManager = StudyPlanManager_.getInstance_( context );
        }

        StudyPlan studyPlan = mStudyPlanManager.getStudyPlan();

        if( studyPlan != null ){
            List<CalendarSubject> list = mStudyPlanManager
                    .getStudyPlan().getCalendarSubjectsByWeekdayAndPeriod(weekday, period);

            if( ( list != null ) && ( list.isEmpty() == false ) ){
                SubjectNotificationBuilder.createNotification( context, list, period );
            }
            else{
                SLog.d(TAG, "This period does not contain subjects.");
            }
        }
        else{
            SLog.e(TAG, "Error on getting study plan.");
        }
    }
}
