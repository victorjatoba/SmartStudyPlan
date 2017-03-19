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
 * Esta classe é responsável por encapsular uma view da lista de matérias no plano de estudo que
 * está sendo editado.
 */
@EViewGroup(R.layout.calendar_subject_edit_item)
public class CalendarSubjectEditItemView extends LinearLayout {

    @ViewById ImageView calendarSubjectEditIcon;
    @ViewById TextView  calendarSubjectEditName;
    @ViewById TextView  calendarSubjectEditTime;

    private int itemColorResource;
    private int subjectIcon;

    @TargetApi(21)
    public CalendarSubjectEditItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CalendarSubjectEditItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalendarSubjectEditItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarSubjectEditItemView(Context context) {
        super(context);
    }

    public void bindView(CalendarSubject calendarSubject, float density, boolean isFirstItem){

        Subject subject = calendarSubject.getSubject();

        subjectIcon = subject.getIcon();

        Drawable iconDrawable = applyColorFilter(
                getContext().getResources().getDrawable( Subject.iconIds[ subjectIcon ] ));

        calendarSubjectEditIcon.setImageDrawable(iconDrawable);
        calendarSubjectEditName.setText(subject.getName());

        if( calendarSubject.getDuration() < 1.0f ) {
            calendarSubjectEditTime.setText(String.format("%d%s", (int) (calendarSubject.getDuration() * 60), "m"));
        }
        else if( calendarSubject.getDuration() % 1 == 0 ){
            calendarSubjectEditTime.setText((int)calendarSubject.getDuration() + "h");
        }
        else {
            String partTime =  String.format("%d%s", (int) (( calendarSubject.getDuration() % 1 ) * 60), "m");
            calendarSubjectEditTime.setText((int)calendarSubject.getDuration() + "h" + partTime);
        }

        int difficultValue = ( subject.getDifficult() + subject.getWeight() ) / 2 ;

        if( difficultValue < 20 ){
            itemColorResource = R.color.difficult_1;
        }
        else if( difficultValue < 40 ){
            itemColorResource = R.color.difficult_2;
        }
        else if( difficultValue < 60 ){
            itemColorResource = R.color.difficult_3;
        }
        else if( difficultValue < 80 ){
            itemColorResource = R.color.difficult_4;
        }
        else{
            itemColorResource = R.color.difficult_5;
        }

        setBackgroundResource(itemColorResource);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) ( 64 * density ) );

        params.setMargins( 0, isFirstItem ? 0 : (int)( 1 * density ), 0, 0 );

        setLayoutParams( params );
    }

    public void setEditSelected( boolean isSelected ){
        if( isSelected ){
            calendarSubjectEditIcon.setImageResource( Subject.iconIds[ subjectIcon ] );

            setBackgroundColor( Color.TRANSPARENT );

            calendarSubjectEditName.setTextColor( Color.BLACK );
            calendarSubjectEditTime.setTextColor( Color.BLACK );

        }
        else{
            Drawable iconDrawable = applyColorFilter(
                    getContext().getResources().getDrawable( Subject.iconIds[ subjectIcon ] ));

            calendarSubjectEditIcon.setImageDrawable(iconDrawable);

            setBackgroundResource( itemColorResource );

            calendarSubjectEditName.setTextColor( Color.WHITE );
            calendarSubjectEditTime.setTextColor( Color.WHITE );
        }
    }

    private static Drawable applyColorFilter(Drawable drawable) {
        Drawable mutate = drawable.mutate();
        mutate.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        return mutate;
    }
}