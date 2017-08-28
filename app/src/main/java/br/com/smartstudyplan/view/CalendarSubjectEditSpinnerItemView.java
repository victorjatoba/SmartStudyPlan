package br.com.smartstudyplan.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Subject;

/**
 * Esta classe é responsável por encapsular uma view da lista de matérias no plano de estudo que
 * está sendo editado.
 */
@EViewGroup(R.layout.calendar_subject_edit_spinner_item)
public class CalendarSubjectEditSpinnerItemView extends LinearLayout {

    @ViewById ImageView spinnerSubjectEditIcon;
    @ViewById TextView spinnerSubjectEditName;

    @TargetApi(21)
    public CalendarSubjectEditSpinnerItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CalendarSubjectEditSpinnerItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalendarSubjectEditSpinnerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarSubjectEditSpinnerItemView(Context context) {
        super(context);
    }

    /**
     * Recebe as informações a serem exibidas na <code>View</code>.
     *
     * @param subject o obejeto que contém as informações
     */
    public void bindView(Subject subject){

        Drawable iconDrawable = applyColorFilter(
                getContext().getResources().getDrawable( Subject.iconIds[ subject.getIcon() ] ));

        spinnerSubjectEditIcon.setImageDrawable(iconDrawable);
        spinnerSubjectEditName.setText(subject.getName());

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

    }

    private static Drawable applyColorFilter(Drawable drawable) {
        Drawable mutate = drawable.mutate();
        mutate.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        return mutate;
    }
}
