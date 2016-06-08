package com.wonluue.mynavigationbar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wonluue.mynavigationbar.utils.WindowsUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class NavigationBarActivity extends AppCompatActivity {
    private AppBarLayout toolbarDefault;
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

    private List<Fragment> fragmentList;
    private List<String> fragmentTitle;
    private List<Integer> fragmentIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_navigation_bar);
        initToolbarView();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "替换成自己的动作", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });*/
    }

    protected void initToolbarView() {
        toolbarDefault = (AppBarLayout) findViewById(R.id.toolbar_default);
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
        fragmentTitle = new ArrayList<>();
        fragmentIcon = new ArrayList<>();

        toolbarDefault.setPadding(0, WindowsUtils.getStatusBarHeight(this), 0, 0);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // 设置tabbar颜色为灰色
            tabbar.setBackgroundColor(getResources().getColor(R.color.color_tabbar_bg));
        }
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
        // 得到fragment管理器，并添加切换动画
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 如果要切换到的fragment没有被添加过
        if (!to.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.fragmentParent, to).commit();
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
        // 让menu回去
        //menu.toggle();
    }

    /**
     * 设置显示toolbar
     * 默认即显示
     */
    public void showToolbar() {
        toolbarDefault.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏toolbar
     */
    public void hideToolbar() {
        toolbarDefault.setVisibility(View.GONE);
        tabbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 根据参数返回对应的TextView
     *
     * @param i
     * @return
     */
    public TextView getToolbarTextView(int i) {
        if (i == ToolbarTextView.LEFT_TEXTVIEW) {
            tvToolbarLeftText.setVisibility(View.VISIBLE);
            return tvToolbarLeftText;
        }
        if (i == ToolbarTextView.CENTER_TEXTVIEW) {
            tvToolbarCenterText.setVisibility(View.VISIBLE);
            return tvToolbarCenterText;
        }
        if (i == ToolbarTextView.RIGHT_TEXTVIEW) {
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
        if (i == ToolbarImageView.LEFT_IMAGEVIEW) {
            ivToolbarLeftImg.setVisibility(View.VISIBLE);
            return ivToolbarLeftImg;
        }
        if (i == ToolbarImageView.RIGHT_IMAGEVIEW) {
            tvToolbarLeftText.setVisibility(View.VISIBLE);
            return ivToolbarLeftImg;
        }
        return null;
    }

    /**
     * 设置要执行的意图
     *
     * @param intent 为null则不打开任何意图
     */
    public void setIntent(Intent intent) {

    }

    public class ToolbarTextView {
        public static final int LEFT_TEXTVIEW = 0;
        public static final int CENTER_TEXTVIEW = 1;
        public static final int RIGHT_TEXTVIEW = 2;
    }

    public class ToolbarImageView {
        public static final int LEFT_IMAGEVIEW = 0;
        public static final int RIGHT_IMAGEVIEW = 1;
    }

    public class MyFragmentManager {
        public void add(Fragment fragment, String title, int rsid) {
            fragmentList.add(fragment);
            fragmentTitle.add(title);
            fragmentIcon.add(rsid);
        }

        public void remove(Fragment fragment) {
            if (fragmentList.contains(fragment)) {
                fragmentList.remove(fragment);
            }
        }

        public void commitFragment() {
            tabbar.removeAllViews();
            for (int i = 0; i < fragmentList.size(); i++) {
                View view = View.inflate(NavigationBarActivity.this, R.layout.item_tabbar, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                tabbar.addView(view, lp);
            }
            for (int i = 0; i < tabbar.getChildCount(); i++) {
                View childAt = tabbar.getChildAt(i);
                LinearLayout llTabbarMenu = (LinearLayout) childAt.findViewById(R.id.ll_tabbar_menu);
                ImageView ivTabbarIcon = (ImageView) childAt.findViewById(R.id.iv_tabbar_icon);
                TextView tvTabbarTitle = (TextView) childAt.findViewById(R.id.tv_tabbar_title);
                //ivTabbarIcon.setImageResource(fragmentIcon.get(i));
                //tvTabbarTitle.setText(fragmentTitle.get(i));
            }
        }
    }

}
