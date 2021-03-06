package com.wonluue.mynavigationbar.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Echo.W on 2016/8/4 0004.
 */
public class UiProgressSheetView extends UiSheetView {

    private Activity activity;
    private TextView textView;
    private View view;

    public UiProgressSheetView(@NonNull Activity activity, int location) {
        super(activity, location);
        view = View.inflate(activity,R.layout.layout_progress_sheet_view,null);
        textView = (TextView) view.findViewById(R.id.textView);
        setView(view);
    }

    public UiProgressSheetView setMessage(String message){
        textView.setText(message);
        return this;
    }

    public UiProgressSheetView setMessage(int messageId){
        textView.setText(messageId);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void show(float blur) {
        super.show(blur);
    }
}
