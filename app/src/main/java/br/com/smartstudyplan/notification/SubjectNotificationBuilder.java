package br.com.smartstudyplan.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.activity.CalendarSubjectActivity_;
import br.com.smartstudyplan.bean.Availability;
import br.com.smartstudyplan.bean.CalendarSubject;

/**
 * Está classe é responsável por gerar a notificação.
 */
public class SubjectNotificationBuilder {

    private static final int INTENT_REQUEST_CODE = 99;
    private static final int NOTIFICATION_ID     = 201;

    /**
     * Gera a notificação a partir dos dados recebidos.
     *
     * @param context contexto da aplicação
     * @param subjectList Lista de matérias de um período
     * @param period o periodo das matérias
     */
    public static void createNotification( Context context, List<CalendarSubject> subjectList, Availability.Period period){
        String message = createNotificationMessage( context, subjectList, period );

        if( message == null ){
            return ;
        }

        Notification notification  = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification_1)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentIntent(createPendingIntent(context))
                .setAutoCancel(true)
                .build();

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyMgr.notify( NOTIFICATION_ID, notification );
    }

    /**
     * Gera o texto que será exibido na notificação.
     *
     * @param context contexto da aplicação
     * @param subjectList Lista de matérias de um período
     * @param period o periodo das matérias
     * @return texto a ser exibido na notificação
     */
    private static String createNotificationMessage( Context context, List<CalendarSubject> subjectList, Availability.Period period ){
        StringBuilder sb = new StringBuilder(context.getString(R.string.notification_start_message));
        sb.append(" ");

        if( ( subjectList != null  ) && ( subjectList.isEmpty() == false )){
            for( int i=0; i < subjectList.size() ; i++ ){
                sb.append( subjectList.get(i).getSubject().getName());
                if( ( subjectList.size() >= 2 ) && ( i == ( subjectList.size() - 2 ) ) ){
                    sb.append(" ");
                    sb.append(context.getString(R.string.notification_connective));
                }
                else if( i < ( subjectList.size() - 1 ) ){
                    sb.append(",");
                }
                sb.append(" ");
            }
        }
        else {
            return null;
        }

        switch( period ){
            case MORNING:
                sb.append(context.getString(R.string.notification_morning));
                break;
            case AFTERNOON:
                sb.append(context.getString(R.string.notification_afternoon));
                break;
            case NIGHT:
                sb.append(context.getString(R.string.notification_night));
                break;
            default:
                sb.append(context.getString(R.string.notification_today));
        }

        return sb.toString();
    }

    /**
     * Cria o <code>PendingIntent</code> que é responsável por abrir o aplicativo quando o usuário
     * clicar na notificação.
     *
     * @param context contexto da aplicação
     * @return o <code>PendingIntent</code>
     */
    private static PendingIntent createPendingIntent( Context context ){
        Intent resultIntent = new Intent(context, CalendarSubjectActivity_.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(CalendarSubjectActivity_.class);
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(INTENT_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
