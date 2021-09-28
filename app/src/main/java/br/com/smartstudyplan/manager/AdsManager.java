package br.com.smartstudyplan.manager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import br.com.smartstudyplan.R;
import br.com.smartstudyplan.preferences.UserPreferences_;

@EBean( scope = EBean.Scope.Singleton )
public class AdsManager {

    @Pref
    UserPreferences_ preferences;

    /**
     * Verifica se o usuário é pago ou não, caso necessário mostra a propaganda
     * @param context
     * @param adView
     * @return true se for necessário verificar as compras do usuário, false caso contrário
     */
    public int showAdsIfNecessary(Context context, AdView adView) {
        int payStatus = preferences.payUserStatus().get();
        if (payStatus == 0) {
            //MobileAds.initialize(context, context.getString(R.string.admob_id));
            MobileAds.initialize(context, initializationStatus -> { });

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(adRequest);
        } else {
            adView.setVisibility(View.GONE);
            adView.destroy();
        }

        return payStatus;
    }

    public void updatePurchase(boolean hasPurchase, Context context, AdView adView) {
        Log.d("AdsManager", "updatePurchase");
        preferences.payUserStatus().put(hasPurchase ? 1 : 0);
        showAdsIfNecessary(context, adView);
    }

}
