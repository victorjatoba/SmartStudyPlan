package br.com.smartstudyplan.activity;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;

/**
 * Esta classe é a tela que mostra as dicas para cada uma das outras telas do aplicativo.
 */
@SuppressLint("Registered")
@EActivity(R.layout.activity_help)
public class HelpActivity extends AppCompatActivity {

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
