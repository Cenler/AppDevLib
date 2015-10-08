package com.icenler.lib.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.icenler.lib.R;
import com.icenler.lib.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends BaseActivity {

    @Bind(R.id.v1)
    View v1;
    @Bind(R.id.v2)
    View v2;
    @Bind(R.id.v3)
    View v3;
    @Bind(R.id.v4)
    View v4;
    @Bind(R.id.root_view)
    ViewGroup mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.root_view)
    public void onClick(View v) {
        // new Fade()
        // new Slide()
        // new Explode()
        // TransitionManager.beginDelayedTransition(mRootView, new Explode());
        // toggleVisibility(v1, v2, v3, v4);
//        Pair<View, String> pairs = new Pair<View, String>(v1, "v1");
//        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, pairs).toBundle();
//        startActivity(new Intent(this, MainActivity.class), bundle);
    }

    private void toggleVisibility(View... views) {
        for (View view : views) {
            boolean isVisible = view.getVisibility() == View.VISIBLE;
            view.setVisibility(isVisible ? View.INVISIBLE : View.VISIBLE);
        }
    }

}
