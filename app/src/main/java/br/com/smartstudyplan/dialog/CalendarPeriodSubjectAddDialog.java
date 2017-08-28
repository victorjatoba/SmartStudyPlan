package br.com.smartstudyplan.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.adapter.CalendarSubjectEditSpinnerAdapter;
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.Subject;

/**
 * Esta classe é responsável por mostrar o dialog de matérias.
 */
public class CalendarPeriodSubjectAddDialog {

    private static CalendarSubject mCalendarSubject;

    /**
     * Mostra o dialog de matérias.
     *
     * @param activity activity de onde o dialog é chamado
     * @param titleResource recurso de string do título do dialog
     * @param adapter o adapter do spinner de matérias
     * @param calendarSubject matéria que terá os atributos mostrados no dialog
     * @param calendarSubjectList a lista de matérias e suas respectivas durações para aquele dia e período
     * @param listener listener que irá tratar o resultado do que foi feito no dialog
     */
    public static void show( Activity activity, final int titleResource, CalendarSubjectEditSpinnerAdapter adapter,
                             final CalendarSubject calendarSubject, final List<CalendarSubject> calendarSubjectList,
                             final CalendarPeriodSubjectAddListener listener ){

        mCalendarSubject = calendarSubject;

        final int subjectPosition = calendarSubjectList.indexOf(mCalendarSubject);
        float totalTime = 0;

        LayoutInflater inflater = activity.getLayoutInflater();

        RelativeLayout layout = (RelativeLayout) inflater.inflate( R.layout.dialog_calendar_period_subject, null );

        final TextView titleView = (TextView) layout.findViewById(R.id.calendar_period_subject_title);
        final Spinner subjectView  = (Spinner) layout.findViewById( R.id.calendar_period_subject_edit_subject_name );
        final SeekBar timeSeek = (SeekBar) layout.findViewById(R.id.calendar_period_subject_time);
        final TextView selectedTimeView = (TextView) layout.findViewById(R.id.calendar_period_subject_selected_time);
        final TextView maxTimeView = (TextView) layout.findViewById(R.id.calendar_period_subject_max_time);

        titleView.setText(titleResource);

        subjectView.setAdapter(adapter);
        if ( mCalendarSubject != null ){
            subjectView.setSelection( adapter.getContentList().indexOf(mCalendarSubject.getSubject()) );
        } else {
            mCalendarSubject = new CalendarSubject();
            mCalendarSubject.setDuration(0);
        }

        for( CalendarSubject cSubject : calendarSubjectList ){
            totalTime += cSubject.getDuration();
        }

        totalTime = 5 - totalTime + mCalendarSubject.getDuration();

        if( mCalendarSubject.getDuration() == 0 ){ // novo CalendarSubject
            selectedTimeView.setText( activity.getString(R.string.min_time) );
        } else { // editando CalendarSubject
            selectedTimeView.setText( formatTime(mCalendarSubject.getDuration()) );
        }

        maxTimeView.setText( formatTime(totalTime) );

        int max = (int) (totalTime/0.5 - 1);

        timeSeek.setMax(max);
        timeSeek.setProgress( (int) (mCalendarSubject.getDuration() / 0.5) - 1 );

        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = (float) ((progress*0.5)+0.5);
                selectedTimeView.setText( formatTime(value) );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* no code */ }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* no code */  }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setView(layout);

        builder.setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCalendarSubject.setSubject((Subject) subjectView.getSelectedItem());
                mCalendarSubject.setDuration( (float) ( (timeSeek.getProgress()*0.5) + 0.5 ) );
                if (titleResource == R.string.subject_add) {
                    calendarSubjectList.add(mCalendarSubject);
                } else {
                    calendarSubjectList.set(subjectPosition, mCalendarSubject);
                }

                if( listener != null ){
                    listener.returnValues(calendarSubjectList);
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton( R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();

    }

    /**
     * Formata o texto do tempo que será mostrado na tela.
     *
     * @param time tempo em horas
     * @return Texto formatado do tempo
     */
    private static String formatTime(float time){
        String formattedTime;
        if( time < 1.0f ) {
            formattedTime = String.format("%d%s", (int) (time * 60), "m");
        }
        else if( time % 1 == 0 ){
            formattedTime = (int)time + "h";
        }
        else {
            String partTime = String.format("%d%s", (int) (( time % 1 ) * 60), "m");
            formattedTime = (int)time + "h" + partTime;
        }
        return formattedTime;
    }
}
