package com.mobi.samsung.manausmobi.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.mobi.samsung.manausmobi.R;
import com.mobi.samsung.manausmobi.fragments.AppIntroSlider;

public class IntroActivity extends AppIntro {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroSlider.newInstance(R.layout.fragment_intro_mobility));
        addSlide(AppIntroSlider.newInstance(R.layout.fragment_intro_safety));
        addSlide(AppIntroSlider.newInstance(R.layout.fragment_intro_shared));
        addSlide(AppIntroSlider.newInstance(R.layout.fragment_intro_dashboard));

        showStatusBar(true);
        showSkipButton(true);

        setSkipText(getString(R.string.intro_skip));
        setDoneText(getString(R.string.intro_done));

        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
