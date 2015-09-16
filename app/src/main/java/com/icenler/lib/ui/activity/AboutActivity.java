package com.icenler.lib.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.icenler.lib.R;
import com.icenler.lib.utils.helper.ActivityHelper;
import com.icenler.lib.view.swiplayout.SwipeBackCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends SwipeBackCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mToolbarLayout;

    public static void startMe(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        mToolbarLayout.setTitle("关于");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_share:
                onClickShare();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickShare() {
        ActivityHelper.doShare(this, "分享", "https://github.com/Cenler/AppDevLib.git");
    }

}
