package com.tangzy.mvpframe.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tangzy.mvpframe.R;
import com.tangzy.mvpframe.core.presenter.MvpPresenterIml;
import com.tangzy.mvpframe.core.view.MvpView;
import com.tangzy.mvpframe.manager.Constant;
import com.tangzy.mvpframe.permission.EasyPermissions;
import com.tangzy.mvpframe.util.Logger;
import com.tangzy.mvpframe.util.StatusBarUtil;
import com.tangzy.mvpframe.view.CustomListViewHeader;
import com.tangzy.mvpframe.view.ProgressDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/31.
 */

public class BaseActivity extends AppCompatActivity implements IActivity, EasyPermissions.PermissionCallbacks, MvpView {
//    private static final String TAG = "";
    protected String TAG = "BaseActivity";
    private Activity activity;
    protected boolean isFirstLayout = true;
    private Unbinder unbinder;

    private TextView tv_title;
    // 标题栏左侧，右侧图标
    private ImageView iv_left;
    private List<MvpPresenterIml> mvpPresenterImls;

    private SmartRefreshLayout mRefreshLayout;
    private CustomListViewHeader customHead;
    private LinearLayout content_view;
    private LinearLayout titleLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        startTransition();
        TAG = getClass().getSimpleName();
        super.onCreate(savedInstanceState);
    }

    public void addPresenter(MvpPresenterIml presenterIml){
        Logger.d(TAG, "addPresenter");
        if (mvpPresenterImls == null){
            mvpPresenterImls = new ArrayList<>();
        }
        mvpPresenterImls.add(presenterIml);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        if (layoutResID < 0) {
            return;
        }
        setView(layoutResID);
        initTitleBar();
        initView();
        initStatusBar();
        setStatusBarFontIconDark(false);
        unbinder = ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
    }

    protected void startTransition(){
        overridePendingTransition(R.anim.slide_in_right, 0);
    }
    protected void stopTransition(){
        overridePendingTransition(0, R.anim.slide_out_right);
    }


    protected void hideTitleLeft() {
        if (iv_left != null)
            iv_left.setVisibility(View.GONE);
    }

    protected void hideTitle() {
        if (titleLayout != null)
            titleLayout.setVisibility(View.GONE);
    }
    protected void setTitle(String title) {
        if (tv_title != null && title != null)
            tv_title.setText(title);
    }


    /**
     * 设置Activity的内容布局，取代setContentView（）方法
     */
    private void setView(int layoutResID) {
        LinearLayout content_linear = this.findViewById(R.id.content_view);
        content_linear.addView(View.inflate(this, layoutResID, null), new LinearLayout.LayoutParams(-1, -1));
        initAllViewForActivity(content_linear);
    }

    private void initAllViewForActivity(final ViewGroup vg) {
        vg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                vg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (activity != null && activity instanceof IActivity) {
                    ((IActivity) activity).onActivityFirstLayout();
                }
            }
        });
    }

    private void initTitleBar() {
        titleLayout = findViewById(R.id.titleLayout);
        tv_title = findViewById(R.id.tv_title);
        iv_left = findViewById(R.id.iv_left);

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });
    }

    private void initView() {
        content_view = findViewById(R.id.content_view);
        ViewGroup.LayoutParams layoutParams = content_view.getLayoutParams();
        layoutParams.height = Constant.heightScreen - this.getResources().getDimensionPixelOffset(R.dimen.dp_70);
        content_view.setLayoutParams(layoutParams);
        mRefreshLayout = findViewById(R.id.load_more_list_view_ptr_frame);
        customHead = findViewById(R.id.customHead);
        setEnableRefresh(false);
        setEnableLoadmore(false);
    }

    protected void finishRefresh(){
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh();
    }
    protected void finishLoadmore(){
        if (mRefreshLayout != null)
            mRefreshLayout.finishLoadmore();
    }

    protected void setOnRefreshListener(OnRefreshListener listener){
        if (mRefreshLayout != null){
            setEnableRefresh(true);
            mRefreshLayout.setOnRefreshListener(listener);
        }
    }
    protected void setOnLoadmoreListener(OnLoadmoreListener listener){
        if (mRefreshLayout != null){
            setEnableLoadmore(true);
            mRefreshLayout.setOnLoadmoreListener(listener);
        }
    }
    protected void setEnableLoadmore(boolean isMore){
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableLoadmore(isMore);
        }
    }
    protected void setEnableRefresh(boolean isRefresh){
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(isRefresh);
        }
    }
    protected void setLeftView(int visibitity){
        if (iv_left != null){
            iv_left.setVisibility(visibitity);
        }
    }

    private void initStatusBar() {
        StatusBarUtil.setTranslucentForImageView(BaseActivity.this, 0);
    }
    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体是否为深色
     */
    public void setStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = getWindow();
            Class clazz = getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            Window window = getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    protected void onClickBack(){
        finish();
    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }

    @Override
    public void onActivityFirstLayout() {
        isFirstLayout = false;
    }

    public void cleanPresenter(){
        Logger.d(TAG, "cleanPresenter");
        if (mvpPresenterImls !=null && mvpPresenterImls.size() > 0){
            int size = mvpPresenterImls.size();
            for (int i=0; i < size; i ++){
                MvpPresenterIml mvpPresenterIml = mvpPresenterImls.get(i);
                if (mvpPresenterIml != null)
                    Logger.d(TAG, "detachView");
                mvpPresenterIml.detachView(false);
            }
        }
    }
    
    /**
     * 隐藏软键盘
     * @param view 软键盘属于哪个View 的。activity可直接传递null
     */
    protected void hideKeyBoard(View view) {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null){
                view = getCurrentFocus();
            }
            if (view != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unbinder.unbind();
        cleanPresenter();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        super.finish();
        stopTransition();
    }

    protected static final int RC_PERM = 123;
    protected static int reSting = R.string.ask_again;//默认提示语句
    private CheckPermListener mListener;

    public interface CheckPermListener {
        //权限通过后的回调方法
        void superPermission();
    }
    public void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
        mListener = listener;
        List<String> list = new ArrayList<>();
        for (String item: mPerms) {
            if (!EasyPermissions.hasPermissions(this, item)) {
                list.add(item);
            }
        }
        int length = list.size();
        if (length>0){
            String[] perms = new String[list.size()];
            for (int i = 0; i<length; i++){
                perms[i] = list.get(i);
            }
            EasyPermissions.requestPermissions(this, getString(resString),
                    RC_PERM, perms);
        }else {
            if (mListener != null)
                mListener.superPermission();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //同意了某些权限可能不是全部
    }

    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.perm_tip),
                R.string.setting, R.string.cancel, null, perms);
    }
    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyBoard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Context obtainContext() {
        return this;
    }

    @Override
    public Activity obtainActivity() {
        return this;
    }

    @Override
    public void showLoading(String waitMessage) {
        ProgressDialog.getInstance(this).setMessage(waitMessage).show();
    }

    @Override
    public void hideLoading() {
        ProgressDialog.getInstance(this).dismiss();
    }
}
