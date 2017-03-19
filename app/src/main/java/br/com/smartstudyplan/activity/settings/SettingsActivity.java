package br.com.smartstudyplan.activity.settings;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.smartstudyplan.R;

/**
 * Esta tela é responsável por mostra a lista de configurações disponível.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_settings )
public class SettingsActivity extends ActionBarActivity {

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
