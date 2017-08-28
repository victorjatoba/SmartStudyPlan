package br.com.smartstudyplan.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Availability;

/**
 * Está classe é um dialog para o usuário escolher a disponibilidade de estudo para um período
 * no dia.
 */
public class AvailabilityDialog {

    /**
     * Mostra o dialog de disponibilidade.
     *
     * @param activity activity de onde o dialog é chamado
     * @param weekday o dia da semana selecionado
     * @param period o período selecionado
     * @param available a disponibilidade previamente selecionada
     * @param listener listener que irá tratar o resultado do que foi feito no dialog
     */
    public static void show( Activity activity, Availability.Weekday weekday,
                             Availability.Period period, Availability.Available available,
                             final AvailabilityDialogListener listener ){

        LayoutInflater inflater = activity.getLayoutInflater();

        LinearLayout layout = (LinearLayout) inflater.inflate( R.layout.dialog_availability, null );

        final TextView titleView = (TextView) layout.findViewById( R.id.title );
        ImageView noneView     = (ImageView) layout.findViewById( R.id.none );
        ImageView partTimeView = (ImageView) layout.findViewById( R.id.part_time );
        ImageView fullTimeView = (ImageView) layout.findViewById( R.id.full_time );

        if( available.getValue() == Availability.Available.NONE.getValue() ){
            noneView.setImageResource(R.drawable.circle_difficult_5);
            partTimeView.setImageResource(R.drawable.ring_availability_part_time);
            fullTimeView.setImageResource(R.drawable.ring_availability_full_time);
        } else if( available.getValue() == Availability.Available.PART_TIME.getValue() ){
            noneView.setImageResource(R.drawable.ring_availability_none);
            partTimeView.setImageResource(R.drawable.circle_difficult_3);
            fullTimeView.setImageResource(R.drawable.ring_availability_full_time);
        } else {
            noneView.setImageResource(R.drawable.ring_availability_none);
            partTimeView.setImageResource(R.drawable.ring_availability_part_time);
            fullTimeView.setImageResource(R.drawable.circle_difficult_1);
        }

        CharSequence title = activity.getResources().getStringArray(R.array.week_day)[weekday.getValue()]
                + " - "
                + activity.getResources().getStringArray(R.array.day_period)[period.getValue()];

        titleView.setText( title );

        AlertDialog.Builder builder = new AlertDialog.Builder( activity );
        builder.setView(layout);


        final AlertDialog dialog = builder.create();

        noneView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( listener != null ){
                    listener.returnValues( Availability.Available.NONE );
                }
                dialog.dismiss();
            }
        });

        partTimeView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( listener != null ){
                    listener.returnValues( Availability.Available.PART_TIME );
                }
                dialog.dismiss();
            }
        });

        fullTimeView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( listener != null ){
                    listener.returnValues( Availability.Available.FULL_TIME );
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
