package com.app.task10sep;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SplashActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        interstitialAd = new InterstitialAd(SplashActivity.this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        loadInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                showInterstitialAd();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Intent goMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goMain);
            }
            @Override
            public void onAdClosed() {
                Intent goMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goMain);
            }
        });
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    private void showInterstitialAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Intent goMain = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(goMain);
        }

    }
}