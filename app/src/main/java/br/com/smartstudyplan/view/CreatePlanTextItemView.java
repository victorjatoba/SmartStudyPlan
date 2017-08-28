package br.com.smartstudyplan.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;

/**
 * Esta classe é responsável por encapsular uma view da informações exibidas quando está sendo
 * gerado o plano de estudo.
 */
@EViewGroup(R.layout.item_create_plan_text)
public class CreatePlanTextItemView extends RelativeLayout{

    @ViewById ImageView   textIcon;
    @ViewById ProgressBar textProgressBar;
    @ViewById TextView    text;

    @TargetApi(21)
    public CreatePlanTextItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CreatePlanTextItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CreatePlanTextItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CreatePlanTextItemView(Context context) {
        super(context);
    }

    /**
     * Recebe as informações a serem exibidas na <code>View</code>.
     *
     * @param textResource recurso de texto a ser exibido
     */
    public void bindView(int textResource){
        this.text.setText( textResource );
    }

    /**
     * Inicia a contagem do tempo para a atualização da <code>View</code>.
     *
     * @param time tempo em milissegundos
     */
    public void start( int time ){
        this.postDelayed( new Runnable() {
            @Override
            public void run() {
                showComplete();
            }
        }, time - 1000 );
    }

    /**
     * Atualiza o ícone da <code>View</code>.
     */
    public void showComplete( ){
        textProgressBar.setVisibility(View.GONE);
        textIcon.setVisibility(View.VISIBLE);
    }
}
