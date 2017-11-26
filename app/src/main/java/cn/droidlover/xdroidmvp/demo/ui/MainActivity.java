package cn.droidlover.xdroidmvp.demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.demo.R;
import cn.droidlover.xdroidmvp.demo.view.CircleImageView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by wanglei on 2016/12/22.
 */

public class MainActivity extends XActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawer_view)
    LinearLayout drawerView;
    @BindView(R.id.civ_head)
    CircleImageView headerView;

    private ActionBarDrawerToggle drawerToggle;

    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"首页", "干货", "妹子"};

    XFragmentAdapter adapter;


    @Override
    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        fragmentList.clear();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(GanhuoFragment.newInstance());
        fragmentList.add(GirlFragment.newInstance());

        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);

        ILFactory.getLoader().loadNet(headerView, HEAD_URL, null);
    }

    private final String HEAD_URL = "http://wx.qlogo.cn/mmopen/vi_32/N1DKiaSbrcCeFibNX8ebQ9Zsw5jw9xNCvtuhDVxVjr9IMCcXQuFTww07DY3ufKxuU0LgGW0r6uavJl5VKV2uIb2g/0";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.menu_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_droid:
                AboutActivity.launch(context);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        // 修改侧滑菜单默认图标
        drawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                //drawerLayout.openDrawer(drawerView);
            }
        });

    }
}
