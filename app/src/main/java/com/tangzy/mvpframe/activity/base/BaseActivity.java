package com.tangzy.mvpframe.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.tangzy.mvpframe.manager.MessgeEvent;
import com.tangzy.mvpframe.permission.EasyPermissions;
import com.tangzy.mvpframe.util.Logger;
import com.tangzy.mvpframe.util.StatusBarUtil;
import com.tangzy.mvpframe.view.CustomListViewHeader;
import com.tangzy.mvpframe.view.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/31.
 */


/**
 * Created by Administrator on 2017/10/26.
 */
public class BaseActivity extends AppCompatActivity implements IActivity, EasyPermissions.PermissionCallbacks, MvpView {
    protected static String TAG = "BaseActivity";
    private Activity activity;
    protected boolean isFirstLayout = true;
    private Unbinder unbinder;

    private TextView mTitleView;
    // 标题栏左侧，右侧图标
    private ImageView mLeftBtn;
    private LinearLayout titleLayout;


    public static final int REQUEST_CODE_CALLBACK = 0x1000;
    public static final String EXTRA_ACTIVITY_NAME = "_extra_activity_name";
    public static final String EXTRA_START_CALLBACK = "_extra_start_callback";

    public static final String ONE_LAYOUT = "OneLayout";
    public static final String TWO_LAYOUT = "TwoLayout";

    private static final int DIALOG_WAIT = 102;
    private static final int MSG_WHAT_SHOW_WAIT_DIALOG = 104;
    private static final int MSG_WHAT_HIDE_WAIT_DIALOG = 105;
    private String showMessage = "";
    private int dialogId = -1;

