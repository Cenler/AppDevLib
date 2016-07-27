package com.icenler.lib.feature.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.icenler.lib.R;
import com.icenler.lib.utils.AppUtil;
import com.icenler.lib.utils.helper.ActivityHelper;
import com.icenler.lib.view.swiplayout.SwipeBackCompatActivity;

import butterknife.BindView;

public class AboutActivity extends SwipeBackCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.version_tv)
    TextView appVersion;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    public static void startMe(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int doGetLayoutResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void doInit() {
        mToolbarLayout.setTitle(getString(R.string.about_app));
        appVersion.setText(String.format("Version: %s", AppUtil.getAppVersionName(this)));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getDefaultStatusBarTintColor() {
        return android.R.color.transparent;
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
