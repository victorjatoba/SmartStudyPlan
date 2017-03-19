package br.com.smartstudyplan.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import br.com.smartstudyplan.util.SLog;

/**
 * Receiver responsável por chamar o AlarmManager que irá solicitar a exibição das notificações a
 * cada período específico.
 * Será chamado quando:
 * - Um novo plano for gerado;
 * - O aparelho for iniciado;
 * - Quando for solicitado o cancelamento da notificação;
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    private static final int INTENT_REQUEST_CODE = 99;
    private static final int INIT_MORNING_HOUR   = 9;

    /**
     * Identificação do Action no broadcast quando um novo plano é gerado.
     */
    public static final String CREATED_PLAN_RECEIVER    = "br.com.smartstudyplan.CREATED_PLAN";

    /**
     * Identificação do Action no broadcast quando solicitado o cancelamento das notificações.
     */
    public static final String CANCEL_ALARM_MANAGER     = "br.com.smartstudyplan.CANCEL_ALARM";

    /**
     * Identificação do Action no broadcast quando o aparelho for reiniciado.
     */
    private static final String BOOT_COMPLETED_RECEIVER = "android.intent.action.BOOT_COMPLETED";

    /**
     * Intervalo de tempo em que o sistema de notificação será requisitado.
     */
    private static final long ALARM_INTERVAL = 6 * 3600 * 1000; //6 HOURS

    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        if( mAlarmManager == null ){
            mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }

        if( intent.getAction().equals(BOOT_COMPLETED_RECEIVER) ||
            intent.getAction().equals(CREATED_PLAN_RECEIVER) ){
            SLog.d(TAG, "Start alarm, receiver action: " + intent.getAction() );
            startNotificationAlarmManager( context );
        }
        else if( intent.getAction().equals(CANCEL_ALARM_MANAGER) ){
            SLog.d(TAG, "Stop alarm");
            stopNotificationAlarmManager( context );
        }
    }

    /**
     * Configura o alarm manager para chamar o sistema de notificação de tempos em tempos.
     *
     * @param context contexto da aplicação
     */
    private void startNotificationAlarmManager( Context context ){
        Intent notificationAlarmIntent = new Intent(context, NotificationAlarmReceiver.class);
        mAlarmIntent  = PendingIntent.getBroadcast(context, INTENT_REQUEST_CODE,
                notificationAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, INIT_MORNING_HOUR);
        calendar.set(Calendar.MINUTE,      0);
        calendar.set(Calendar.SECOND,      0);
        calendar.set(Calendar.MILLISECOND, 0);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                ALARM_INTERVAL, mAlarmIntent);
    }

    /**
     * Cancela o alarm manager para não chamar mais o sistema de notificação.
     *
     * @param context contexto da aplicação
     */
    private void stopNotificationAlarmManager( Context context ){
        Intent notificationAlarmIntent = new Intent(context, NotificationAlarmReceiver.class);
        mAlarmIntent  = PendingIntent.getBroadcast(context, INTENT_REQUEST_CODE,
                notificationAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT|Intent.FILL_IN_DATA);

        mAlarmManager.cancel(mAlarmIntent);
    }
}
