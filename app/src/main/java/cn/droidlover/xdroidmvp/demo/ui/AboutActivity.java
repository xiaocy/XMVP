package cn.droidlover.xdroidmvp.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.demo.R;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * Created by wanglei on 2016/12/31.
 */

public class AboutActivity extends XActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    public void initData(Bundle savedInstanceState) {
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
        getSupportActionBar().setTitle("关于XDroidMvp");
    }

    @OnClick({R.id.iv_logo})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_logo:
                WebActivity.launch(context, "http://www.baidu.com", "度娘");
                break;
            default:
                break;
        }
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(AboutActivity.class)
                .data(new Bundle())
                .launch();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
