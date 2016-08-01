package com.wonluue.sample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wonluue.mynavigationbar.activity.NavigationBarActivity;
import com.wonluue.sample.fragment.OneFragment;
import com.wonluue.sample.fragment.ThreeFragment;
import com.wonluue.sample.fragment.TwoFragment;

public class MainActivity extends NavigationBarActivity implements NavigationBarActivity.OnFragmentChangeListener,NavigationBarActivity.OnTabMenuClickListener {

    private MyToolbarManager mToolbarManager;
    private NavigationBarActivity.MyFragmentManager mFragmentManager;// Fragment管理器
    private TextView mTvToolbarLeft;// 左侧标题
    private TextView mTvToolbarMiddle;// 中间标题
    private TextView mTvToolbarRight;// 右侧标题

    private ThreeFragment threeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbarManager = new MyToolbarManager();
        mTvToolbarLeft = mToolbarManager.getToolbarTextView(mToolbarManager.LEFT_TEXTVIEW);
        mTvToolbarMiddle = mToolbarManager.getToolbarTextView(mToolbarManager.CENTER_TEXTVIEW);
        mTvToolbarRight = mToolbarManager.getToolbarTextView(mToolbarManager.RIGHT_TEXTVIEW);

        threeFragment = new ThreeFragment();

        mFragmentManager = new MyFragmentManager();
        mFragmentManager.add(new OneFragment(),R.mipmap.ic_launcher,"发现",false);
        mFragmentManager.add(new TwoFragment(),R.mipmap.ic_launcher,"理财");
        mFragmentManager.add(threeFragment,R.mipmap.ic_launcher,"我的");
        mFragmentManager.commitFragment();

        addOnFragmentChangeListener(this);
        addOnTabMenuClickListener(this);
        setStatusLightMode();
    }


    @Override
    public void onFragmentChange(int fragmentId) {
        switch (fragmentId) {
            case 0:
                setStatusLightMode();
                break;
            case 1:
                setStatusDarkMode();
                mTvToolbarMiddle.setText("理财");
                break;
            case 2:
                setStatusDarkMode();
                mTvToolbarMiddle.setText("我的");
                break;
        }
    }

    @Override
    public void OnTabMenuClick(int fragmentId) {
        switch (fragmentId) {
            case 2:
                if(1==1){
                    mFragmentManager.setOpenOther(threeFragment);
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                }else {
                    mFragmentManager.setOpenDefault();
                }
                break;
        }
    }
}
