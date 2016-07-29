package com.wonluue.mynavigationbar.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.fragment.NavigationController;
import com.wonluue.mynavigationbar.utils.StatusBarUtil;
import com.wonluue.mynavigationbar.utils.UiUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EchoWang on 2016/6/12 0012.
 */
public abstract class NavigationBarActivity extends AppCompatActivity {
    private AppBarLayout toolbarDefault;
    private FrameLayout flToolbarDefault;
    private LinearLayout llToolbarLeft;
    private ImageView ivToolbarLeftImg;
    private TextView tvToolbarLeftText;
    private LinearLayout llToolbarCenter;
    private TextView tvToolbarCenterText;
    private LinearLayout llToolbarRight;
    private ImageView ivToolbarRightImg;
    private TextView tvToolbarRightText;
    private FrameLayout fragmentParent;
    private LinearLayout tabbar;

    private List<Fragment> fragmentList;// 用来存放每个Fragment或Activity
    private List<String> tabbarTitle;// 用来存放Fragment的标题
    private List<Integer> tabbarIcon;// 用来存放Fragment的图标
    private List<Boolean> isShowToolbar;// 用来存放当前Fragment是否显示Toolbar

    private Fragment nowFragment;// 用来记录当前的Fragment

    private OnFragmentChangeListener mOnFragmentChange;// 当fragment切换时监听

    private OnTabMenuClickListener mOnTabMenuClick;// 当点击tab时监听

    // 声明一个泛型为当前Activity的软引用
    private static SoftReference<NavigationBarActivity> activitySoftReference;

    private NavigationController mNavigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        activitySoftReference = new SoftReference<>(this);
        setContentView(R.layout.activity_navigation_bar);

        // 加载toolbar布局
        initToolbarView();
        // 实例化导航控制器
        mNavigationController = new NavigationController(this);
        //Snackbar.make(view, "替换成自己的动作", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    /**
     * 加载toolbar布局
     */
    protected void initToolbarView() {
        toolbarDefault = (AppBarLayout) findViewById(R.id.toolbar_default);
        flToolbarDefault = (FrameLayout) findViewById(R.id.fl_toolbar_default);
        llToolbarLeft = (LinearLayout) findViewById(R.id.ll_toolbar_left);
        ivToolbarLeftImg = (ImageView) findViewById(R.id.iv_toolbar_left_img);
        tvToolbarLeftText = (TextView) findViewById(R.id.tv_toolbar_left_text);
        llToolbarCenter = (LinearLayout) findViewById(R.id.ll_toolbar_center);
        tvToolbarCenterText = (TextView) findViewById(R.id.tv_toolbar_center_text);
        llToolbarRight = (LinearLayout) findViewById(R.id.ll_toolbar_right);
        ivToolbarRightImg = (ImageView) findViewById(R.id.iv_toolbar_right_img);
        tvToolbarRightText = (TextView) findViewById(R.id.tv_toolbar_right_text);
        fragmentParent = (FrameLayout) findViewById(R.id.fragmentParent);
        tabbar = (LinearLayout) findViewById(R.id.tabbar);

        fragmentList = new ArrayList<>();
        tabbarTitle = new ArrayList<>();
        tabbarIcon = new ArrayList<>();
        isShowToolbar = new ArrayList<>();

        toolbarDefault.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);

