package com.wonluue.mynavigationbar.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2016/8/4 0004.
 */
public class UiListSheetView extends UiSheetView {

    public static final int SHOW_TEXT = 0;
    public static final int SHOW_RADIO = 1;

    private OnItemClickListener mOnItemClickListener;

    private int show_type = SHOW_TEXT;// 记录显示的样式

    private ListView listView;
    private List<String> dataList;// 用来存放传递过来的数据
    private Activity activity;

    public UiListSheetView(@NonNull Activity activity, int location) {
        super(activity, location);
        this.activity = activity;
        dataList = new ArrayList<>();
        listView = new ListView(activity);
        listView.setDividerHeight(1);
        setView(listView);
    }

    public UiListSheetView setDataList(List<String> data) {
        if (data != null)
            dataList = data;
        return this;
    }

    public UiListSheetView setShowType(int type) {
        show_type = type;
        return this;
    }

    @Override
    public void show() {
        super.show();
        adapter = new ItemAdapter();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (show_type == SHOW_RADIO) {
                    ((RadioButton) view.findViewWithTag(position)).setChecked(true);
                    setSelectIndex(position);
                }
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(position);
            }
        });

        listView.setAdapter(adapter);

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int sHeight = wm.getDefaultDisplay().getHeight();// 屏幕高度
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
        if (dataList.size() > 5)
            linearParams.height = sHeight / 3;
        listView.setLayoutParams(linearParams);

    }

    private ItemAdapter adapter;
    private int selectIndex = 0;

    private void setSelectIndex(int index) {
        selectIndex = index;
        adapter.notifyDataSetChanged();
    }

    class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if (show_type == SHOW_RADIO) {
                LinearLayout ll = new LinearLayout(activity);
                ll.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                final RadioButton radioButton = new RadioButton(activity);
                radioButton.setPadding(UiUtils.dip2px(8), UiUtils.dip2px(14), UiUtils.dip2px(14), UiUtils.dip2px(14));
                ll.setPadding(UiUtils.dip2px(14), 0, 0, 0);
                radioButton.setText(dataList.get(position));
                radioButton.setFocusable(false);
                radioButton.setClickable(false);
                radioButton.setTag(position);
                ll.addView(radioButton);

                if (selectIndex == position) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
                return ll;
            } else {
                TextView tv = new TextView(activity);
                tv.setPadding(UiUtils.dip2px(14), UiUtils.dip2px(14), UiUtils.dip2px(14), UiUtils.dip2px(14));
                tv.setText(dataList.get(position));
                tv.setTextColor(activity.getResources().getColor(R.color.color_sheet_view_msg));
                return tv;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
