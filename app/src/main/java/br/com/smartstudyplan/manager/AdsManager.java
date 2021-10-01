package br.com.smartstudyplan.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

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

            MobileAds.initialize(context, initializationStatus -> {
            });

            adView.setVisibility(View.VISIBLE);

            adView.loadAd(new AdRequest.Builder().build());

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
