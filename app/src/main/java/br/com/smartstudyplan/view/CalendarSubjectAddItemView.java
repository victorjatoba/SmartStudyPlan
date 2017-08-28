package br.com.smartstudyplan.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;

/**
 * Esta classe é responsável por encapsular uma view da lista de matérias no plano de estudo.
 */
@EViewGroup(R.layout.calendar_subject_add_item)
public class CalendarSubjectAddItemView extends LinearLayout {

    @ViewById ImageView calendarSubjectAddIcon;
    @ViewById TextView  calendarSubjectAddTime;

    @TargetApi(21)
    public CalendarSubjectAddItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CalendarSubjectAddItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalendarSubjectAddItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarSubjectAddItemView(Context context) {
        super(context);
    }

    /**
     * Recebe as informações a serem exibidas na <code>View</code>.
     *
     * @param density densidade da tela
     * @param availableTime tempo disponível
     */
    public void bindView(float density, float availableTime){

        if( availableTime < 1.0f ) {
            calendarSubjectAddTime.setText(String.format("%d%s", (int) (availableTime * 60), "m " + getContext().getString(R.string.time_available_plural)));
        }
        else if (availableTime == 1.0f) {
            calendarSubjectAddTime.setText((int)availableTime + "h " + getContext().getString(R.string.time_available_singular));
        }
        else if( availableTime % 1 == 0 ){
            calendarSubjectAddTime.setText((int)availableTime + "h " + getContext().getString(R.string.time_available_plural));
        }
        else {
            String partTime =  String.format("%d%s", (int) ((availableTime % 1) * 60), "m");
            calendarSubjectAddTime.setText((int)availableTime + "h" + partTime + " " + getContext().getString(R.string.time_available_plural));
        }

        setBackgroundResource(R.color.gray_dark);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) ( 64 * density ) );


        params.setMargins( 0, (int)( 1 * density ), 0, 0 );


        setLayoutParams( params );
    }
}