    private List<MvpPresenterIml> mvpPresenterImls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        startTransition();
        TAG = getClass().getSimpleName();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        if (layoutResID < 0) {
            return;
        }
        setView(layoutResID);
        unbinder = ButterKnife.bind(this);
        initTitleBar();
        setStatusBarFontIconDark(true);
        initStatusBar();
    }

    protected void initStatusBar() {
        StatusBarUtil.setTranslucentForImageView(BaseActivity.this, 0);
    }

    public void addPresenter(MvpPresenterIml presenterIml){
        Logger.d(TAG, "addPresenter");
        if (mvpPresenterImls == null){
            mvpPresenterImls = new ArrayList<>();
        }
        mvpPresenterImls.add(presenterIml);
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

    protected void startTransition(){
        overridePendingTransition(R.anim.slide_in_right, 0);
    }
    protected void stopTransition(){
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        cleanPresenter();
    }

    @Override
    public void finish() {
        super.finish();
        stopTransition();
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

    public TextView getTitleView() {
        return mTitleView;
    }

    public void setTitleText(String title) {
        if (mTitleView!=null && title != null)
            mTitleView.setText(title);
    }

    protected void onClickBack(){
        finish();
    }

    @Override
    public void onBackPressed() {
        onClickBack();
    }

    /**
     * 设置Activity的内容布局，取代setContentView（）方法
     */
    public void setView(int layoutResID) {
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
    //
    public void initTitleBar() {
        mTitleView = findViewById(R.id.tv_title);
        mLeftBtn = findViewById(R.id.title_left_img);
        titleLayout = findViewById(R.id.included_title);

        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onClickBack();
            }
        });

    }

    public void hideTitle() {
        if (titleLayout != null)
            titleLayout.setVisibility(View.GONE);
    }

    public void setTitleLayout(String layout) {
        if (titleLayout.getVisibility() != View.VISIBLE){
            titleLayout.setVisibility(View.VISIBLE);
        }
        if (ONE_LAYOUT.equals(layout)){
            titleLayout.setBackgroundColor(getResources().getColor(R.color.color_EAEAEA));
            setStatusBarFontIconDark(true);
        }else if (TWO_LAYOUT.equals(layout)){
            titleLayout.setBackgroundColor(getResources().getColor(R.color.color_53B890));
        }
//        initStatusBar();
    }

    public void setLeftShow(boolean isShow) {
        if (mLeftBtn != null) {
            if (isShow){
                mLeftBtn.setVisibility(View.VISIBLE);
            }else {
                mLeftBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i(getClass().getSimpleName(), "onActivityResult：" + getClass().getSimpleName() + "   intent is null： " + (data == null));
        if (!onActivityResultC(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 当activity第一次layout之后
     */
    @Override
    public void onActivityFirstLayout() {
        isFirstLayout = false;
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

    protected static final int RC_PERM = 123;

    protected static int reSting = R.string.ask_again;//默认提示语句
    private CheckPermListener mListener;

    @Override
    public Context obtainContext() {
        return null;
    }

    @Override
    public Activity obtainActivity() {
        return null;
    }

    @Override
    public void showLoading(String waitMessage) {
        ProgressDialog.getInstance(this).setMessage(waitMessage).show();
    }

    @Override
    public void hideLoading() {
        ProgressDialog.getInstance(this).dismiss();
    }

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
     * 启动一个插入型Activity，自带转场
     * 例如，当你在A中启动B，如果你想在B中启动C，同时关闭A
     * 则在A中使用startCallbackActivity启动B
     * 在B中使用startResponseActivity启动C即可
     *
     * @param intent
     */
    public void startCallbackActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_CODE_CALLBACK);
    }
    /**
     * 启动一个Activity，自带转场，同时关闭自己
     * 例如，当你在A中启动B，如果你想在B中启动C，同时关闭B
     * 则在A中使用startCallbackActivity启动B
     * 在B中使用startResponseActivity，关闭B并启动C即可
     *
     * @param intent
     */
    public void startResponseActivity(Intent intent) {
        Logger.i(TAG, "startResponseActivity：" + "   intent is null： " + (intent == null));
        setResult(Activity.RESULT_FIRST_USER, intent);
        finish();
    }

    /**
     *
     * @param clazz
     */
    public void finishToActivity(Class<? extends Activity> clazz){
        Logger.i(TAG, "finishToActivity clazz:" + clazz.getName());
        if (!getClass().getCanonicalName().equals(clazz.getCanonicalName())){
            Intent intent = new Intent();
            setResult(Activity.RESULT_FIRST_USER, intent);
            intent.putExtra(EXTRA_ACTIVITY_NAME, clazz.getCanonicalName());
            finish();
        }
    }

    /**
     * 关闭从[自己到目标clazz]之间的所有Activity，并启动一个新的Activity
     * 例如，当你在A中启动B，B中启动C->D->E->F。此时想启动Z，同时关闭之前除A以外所有Activity
     * 则在从A到Y启动Activity都使用startCallbackActivity方法。而在Y->Z使用
     * startResponseActivityFromAssignedActivity，第一个参数选Z的intent，
     * 第二个参数选用想要关闭的最前面一个Activity也就是B。
     * <p>
     * 如果仅仅是想关闭从Z到A的所有Activity而不开新的Activity，则此处传入一个不带class和Action的干净的 Intent 即可
     *
     * @param clazz  打开新Activity时所要关闭的最后一个Activity
     * @param intent
     */
    public void startResponseActivityFromAssignedActivity(Intent intent, Class<? extends Activity> clazz) {
        Logger.i(TAG, "startResponseActivityFromAssignedActivity  intent:" + intent + " clazz:" + clazz.getName());
        if (getClass().getCanonicalName().equals(clazz.getCanonicalName())){
            startResponseActivity(intent);
        }else {
            setResult(Activity.RESULT_FIRST_USER, intent);
            intent.putExtra(EXTRA_ACTIVITY_NAME, clazz.getCanonicalName());
            finish();
        }
    }


    //只有ui.onActivityResult里面的代码都不执行才会交给外部调用者的super.onActivityResult处理
    /*package*/
    boolean onActivityResultC(int requestCode, int resultCode, Intent data) {
        Logger.i(TAG, "onActivityResult ------ Intent data is null： " + (data == null) + "  requestCode:" + requestCode + "   resultCode:" + resultCode);
        if (requestCode == REQUEST_CODE_CALLBACK) {
            if (resultCode == Activity.RESULT_FIRST_USER) {
                if (data != null) {
                    try {
                        String activityName = data.getStringExtra(EXTRA_ACTIVITY_NAME);
                        Logger.e(TAG, "onActivityResult  this Activity name:" + getClass().getCanonicalName() + "   target Activity name:" + activityName);
                        if (!TextUtils.isEmpty(activityName)) {
                            Logger.i(TAG, "UILayer的onActivityResult方法执行了，并且activityName不为空");
                            @SuppressWarnings("unchecked")
                            Class<? extends Activity> clazz = (Class<? extends Activity>) Class.forName(activityName);
                            if (getClass().getCanonicalName().equals(activityName)) {
                                Logger.i(TAG, "onActivityResult");
                                if (data.getBooleanExtra(EXTRA_START_CALLBACK, false)) {
                                    data.removeExtra(EXTRA_ACTIVITY_NAME);
                                    startResponseActivity(data);
                                    return true;
                                    /*EXTRA_START_CALLBACK 为 true 代表在名为 activityName 的activity结束之后，将 data 交给上一个activity去处理。
                                     反之，如果为 false 代表会在当前activity结束后立即在上个activity基础上用 data 再开启一个activity。*/
                                }
                                finish();
                                if (isIntentValid(data)) {
                                    startActivity(data);
                                }
                                return true;
                            }
                            startResponseActivityFromAssignedActivity(data, clazz);
                            return true;
                        }
                        //FIXME
                    } catch (Exception ignore) {
                    }
                    if (isIntentValid(data)) {
                        startCallbackActivity(data);//只要data不为空就会开启一个新的Activity
                    }
                    return true;
                }
            }
        }else if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
            //设置返回
        }
        return false;
    }

    public boolean isIntentValid(Intent intent) {//判断intent中是否包含一个明确的意图，即是否有明确的Activity要跳转
        if (intent == null || intent.getComponent() == null)
            return false;
        return !TextUtils.isEmpty(intent.getComponent().getClassName() + intent.getAction());
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyBoard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            InputMethodManager  inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
