package com.app.task10sep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.MalformedURLException;
import java.net.URL;

public class ConsentActivity extends AppCompatActivity {
    ConsentForm form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        concentStatus();
    }

    private void concentStatus(){
        String ID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ConsentInformation.getInstance(ConsentActivity.this).addTestDevice(ID);
        ConsentInformation.getInstance(ConsentActivity.this).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        ConsentInformation consentInformation = ConsentInformation.getInstance(ConsentActivity.this);
        String[] publisherIds = {"pub-4723180664092396"};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                if(consentInformation.getInstance(getBaseContext()).isRequestLocationInEeaOrUnknown()){
                    switch (consentStatus){
                        case UNKNOWN:
                            displayconsentform();
                            break;
                        case PERSONALIZED:
                            loadbannerads(true);
                            break;
                        case NON_PERSONALIZED:
                            loadbannerads(false);
                            break;
                    }
                }
                else {
                    Toast.makeText(ConsentActivity.this, "Not In EU Display Normal Ads", Toast.LENGTH_SHORT).show();
                    loadbannerads(true);
                    Intent goSplash = new Intent(getApplicationContext(),SplashActivity.class);
                    startActivity(goSplash);
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
            }
        });
    }

    private void displayconsentform(){
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL("https://www.your.com/privacyurl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(ConsentActivity.this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        form.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        switch (consentStatus){
                            case PERSONALIZED:
                                loadbannerads(true);
                                break;

                            case NON_PERSONALIZED:
                                loadbannerads(false);
                                break;
                        }
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();
    }

    private void loadbannerads(boolean isPersonlized){
        AdView adView= findViewById(R.id.adView);
        AdRequest adRequest;
        if(isPersonlized){
            adRequest= new AdRequest.Builder().build();
        }
        else {
            Bundle bundle= new Bundle();
            bundle.putString("npa","1");
            adRequest= new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class,bundle).build();
        }

        adView.loadAd(adRequest);

    }
}