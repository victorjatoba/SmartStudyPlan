package br.com.smartstudyplan.activity.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import br.com.smartstudyplan.R;

/**
 * Parte da tela que contém a lista de configurações do aplicativo.
 */
public class SettingsPreferenceFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    private static final String MAILTO = "mailto";

    private static final String MARKET_URI      = "market://details?id=br.com.smartstudyplan";
    private static final String MARKET_HTTP_URI = "http://play.google.com/store/apps/details?id=br.com.smartstudyplan";

    private static final String ID_CONTACT  = "id_contact";
    private static final String ID_RATE     = "id_rate";
    private static final String ID_TOS      = "id_tos";
    private static final String ID_ABOUT    = "id_about";
    private static final String ID_TEAM     = "id_team";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_settings);

        findPreference(ID_CONTACT).setOnPreferenceClickListener( this );
        findPreference(ID_RATE).setOnPreferenceClickListener( this );
        findPreference(ID_TOS).setOnPreferenceClickListener( this );
        findPreference(ID_ABOUT).setOnPreferenceClickListener( this );
        findPreference(ID_TEAM).setOnPreferenceClickListener( this );
    }

    /**
     * Este método é chamado quando algum item da lista de configurações é clicado.
     *
     * @param preference Item que foi clicado.
     * @return true se o item clicado foi interceptado, false caso contrário.
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        boolean returnValue = false;

        switch(preference.getKey()){
            case ID_CONTACT:
                sendEmail();
                returnValue = true;
                break;
            case ID_RATE:
                rateApplication();
                returnValue = true;
                break;
            case ID_TOS:
                SettingsTosActivity_.intent(getActivity()).start();
                returnValue = true;
                break;
            case ID_ABOUT:
                SettingsAboutActivity_.intent(getActivity()).start();
                returnValue = true;
                break;
            case ID_TEAM:
                SettingsTeamActivity_.intent(getActivity()).start();
                returnValue = true;
                break;
        }
        return returnValue;
    }

    /**
     * Este método é responsável por configurar e chamar os aplicativos de e-mail
     * configurado no aparelho.
     */
    private void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                MAILTO, getString(R.string.settings_email), null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_email_subject));

        startActivity(Intent.createChooser(intent, getString(R.string.settings_email_select_app)));
    }

    /**
     * Este método é responsável por encaminhar o usuário para a loja de aplicativos Play Store,
     * na página do aplicativo.
     */
    private void rateApplication(){
        Uri uri = Uri.parse(MARKET_URI);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_HTTP_URI)));
        }
    }
}
