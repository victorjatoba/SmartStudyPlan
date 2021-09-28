package br.com.smartstudyplan.activity.settings;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.smartstudyplan.R;

/**
 * Esta tela é responsável por mostrar informações sobre o aplicativo.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_settings_about)
public class SettingsAboutActivity extends AppCompatActivity {

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
    }
}
