package com.wonluue.mynavigationbar.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.StatusBarUtil;
import com.wonluue.mynavigationbar.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EchoWang on 2016/6/12 0012.
 */
public abstract class NavigationBarActivity extends AppCompatActivity {
    // tabbar按钮个数
    private int cols = 0;

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
    //private LinearLayout tabbar;
    private View tabbarLine;
    private GridView tabbar;
    private TabbarAdapter adapter;

    private List<Fragment> fragmentList;// 用来存放每个Fragment或Activity
    private List<String> tabbarTitle;// 用来存放Fragment的标题
    private List<Integer> tabbarIcon;// 用来存放Fragment的图标
    private List<Integer> tabbarIconSelected;// 用来存放Fragment选中后的图标
    private List<Boolean> isShowToolbar;// 用来存放当前Fragment是否显示Toolbar
    private int showPoint = -1;// 用来存放那个Fragment是否有小红点

    private Fragment nowFragment;// 用来记录当前的Fragment
    private int nowSelected = 0;//

    private OnFragmentChangeListener mOnFragmentChange;// 当fragment切换时监听

    private OnTabMenuClickListener mOnTabMenuClick;// 当点击tab时监听


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_navigation_bar);

        // 加载toolbar布局
        initToolbarView();
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
        tabbarLine = findViewById(R.id.tabbar_line);
        tabbar = (GridView) findViewById(R.id.tabbar);

        fragmentList = new ArrayList<>();
        tabbarTitle = new ArrayList<>();
        tabbarIcon = new ArrayList<>();
        tabbarIconSelected = new ArrayList<>();
        isShowToolbar = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbarDefault.setPadding(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        }

        // 如果系统版本小于等于4.4
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            tabbarLine.setVisibility(View.VISIBLE);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                toolbarDefault.setPadding(0, StatusBarUtil.getStatusBarHeight(NavigationBarActivity.this), 0, 0);
            }
            toolbarDefault.setVisibility(View.VISIBLE);
            flToolbarDefault.setVisibility(View.VISIBLE);
        }

        /**
         * 隐藏toolbar
         */
        public void hideToolbar() {
            // 隐藏toolbar时paddingTop要为0，原因是某些需求要显示banner，此时如果状态栏是纯色影响效果
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                toolbarDefault.setPadding(0, 0, 0, 0);
            }
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
     * Fragment管理类
     */
    public class MyFragmentManager {

        private int openOtherId = -1;

        /**
         * 添加fragment
         * 只显示图标
         *
         * @param fragment 要添加的fragment
         * @param iconList     当前fragment显示的图标
         */
        public void add(Fragment fragment, List<Integer> iconList) {
            add(fragment, iconList, null);
        }

        /**
         * 添加fragment
         * 只显示图标且设置当前fragment是否显示toolbar
         *
         * @param fragment    要添加的fragment
         * @param iconList        当前fragment显示的图标
         * @param showToolbar 是否显示toolbar
         */
        public void add(Fragment fragment, List<Integer> iconList, boolean showToolbar) {
            add(fragment, iconList, null, showToolbar);
        }

        /**
         * 添加fragment
         * 显示图标及标题
         *
         * @param fragment 要添加的fragment
         * @param iconList     当前fragment显示的图标
         * @param title    当前fragment显示的标题
         */
        public void add(Fragment fragment, List<Integer> iconList, String title) {
            add(fragment, iconList, title, true);
        }

        /**
         * 添加fragment或activity
         * 显示图标及标题
         *
         * @param fragment    要添加的fragment
         * @param iconList        当前fragment显示的图标
         * @param title       当前fragment显示的标题
         * @param showToolbar 是否显示toolbar
         */
        public void add(Fragment fragment, List<Integer> iconList, String title, boolean showToolbar) {
            fragmentList.add(fragment);
            tabbarIcon.add(iconList.get(0));
            if(iconList.size() > 1){
                tabbarIconSelected.add(iconList.get(1));
            }
            if (title != null)
                tabbarTitle.add(title);
            isShowToolbar.add(showToolbar);
        }

        /**
         * 移除fragment
         * 主要是用来动态移除fragment
         *
         * @param fragment 要移除的fragment
         */
        public void remove(Fragment fragment) {
            if (fragmentList.contains(fragment)) {
                fragmentList.remove(fragment);
            }
        }

        /**
         * 调用此方法则点击对应tab时不打开对应fragment
         *
         * @param fragment
         */
        public void setOpenOther(Fragment fragment) {
            if (fragmentList.size() > 0) {
                for (int i = 0; i < fragmentList.size(); i++) {
                    if (fragmentList.get(i).equals(fragment)) {
                        openOtherId = i;
                        break;
                    }
                }
            }
        }

        /**
         * 调用此方法后打开默认fragment
         */
        public void setOpenDefault() {
            openOtherId = -1;
        }

        /**
         * 用来适配tabbar
         * 只有当调用了此方法，添加或移除fragment操作才会生效
         */
        public void commitFragment() {
            cols = fragmentList.size();
            tabbar.setNumColumns(cols);

            // 首先移除tabbar上所有的控件，防止当多次操作时产生重复添加的情况
            //tabbar.removeAllViews();
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
                adapter = new TabbarAdapter();
                tabbar.setAdapter(adapter);
            } else {
                // 如果只有一个或没有，隐藏tabbar且不往下执行，老实说如果这样就没必要继承当前Activity了
                tabbar.setVisibility(View.GONE);
                return;
            }
            // 能走到这里代表肯定不止一个fragment
            tabbar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position != nowSelected) {
                        if (mOnTabMenuClick != null)
                            mOnTabMenuClick.OnTabMenuClick(position);
                        if (position != openOtherId) {
                            switchFragment(nowFragment, fragmentList.get(position));
                            nowFragment = fragmentList.get(position);
                            nowSelected = position;
                            if (isShowToolbar.get(position)) {
                                new MyToolbarManager().showToolbar();
                            } else {
                                new MyToolbarManager().hideToolbar();
                            }
                            if (mOnFragmentChange != null) {
                                mOnFragmentChange.onFragmentChange(position);
                                adapter.notifyDataSetChanged();
                            /*ivTabbarIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));
                            for (int i = 0; i < fragmentList.size(); i++) {
                                if (position != i) {
                                    ImageView iv = ((ImageView) tabbar.getChildAt(i).findViewWithTag(i));
                                    if (iv != null) {
                                        iv.setColorFilter(getResources().getColor(R.color.color_tabbar_icon));
                                    }
                                }
                            }*/
                            }
                        }
                    }
                }
            });
        }

        public void showPoint(int id){
            showPoint = id;
            if(adapter != null)
                adapter.notifyDataSetChanged();
        }

        public void hidePoint(){
            showPoint = -1;
            if(adapter != null)
                adapter.notifyDataSetChanged();
        }

        public void swithFragment(int position){
            switchFragment(nowFragment, fragmentList.get(position));
            nowFragment = fragmentList.get(position);
            nowSelected = position;
            if (isShowToolbar.get(position)) {
                new MyToolbarManager().showToolbar();
            } else {
                new MyToolbarManager().hideToolbar();
            }
            if (mOnFragmentChange != null) {
                mOnFragmentChange.onFragmentChange(position);
                adapter.notifyDataSetChanged();
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

    class TabbarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cols;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getApplicationContext(), R.layout.item_tabbar_menu, null);

            LinearLayout llTabbarMenu = (LinearLayout) convertView.findViewById(R.id.ll_tabbar_menu);
            ImageView ivTabbarIcon = (ImageView) convertView.findViewById(R.id.iv_tabbar_icon);
            View viewTabbarPoint = convertView.findViewById(R.id.view_tabbar_point);
            TextView tvTabbarTitle = (TextView) convertView.findViewById(R.id.tv_tabbar_title);

            // 用来得到屏幕的宽高
            DisplayMetrics metrics = new DisplayMetrics();
            // 得到屏幕的宽高
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            // 得到屏幕宽度
            int width = metrics.widthPixels;// 屏幕高度像素
            // 得到布局管理
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llTabbarMenu.getLayoutParams();
            // 设置宽度
            lp.width = width / cols;
            // 更改布局
            llTabbarMenu.setLayoutParams(lp);

            if(position == nowSelected) {
                if(tabbarIconSelected.size() > 0) {
                    ivTabbarIcon.setImageResource(tabbarIconSelected.get(position));
                }else {
                    ivTabbarIcon.setColorFilter(getResources().getColor(R.color.color_tabbar_icon));
                }
            }else {
                ivTabbarIcon.setImageResource(tabbarIcon.get(position));
            }
            tvTabbarTitle.setText(tabbarTitle.get(position));

            if(showPoint == position){
                viewTabbarPoint.setVisibility(View.VISIBLE);
            }else {
                viewTabbarPoint.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
}
