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
import br.com.smartstudyplan.bean.CalendarSubject;
import br.com.smartstudyplan.bean.Subject;

/**
 * Esta classe é responsável por encapsular uma view da lista de matérias.
 */
@EViewGroup(R.layout.calendar_subject_item)
public class CalendarSubjectItemView extends LinearLayout {

    @ViewById ImageView icon;
    @ViewById TextView  name;
    @ViewById TextView  time;

    @TargetApi(21)
    public CalendarSubjectItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CalendarSubjectItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalendarSubjectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarSubjectItemView(Context context) {
        super(context);
    }

    /**
     * Recebe as informações a serem exibidas na <code>View</code>.
     *
     * @param calendarSubject o obejeto que contém as informações
     * @param density densidade da tela
     * @param isFirstItem determina se é o primeiro item da lista
     */
    public void bindView(CalendarSubject calendarSubject, float density, boolean isFirstItem){

        Subject subject = calendarSubject.getSubject();

        Drawable iconDrawable = applyColorFilter(
                getContext().getResources().getDrawable( Subject.iconIds[ subject.getIcon() ] ));

        icon.setImageDrawable( iconDrawable );
        name.setText(subject.getName());

        if( calendarSubject.getDuration() < 1.0f ) {
            time.setText(String.format("%d%s", (int) (calendarSubject.getDuration() * 60), "m"));
        }
        else if( calendarSubject.getDuration() % 1 == 0 ){
            time.setText((int)calendarSubject.getDuration() + "h");
        }
        else {
            String partTime =  String.format("%d%s", (int) (( calendarSubject.getDuration() % 1 ) * 60), "m");
            time.setText((int)calendarSubject.getDuration() + "h" + partTime);
        }

        int difficultValue = ( subject.getDifficult() + subject.getWeight() ) / 2 ;

        if( difficultValue < 20 ){
            setBackgroundResource(R.color.difficult_1);
        }
        else if( difficultValue < 40 ){
            setBackgroundResource(R.color.difficult_2);
        }
        else if( difficultValue < 60 ){
            setBackgroundResource(R.color.difficult_3);
        }
        else if( difficultValue < 80 ){
            setBackgroundResource(R.color.difficult_4);
        }
        else{
            setBackgroundResource(R.color.difficult_5);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (int)( ( 44 * density ) + ( 20 * calendarSubject.getDuration() ) ) );


        params.setMargins( 0, isFirstItem ? 0 : (int)( 1 * density ), 0, 0 );


        setLayoutParams( params );
    }

    private static Drawable applyColorFilter(Drawable drawable) {
        Drawable mutate = drawable.mutate();
        mutate.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        return mutate;
    }
}
