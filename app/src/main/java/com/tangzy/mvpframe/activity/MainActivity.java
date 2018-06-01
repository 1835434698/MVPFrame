package com.tangzy.mvpframe.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.activity.base.BaseActivity;
import com.tangzy.mvpframe.fragment.FirstFragment;
import com.tangzy.mvpframe.fragment.FiveFragment;
import com.tangzy.mvpframe.fragment.MineFragment;
import com.tangzy.mvpframe.fragment.SecondFragment;
import com.tangzy.mvpframe.fragment.ThirdFragment;
import com.tangzy.mvpframe.listener.NoDoubleClickListener;
import com.tangzy.mvpframe.util.Logger;
import com.tangzy.mvpframe.util.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab1)
    RelativeLayout tab1;
    @BindView(R.id.tab2)
    RelativeLayout tab2;
    @BindView(R.id.tab3)
    RelativeLayout tab3;
    @BindView(R.id.tab4)
    RelativeLayout tab4;
    @BindView(R.id.tab5)
    RelativeLayout tab5;
    @BindView(R.id.tab1_image)
    ImageView tab1_image;
    @BindView(R.id.tab2_image)
    ImageView tab2_image;
    @BindView(R.id.tab3_image)
    ImageView tab3_image;
    @BindView(R.id.tab4_image)
    ImageView tab4_image;
    @BindView(R.id.tab5_image)
    ImageView tab5_image;
    @BindView(R.id.tab1_text)
    TextView tab1_text;
    @BindView(R.id.tab2_text)
    TextView tab2_text;
    @BindView(R.id.tab3_text)
    TextView tab3_text;
    @BindView(R.id.tab4_text)
    TextView tab4_text;
    @BindView(R.id.tab5_text)
    TextView tab5_text;
    @BindView(R.id.bottom_layout)
    LinearLayout bottom_layout;

    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideTitle();
        initListener();
        setTabView(0);
    }

    private NoDoubleClickListener noDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()){
                case R.id.tab1:
                    setTabView(0);
                    break;
                case R.id.tab2:
                    setTabView(1);
                    break;
                case R.id.tab3:
                    setTabView(2);
                    break;
                case R.id.tab4:
                    setTabView(3);
                    break;
                case R.id.tab5:
                    setTabView(4);
                    break;
            }

        }
    };

    private void initListener() {
        tab1.setOnClickListener(noDoubleClickListener);
        tab2.setOnClickListener(noDoubleClickListener);
        tab3.setOnClickListener(noDoubleClickListener);
        tab4.setOnClickListener(noDoubleClickListener);
        tab5.setOnClickListener(noDoubleClickListener);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Utils.exitApp(this);
    }

    public void setTabView(int tab) {//选择当前处于显示状态的tab
        Logger.d(TAG, "setTab = "+tab);
        // TODO Auto-generated method stub
        if (currentIndex == tab) {
            return;
        }
        switch (tab) {
            case 0://
                setClientTabView(tab);
                setClientFragment(tab, 0);
                break;

            case 1://
                setClientTabView(tab);
                setClientFragment(tab, 1);
                break;

            case 2://
                setClientTabView(tab);
                setClientFragment(tab, 2);
                break;

            case 3://
                setClientTabView(tab);
                setClientFragment(tab, 3);
                break;

            case 4://
                setClientTabView(tab);
                setClientFragment(tab, 4);
                break;
        }
    }
    public void setClientFragment(int tab, int tag) {
        if (currentIndex == tab) {
            return;
        }
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment tab0 = fragmentManager.findFragmentByTag("tab0");
        Fragment tab1 = fragmentManager.findFragmentByTag("tab1");
        Fragment tab2 =  fragmentManager.findFragmentByTag("tab2");
        Fragment tab3 = fragmentManager.findFragmentByTag("tab3");
        Fragment tab4 = fragmentManager.findFragmentByTag("tab4");
        switch (tab) {
            case 0:
                if (tab1 != null) {
                    fragmentTransaction.hide(tab1);
                }
                if (tab2 != null) {
                    fragmentTransaction.hide(tab2);
                }
                if (tab3 != null) {
                    fragmentTransaction.hide(tab3);
                }
                if (tab4 != null) {
                    fragmentTransaction.hide(tab4);
                }
                if (tab0 == null) {
                    tab0 = new FirstFragment();
                    fragmentTransaction.add(R.id.fragment_content, tab0, "tab0");
                } else {
                    fragmentTransaction.show(tab0);
                }
                break;
            case 1:
                if (tab0 != null) {
                    fragmentTransaction.hide(tab0);
                }
                if (tab2 != null) {
                    fragmentTransaction.hide(tab2);
                }
                if (tab3 != null) {
                    fragmentTransaction.hide(tab3);
                }
                if (tab4 != null) {
                    fragmentTransaction.hide(tab4);
                }
                if (tab1 == null) {
                    tab1 = new SecondFragment();
                    fragmentTransaction.add(R.id.fragment_content, tab1, "tab1");
                } else {
                    fragmentTransaction.show(tab1);
                }
                break;
            case 2:
                if (tab0 != null) {
                    fragmentTransaction.hide(tab0);
                }
                if (tab1 != null) {
                    fragmentTransaction.hide(tab1);
                }
                if (tab3 != null) {
                    fragmentTransaction.hide(tab3);
                }
                if (tab4 != null) {
                    fragmentTransaction.hide(tab4);
                }
                if (tab2 == null) {
                    tab2 = new ThirdFragment();
                    fragmentTransaction.add(R.id.fragment_content, tab2, "tab2");
                } else {
                    fragmentTransaction.show(tab2);
                }
                break;
            case 3:
                if (tab0 != null) {
                    fragmentTransaction.hide(tab0);
                }
                if (tab1 != null) {
                    fragmentTransaction.hide(tab1);
                }
                if (tab2 != null) {
                    fragmentTransaction.hide(tab2);
                }
                if (tab4 != null) {
                    fragmentTransaction.hide(tab4);
                }
                if (tab3 == null) {
                    tab3 = new MineFragment();
                    fragmentTransaction.add(R.id.fragment_content, tab3, "tab3");
                } else {
                    fragmentTransaction.show(tab3);
                }
                break;
            case 4:
                if (tab0 != null) {
                    fragmentTransaction.hide(tab0);
                }
                if (tab1 != null) {
                    fragmentTransaction.hide(tab1);
                }
                if (tab2 != null) {
                    fragmentTransaction.hide(tab2);
                }
                if (tab3 != null) {
                    fragmentTransaction.hide(tab3);
                }
                if (tab4 == null) {
                    tab4 = new FiveFragment();
                    fragmentTransaction.add(R.id.fragment_content, tab4, "tab4");
                } else {
                    fragmentTransaction.show(tab4);
                }
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
        currentIndex = tab;
    }

    public void setClientTabView(int tab) {//处理客户经理角色时下导航字体和图标状态
        switch (tab) {
            case 0:
                tab1_image.setSelected(true);
                tab2_image.setSelected(false);
                tab3_image.setSelected(false);
                tab4_image.setSelected(false);
                tab5_image.setSelected(false);
//                tab1_text.setTextColor(getResources().getColor(R.color.color_2772FF));
//                tab2_text.setTextColor(getResources().getColor(R.color.color_515151));
                break;
            case 1:
                tab1_image.setSelected(false);
                tab2_image.setSelected(true);
                tab3_image.setSelected(false);
                tab4_image.setSelected(false);
                tab5_image.setSelected(false);
                break;
            case 2:
                tab1_image.setSelected(false);
                tab2_image.setSelected(false);
                tab3_image.setSelected(true);
                tab4_image.setSelected(false);
                tab5_image.setSelected(false);
                break;
            case 3:
                tab1_image.setSelected(false);
                tab2_image.setSelected(false);
                tab3_image.setSelected(false);
                tab4_image.setSelected(true);
                tab5_image.setSelected(false);
                break;
            case 4:
                tab1_image.setSelected(false);
                tab2_image.setSelected(false);
                tab3_image.setSelected(false);
                tab4_image.setSelected(false);
                tab5_image.setSelected(true);
                break;
        }
    }
}
