package com.icenler.lib.ui;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.icenler.lib.R;
import com.icenler.lib.ui.activity.AboutActivity;
import com.icenler.lib.ui.base.BaseApplication;
import com.icenler.lib.ui.base.BaseCompatActivity;
import com.icenler.lib.ui.fragment.TestFragment;
import com.icenler.lib.utils.manager.SnackbarManager;
import com.icenler.lib.utils.manager.ToastManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseCompatActivity {


    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
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
        setOverFlowShowingAlways();
        ButterKnife.bind(this);

        init();
    }

    boolean isGrey = false;

    @OnClick({R.id.float_action_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.float_action_btn:
                SnackbarManager.show(mContainer, "向右滑动移除");
                View root = findViewById(R.id.drawer_layout);
                if (!isGrey) {
                    isGrey = !isGrey;
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    Paint paint = new Paint();
                    paint.setColorFilter(new ColorMatrixColorFilter(matrix));

                    root.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
                } else {
                    isGrey = !isGrey;
                    root.setLayerType(View.LAYER_TYPE_NONE, null);
                }
                break;
        }
    }

    private void setOverFlowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field field = config.getClass().getDeclaredField("sHasPermanentMenuKey");
            field.setAccessible(true);
            field.set(config, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // 显示 OverFlow 中 Item 的图标
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
            case R.id.action_about:
                AboutActivity.startMe(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * - http://jcodecraeer.com/plus/list.php?tid=31&codecategory=22000
     * - http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0822/3342.html
     * - http://segmentfault.com/a/1190000002873657
     */
    private void init() {
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayUseLogoEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.action_open_menu, R.string.action_close_menu);
        mDrawerToggle.syncState();
        mDrawerToggle.setToolbarNavigationClickListener(null);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        TabPagerAdapter mAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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

        private List<String> tabTitle = Arrays.asList("iBox", "Tools", "View");

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

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     *                  Palette.Builder from = Palette.from(bitmap);
     *                  Palette generate = from.generate();
     *                  generate.getDarkMutedSwatch().getRgb();
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;

        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));

        return Color.rgb(red, green, blue);
    }

}
