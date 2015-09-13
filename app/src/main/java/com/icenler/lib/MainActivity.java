package com.icenler.lib;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.icenler.lib.base.BaseActivity;
import com.icenler.lib.base.BaseApplication;
import com.icenler.lib.utils.manager.ToastManager;
import com.icenler.lib.view.AutoScrollViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.auto_pager_view)
    AutoScrollViewPager mViewPager;

    private long firshTimeOfClickBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    /**
     * TODO 工作安排：
     * 2、 导入全球特卖 头部广告布局
     * 3、 引入 Dialog
     * 4、 处理带处理项
     */
    private void init() {
        final int[] colors = {R.color.color_green_highlight,
                R.color.color_red_assist,
                R.color.color_rose};
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = new View(MainActivity.this);
                view.setBackgroundResource(colors[position%3]);

                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mViewPager.setInterval(2000);
        mViewPager.startAutoScroll();
        mViewPager.startAutoScroll(2500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firshTimeOfClickBackPressed > 2000) {
            firshTimeOfClickBackPressed = System.currentTimeMillis();
            ToastManager.show(this, getString(R.string.prompt_exit_app));
        } else {
            BaseApplication.getInstance().exitApp();
            super.onBackPressed();
        }
    }

}
