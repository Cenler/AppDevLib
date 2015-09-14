package com.icenler.lib.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.icenler.lib.R;
import com.icenler.lib.base.BaseApplication;
import com.icenler.lib.ui.fragment.TestFragment;
import com.icenler.lib.utils.manager.ToastManager;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout mContainer;
    @Bind(R.id.tabs_layout)
    TabLayout mTabs;
    @Bind(R.id.pager_view)
    ViewPager mPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private long exitTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * TODO 待办项：
     * 4、 处理带处理项
     * 5、 pinned-section-listview
     * 6、 SwipeMenuListView
     */
    private void init() {
        setSupportActionBar(mToolbar);
        TabPagerAdapter mAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTimeMillis > 2000) {
            exitTimeMillis = System.currentTimeMillis();
            ToastManager.show(this, getString(R.string.prompt_exit_app));
        } else {
            BaseApplication.getInstance().exitApp();
            super.onBackPressed();
        }
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> tabTitle = Arrays.asList("普天同庆", "日月同辉", "流光普照", "日月星城", "浩瀚星宇");

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            bundle.putString(TestFragment.TITLE, tabTitle.get(i));

            return TestFragment.newFragment(bundle);
        }

        @Override
        public int getCount() {
            return tabTitle.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle.get(position);
        }
    }

}
