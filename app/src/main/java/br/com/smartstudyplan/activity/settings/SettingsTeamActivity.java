package br.com.smartstudyplan.activity.settings;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;

/**
 * Esta tela é responsável por mostrar informações sobre o time de desenvolvimento do aplicativo.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_settings_team )
public class SettingsTeamActivity extends AppCompatActivity {

    @ViewById TextView content;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

    }
}
