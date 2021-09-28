package br.com.smartstudyplan.activity.settings;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.smartstudyplan.R;

/**
 * Esta tela é responsável por mostra a lista de configurações disponível.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_settings )
public class SettingsActivity extends AppCompatActivity {

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        getFragmentManager().beginTransaction()
                .replace( R.id.container, new SettingsPreferenceFragment() )
                .commit();
    }

}
