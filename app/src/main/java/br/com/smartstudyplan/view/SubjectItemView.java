package br.com.smartstudyplan.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.bean.Subject;

/**
 * Esta classe é responsável por encapsular uma view da lista de matérias.
 */
@EViewGroup( R.layout.list_item_subject )
public class SubjectItemView extends RelativeLayout {

    @ViewById ImageView subjectIcon;
    @ViewById TextView  subjectName;
    @ViewById ImageView weightIcon;
    @ViewById ImageView difficultIcon;

    public SubjectItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SubjectItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubjectItemView(Context context) {
        super(context);
    }

    /**
     * Recebe as informações a serem exibidas na <code>View</code>.
     *
     * @param subject o obejeto que contém as informações
     * @param position a posição onde ele se encontra na lista
     * @param isSelected determina se a <code>View</code> está selecionada
     */
    public void bindView( Subject subject, int position, boolean isSelected ){
        clear();

        if( TextUtils.isEmpty( subject.getName() ) == false ){
            subjectName.setText( subject.getName() );
        }

        subjectIcon.setImageResource(Subject.iconIds[subject.getIcon()]);
        weightIcon.setImageResource(getIconResource(subject.getWeight()));
        difficultIcon.setImageResource(getIconResource(subject.getDifficult()));

        if( ( position % 2 ) == 0 ){
            setBackgroundResource( isSelected ?
                    R.drawable.bg_selected_list_item : R.drawable.bg_item_white );
        }
        else{
            setBackgroundResource( isSelected ?
                    R.drawable.bg_selected_gray_list_item : R.drawable.bg_item_gray );
        }
    }

    /**
     * Retorna o ícone certo a depender do peso/dificuldade passado.
     *
     * @param value valor do peso/dificuldade
     * @return recurso de drawable equivalente ao valor
     */
    private int getIconResource(int value){
        if( value >= 0 && value < 20 ){
            return R.drawable.circle_difficult_1;
        } else if( value >= 20 && value < 40 ){
            return R.drawable.circle_difficult_2;
        } else if ( value >= 40 && value < 60 ){
            return R.drawable.circle_difficult_3;
        } else if ( value >= 60 && value < 80){
            return R.drawable.circle_difficult_4;
        } else if ( value >= 80 && value <= 100 ){
            return R.drawable.circle_difficult_5;
        }
        return 0;
    }

    /**
     * Define a <code>View</code> para o estado inicial.
     */
    private void clear(){
        subjectIcon.setImageBitmap( null );

        subjectName.setText( "" );

        weightIcon.setImageBitmap(null);
        difficultIcon.setImageBitmap( null );
    }
}
