package com.wonluue.sample;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.wonluue.mynavigationbar.NavigationBarActivity;
import com.wonluue.sample.fragment.OneFragment;
import com.wonluue.sample.fragment.ThreeFragment;
import com.wonluue.sample.fragment.TwoFragment;

public class MainActivity extends NavigationBarActivity {

    private MyFragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbarTextView(ToolbarTextView.CENTER_TEXTVIEW)
        .setText("猫小贷");

        fragmentManager = new MyFragmentManager();
        fragmentManager.add(new OneFragment(),"发现",R.mipmap.ic_launcher);
        fragmentManager.add(new TwoFragment(),"理财",R.mipmap.ic_launcher);
        fragmentManager.add(new ThreeFragment(),"我的",R.mipmap.ic_launcher);
        fragmentManager.commitFragment();
    }


}
