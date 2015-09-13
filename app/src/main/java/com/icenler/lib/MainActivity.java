package com.icenler.lib;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.icenler.lib.base.BaseActivity;
import com.icenler.lib.base.BaseApplication;
import com.icenler.lib.utils.manager.ToastManager;
import com.icenler.lib.view.dialog.SimpleProgressDialog;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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
     * 4、 处理带处理项
     * 5、 pinned-section-listview
     */
    private void init() {
        SimpleProgressDialog.show(getFragmentManager());
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
