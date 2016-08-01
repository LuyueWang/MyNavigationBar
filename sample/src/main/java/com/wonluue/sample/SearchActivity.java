package com.wonluue.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wonluue.mynavigationbar.activity.LocalActivity;
import com.wonluue.mynavigationbar.activity.NavigationBarActivity;


public class SearchActivity extends LocalActivity {

    @Override
    public View createView() {
        TextView tv = new TextView(this);
        tv.setText("ssssssssssssssssss");
        return tv;
    }

}