        // 如果系统版本小于5.0
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // 设置tabbar颜色为灰色
            tabbar.setBackgroundColor(getResources().getColor(R.color.color_tabbar_bg));
        }
    }

    /**
     * 设置状态栏为明亮模式
     * 即图标字体为深色
     */
    public void setStatusLightMode() {
        StatusBarUtil.StatusBarLightMode(this);
    }

    /**
     * 设置状态栏为暗夜模式
     * 即图标字体为浅色
     */
    public void setStatusDarkMode() {
        StatusBarUtil.StatusBarDarkMode(this);
    }

    /**
     * 两个fragment切换的方法
     *
     * @param from 当前fragment
     * @param to   要切换到的fragment
     */
    public void switchFragment(Fragment from, Fragment to) {
        // 如果两个fragment都为空，直接return，保证代码的健壮性
        if (from == null || to == null)
            return;
        // 得到fragment管理器
        FragmentTransaction transaction = NavigationBarActivity.this.getSupportFragmentManager().beginTransaction();
        // 如果要切换到的fragment没有被添加过
        if (!to.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.fragmentParent, to).commit();
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
    }

    /**
     * Toolbar管理类
     */
    public class MyToolbarManager {
        public static final int LEFT_TEXTVIEW = 0;// 左侧文字
        public static final int CENTER_TEXTVIEW = 1;// 中间文字
        public static final int RIGHT_TEXTVIEW = 2;// 右侧文字
        public static final int LEFT_IMAGEVIEW = 0;// 左侧图标
        public static final int RIGHT_IMAGEVIEW = 1;// 右侧图标

        /**
         * 设置显示toolbar
         * 默认即显示
         */
        public void showToolbar() {
            // 显示toolbar时要设置paddingTop，防止toolbar顶到屏幕顶端
            toolbarDefault.setPadding(0, StatusBarUtil.getStatusBarHeight(NavigationBarActivity.this), 0, 0);
            toolbarDefault.setVisibility(View.VISIBLE);
            flToolbarDefault.setVisibility(View.VISIBLE);
        }

        /**
         * 隐藏toolbar
         */
        public void hideToolbar() {
            // 隐藏toolbar时paddingTop要为0，原因是某些需求要显示banner，此时如果状态栏是纯色影响效果
            toolbarDefault.setPadding(0, 0, 0, 0);
            toolbarDefault.setVisibility(View.GONE);
            flToolbarDefault.setVisibility(View.GONE);
        }

        /**
         * 根据参数返回对应的TextView
         *
         * @param i
         * @return
         */
        public TextView getToolbarTextView(int i) {
            if (i == LEFT_TEXTVIEW) {
                tvToolbarLeftText.setVisibility(View.VISIBLE);
                return tvToolbarLeftText;
            }
            if (i == CENTER_TEXTVIEW) {
                tvToolbarCenterText.setVisibility(View.VISIBLE);
                return tvToolbarCenterText;
            }
            if (i == RIGHT_TEXTVIEW) {
                tvToolbarRightText.setVisibility(View.VISIBLE);
                return tvToolbarRightText;
            }
            return null;
        }

        /**
         * 根据参数返回对应的ImageView
         *
         * @param i
         * @return
         */
        public ImageView getToolbarImageView(int i) {
            if (i == LEFT_IMAGEVIEW) {
                ivToolbarLeftImg.setVisibility(View.VISIBLE);
                return ivToolbarLeftImg;
            }
            if (i == RIGHT_IMAGEVIEW) {
                tvToolbarLeftText.setVisibility(View.VISIBLE);
                return ivToolbarLeftImg;
            }
            return null;
        }

    }

    /**
     * 获取主场景Activity
     */
    public static NavigationBarActivity getMain() {
        if (activitySoftReference != null) {
            return activitySoftReference.get();
        }
        return null;
    }

    /**
     * 获取导航控制器
     */
    public NavigationController getNavigationController() {
        return mNavigationController;
    }

    /**
     * Fragment管理类
     */
    public class MyFragmentManager {

        private int openOtherId = -1;

        /**
         * 添加fragment
         * 只显示图标
         *
         * @param fragment 要添加的fragment
         * @param rsid     当前fragment显示的图标
         */
        public void add(Fragment fragment, int rsid) {
            add(fragment, rsid, null);
        }

        /**
         * 添加fragment
         * 只显示图标且设置当前fragment是否显示toolbar
         *
         * @param fragment    要添加的fragment
         * @param rsid        当前fragment显示的图标
         * @param showToolbar 是否显示toolbar
         */
        public void add(Fragment fragment, int rsid, boolean showToolbar) {
            add(fragment, rsid, null, showToolbar);
        }

        /**
         * 添加fragment
         * 显示图标及标题
         *
         * @param fragment 要添加的fragment
         * @param rsid     当前fragment显示的图标
         * @param title    当前fragment显示的标题
         */
        public void add(Fragment fragment, int rsid, String title) {
            add(fragment, rsid, title, true);
        }

        /**
         * 添加fragment或activity
         * 显示图标及标题
         *
         * @param fragment    要添加的fragment
         * @param rsid        当前fragment显示的图标
         * @param title       当前fragment显示的标题
         * @param showToolbar 是否显示toolbar
         */
        public void add(Fragment fragment, int rsid, String title, boolean showToolbar) {
            fragmentList.add(fragment);
            tabbarIcon.add(rsid);
            if (title != null)
                tabbarTitle.add(title);
            isShowToolbar.add(showToolbar);
        }

        /**
         * 移除fragment
         * 主要是用来动态移除fragment
         * @param fragment 要移除的fragment
         */
        public void remove(Fragment fragment) {
            if (fragmentList.contains(fragment)) {
                fragmentList.remove(fragment);
            }
        }

        /**
         * 调用此方法则点击对应tab时不打开对应fragment
         * @param fragment
         */
        public void setOpenOther(Fragment fragment){
            if(fragmentList.size() > 0){
                for (int i = 0; i < fragmentList.size(); i++) {
                    if(fragmentList.get(i).equals(fragment)){
                        openOtherId = i;
                        break;
                    }
                }
            }
        }

        /**
         * 调用此方法后打开默认fragment
         */
        public void setOpenDefault(){
            openOtherId = -1;
        }

        /**
         * 用来适配tabbar
         * 只有当调用了此方法，添加或移除fragment操作才会生效
         */
        public void commitFragment() {
            // 首先移除tabbar上所有的控件，防止当多次操作时产生重复添加的情况
            tabbar.removeAllViews();
            // 判断是否有fragment，如果有完成初始化操作
            if (fragmentList.size() > 0) {
                // 记录当前fragment为第一个fragment
                nowFragment = fragmentList.get(0);
                // 设置显示第一个fragment
                FragmentTransaction transaction = NavigationBarActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentParent, nowFragment).commit();
                if (mOnFragmentChange != null)
                    mOnFragmentChange.onFragmentChange(0);
                // 判断当前fragment是否显示toolbar
                if (isShowToolbar.get(0)) {
                    new MyToolbarManager().showToolbar();
                } else {
                    new MyToolbarManager().hideToolbar();
                }
            } else {
                // 如果只有一个或没有，隐藏tabbar且不往下执行，老实说如果这样就没必要继承当前Activity了
                tabbar.setVisibility(View.GONE);
                return;
            }
            // 能走到这里代表肯定不止一个fragment
            // 遍历fragment集合
            for (int i = 0; i < fragmentList.size(); i++) {
                // 每个fragment切换按钮的父容器
                LinearLayout llTabbarMenu = new LinearLayout(NavigationBarActivity.this);
                // 显示fragment的图标
                ImageView ivTabbarIcon = new ImageView(NavigationBarActivity.this);
                // 设置图标控件的属性
                LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                llTabbarMenu.addView(ivTabbarIcon, 0, iconLp);
                // 设置显示图标
                ivTabbarIcon.setImageResource(tabbarIcon.get(i));

                // 如果fragment标题集合大于0，此时表示要显示标题
                if (tabbarTitle.size() > 0) {
                    TextView tvTabbarTitle = new TextView(NavigationBarActivity.this);
                    LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    titleLp.topMargin = UiUtils.dip2px(2);
                    tvTabbarTitle.setTextSize(10);
                    llTabbarMenu.addView(tvTabbarTitle, titleLp);
                    tvTabbarTitle.setText(tabbarTitle.get(i));
                }


                llTabbarMenu.setOrientation(LinearLayout.VERTICAL);
                llTabbarMenu.setGravity(Gravity.CENTER);
                llTabbarMenu.setClickable(true);
                TypedValue typedValue = new TypedValue();
                NavigationBarActivity.this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                llTabbarMenu.setBackgroundResource(typedValue.resourceId);
                llTabbarMenu.setPadding(UiUtils.dip2px(4), UiUtils.dip2px(4), UiUtils.dip2px(4), UiUtils.dip2px(4));

                final int finalI = i;
                llTabbarMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnTabMenuClick != null)
                            mOnTabMenuClick.OnTabMenuClick(finalI);
                        if(finalI != openOtherId) {
                            switchFragment(nowFragment, fragmentList.get(finalI));
                            nowFragment = fragmentList.get(finalI);
                            if (isShowToolbar.get(finalI)) {
                                new MyToolbarManager().showToolbar();
                            } else {
                                new MyToolbarManager().hideToolbar();
                            }
                            if (mOnFragmentChange != null)
                                mOnFragmentChange.onFragmentChange(finalI);
                        }
                    }
                });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                tabbar.addView(llTabbarMenu, lp);

            }
        }
    }

    /**
     * Fragment切换回调接口
     */
    public interface OnFragmentChangeListener {
        void onFragmentChange(int fragmentId);
    }

    /**
     * tab按下回调接口
     */
    public interface OnTabMenuClickListener {
        void OnTabMenuClick(int fragmentId);
    }

    public void addOnFragmentChangeListener(OnFragmentChangeListener listener) {
        this.mOnFragmentChange = listener;
    }

    public void addOnTabMenuClickListener(OnTabMenuClickListener listener) {
        this.mOnTabMenuClick = listener;
    }

}
