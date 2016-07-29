package com.wonluue.sample.fragment;

import android.view.View;

import com.wonluue.mynavigationbar.fragment.LocalFragment;
import com.wonluue.sample.R;

/**
 * Created by Alex on 2016/7/29 0029.
 */
public class NewLocalFragment extends LocalFragment {
    @Override
    public View createView() {
        View view = View.inflate(getActivity(),R.layout.fragment_one,null);
        return view;
    }
}
