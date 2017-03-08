package com.wonluue.sample;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wonluue.mynavigationbar.activity.NavigationBarActivity;
import com.wonluue.mynavigationbar.utils.UiUtils;
import com.wonluue.mynavigationbar.view.UiListSheetView;
import com.wonluue.mynavigationbar.view.UiNotifyView;
import com.wonluue.mynavigationbar.view.UiProgressSheetView;
import com.wonluue.mynavigationbar.view.UiSheetView;
import com.wonluue.mynavigationbar.view.UiTipView;
import com.wonluue.sample.fragment.OneFragment;
import com.wonluue.sample.fragment.ThreeFragment;
import com.wonluue.sample.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavigationBarActivity implements NavigationBarActivity.OnFragmentChangeListener, NavigationBarActivity.OnTabMenuClickListener {

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
        List<Integer> iconList = new ArrayList<>();
        iconList.add(R.mipmap.ic_launcher);
        mFragmentManager.add(new OneFragment(), iconList, "发现", false);
        mFragmentManager.add(new TwoFragment(), iconList, "信息");
        mFragmentManager.add(threeFragment, iconList, "Activity");
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
                mTvToolbarMiddle.setText("自定义提示");
                break;
            case 2:
                setStatusDarkMode();
                mTvToolbarMiddle.setText("自定义Activity");
                break;
        }
    }

    @Override
    public void OnTabMenuClick(int fragmentId) {
        switch (fragmentId) {
            case 2:
                if (1 == 2) {
                    mFragmentManager.setOpenOther(threeFragment);
                    Intent intent = new Intent(MainActivity.this, TestWebActivity.class);
//                    Intent intent = new Intent(MainActivity.this, TestLocalActivity.class);
                    intent.putExtra("back_name", "主页");
                    intent.putExtra("url", "http://www.baidu.cn");
                    startActivity(intent);
                } else {
                    mFragmentManager.setOpenDefault();
                }
                break;
        }
    }

    public void openLocal(View v) {
        Intent intent = new Intent(this, TestLocalActivity.class);
        intent.putExtra("back_name", "主页");
        startActivity(intent);
    }

    public void openWeb(View v) {
        Intent intent = new Intent(this, TestWebActivity.class);
        intent.putExtra("back_name", "主页");
        intent.putExtra("url", "http://www.baidu.cn");
        startActivity(intent);
    }

    public void showSheetView(View v) {
        final UiSheetView uiSheetView = new UiSheetView(this, UiSheetView.CENTER);
        uiSheetView.setTitle("温馨提示");
        uiSheetView.setMessage("今天心情很好，明天也很好，后退会更好，因为后天周五了");
        uiSheetView.setNegativeButton("知道了", null);
        uiSheetView.setPositiveButton("打开网页", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestWebActivity.class);
                intent.putExtra("back_name", "主页");
                intent.putExtra("url", "http://www.baidu.cn");
                startActivity(intent);
                uiSheetView.dismiss();
            }
        });
        uiSheetView.setCancelable(true);
        uiSheetView.show(15);
    }

    public void showDiySheetView(View v) {
        final UiSheetView uiSheetView = new UiSheetView(this, UiSheetView.CENTER);
        uiSheetView.setTitle("温馨提示");
        uiSheetView.setNegativeButton("知道了", null);
        uiSheetView.setPositiveButton("打开网页", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestWebActivity.class);
                intent.putExtra("back_name", "主页");
                intent.putExtra("url", "http://www.baidu.cn");
                startActivity(intent);
                uiSheetView.dismiss();
            }
        });
        uiSheetView.setCancelable(true);
        EditText editText = new EditText(this);
        uiSheetView.setView(editText);
        uiSheetView.show();
    }

    public void showTextListSheetView(View v) {
        final List<String> list = new ArrayList<>();
        list.add("打开");
        list.add("编辑");
        list.add("复制");
        list.add("剪切");
        list.add("重命名");
        list.add("详情");
        list.add("分享");
        list.add("发送到");
        list.add("属性");
        final UiListSheetView uiListSheetView = new UiListSheetView(this, UiSheetView.BOTTOM);
        uiListSheetView.setTitle("操作");
        uiListSheetView.setNegativeButton("取消", null);
        uiListSheetView.setOnItemClickListener(new UiListSheetView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "你点击了：" + list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        uiListSheetView.setDataList(list);
        uiListSheetView.show();
    }

    public void showRadioListSheetView(View v) {
        final List<String> list = new ArrayList<>();
        list.add("北京");
        list.add("上海");
        list.add("石家庄");
        list.add("呼和浩特");
        list.add("深圳");
        final UiListSheetView uiListSheetView = new UiListSheetView(this, UiSheetView.CENTER);
        uiListSheetView.setTitle("单选");
        uiListSheetView.setOnItemClickListener(new UiListSheetView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "你点击了：" + list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        uiListSheetView.setNegativeButton("取消", null);
        uiListSheetView.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uiListSheetView.dismiss();
            }
        });
        uiListSheetView.setShowType(UiListSheetView.SHOW_RADIO);
        uiListSheetView.setCancelable(true);
        uiListSheetView.setDataList(list);
        uiListSheetView.show();
    }

    public void showProgressSheetView(View v) {
        final UiProgressSheetView ui = new UiProgressSheetView(this, UiSheetView.BOTTOM);
        ui.setMessage("今天心情很好，明天也很好，后退会更好，因为后天周五了");
        ui.setNegativeButton("知道了", null);
        ui.show();
    }

    public void openDarkTip(View v) {
        UiTipView.makeTip(this, "提示成功", R.mipmap.no_network, UiTipView.DARK).show(15);
    }

    public void openLightTip(View v) {
        UiTipView.makeTip(this, "提示成功", R.mipmap.no_network, UiTipView.LIGHT).show();
    }

    public void openDeafultNotify(View v) {
        UiNotifyView.makeTip(this, "通知成功", R.mipmap.no_network, UiNotifyView.DEFAULT).show();
    }

    public void openDarkNotify(View v) {
        UiNotifyView.makeTip(this, "通知成功", R.mipmap.no_network, UiNotifyView.DARK).show();
    }

    public void openLightNotify(View v) {
        UiNotifyView.makeTip(this, "通知成功", R.mipmap.no_network, UiNotifyView.LIGHT).show();
    }

}
