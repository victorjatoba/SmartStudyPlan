package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;

/**
 * Created by Victor on 17/11/2014.
 */

/**
 * Esta classe é a tela que mostra as dicas para cada uma das outras telas do aplicativo.
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_help)
public class HelpActivity extends Activity {

    @ViewById TextView helpTitle;
    @ViewById DocumentView helpText;

    @Extra String title;
    @Extra String text;

    /**
     * Primeiro método chamado ao ser iniciada a Activity. Coloca o título e o texto da Ajuda.
     */
    @AfterViews
    void init(){
        helpTitle.setText(title);
        helpText.setText(text);
    }

}
