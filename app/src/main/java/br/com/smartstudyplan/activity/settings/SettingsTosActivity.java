package br.com.smartstudyplan.activity.settings;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.smartstudyplan.R;

/**
 * Esta tela é responsável por mostrar os termos de uso do aplicativo.
 */
@SuppressLint("Registered")
@EActivity( R.layout.activity_settings_tos )
public class SettingsTosActivity extends AppCompatActivity {

    @ViewById WebView webview;

    /**
     * Primeiro método chamado ao ser iniciada a Activity.
     */
    @AfterViews
    void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        webview.loadUrl( getString(R.string.settings_tos_url) );
    }
}
