package com.wonluue.mynavigationbar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wonluue.mynavigationbar.R;
import com.wonluue.mynavigationbar.fragment.SlidingPaneLayout.LayoutParams;
import com.wonluue.mynavigationbar.fragment.SlidingPaneLayout.PanelSlideListener;

/**
 * 支持水平方向拖拽返回的Fragment
 * Created by EchoWang on 2016/7/28.
 */
public class SlidingBackFragment extends BaseFragment implements PanelSlideListener {

	private SlidingPaneLayout mSlidingPaneLayout;

	private PanelSlideListener mPanelSlideListener;

	private boolean mSlideable;

    protected LayoutInflater inflater;

    private View mShadowView;

	@Override
	public final View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		if(mSlideable){
			this.mSlidingPaneLayout = new SlidingPaneLayout(getActivity());
			this.mSlidingPaneLayout.setPanelSlideListener(mSlideListener);
			this.mSlidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);

			mShadowView = View.inflate(getActivity(), R.layout.fragment_sliding_back_left, null);
			final LayoutParams layoutParams = new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

			this.mSlidingPaneLayout.addView(mShadowView, layoutParams);
			View content = onCreateView(container, savedInstanceState);
			if(content != null){
				this.mSlidingPaneLayout.addView(content, layoutParams);
				this.mSlidingPaneLayout.setDragView(content);
			}

			final ViewGroup contentParent = new FrameLayout(getActivity());
			contentParent.addView(this.mSlidingPaneLayout);
			return contentParent;
		}else{
			return onCreateView(container, savedInstanceState);
		}
	}

	public View onCreateView(ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mShadowView = null;
        mSlidingPaneLayout = null;
        mPanelSlideListener = null;
        inflater = null;
    }

    /**
	 * 设置滚动状态监听器
	 * @param panelSlideListener
	 */
	public void setPanelSlideListener(
			final PanelSlideListener panelSlideListener) {
		this.mPanelSlideListener = panelSlideListener;
	}

	/**
	 * 关闭该页面
	 */
	public void finish() {
		if(mSlidingPaneLayout != null){
			mSlidingPaneLayout.markClose();
			setDragable(true);
			this.mSlidingPaneLayout.openPane();
		}else{
			onPanelClosed(null);
		}
	}

	/**
	 * 设置是否可以拖拽
	 * @param dragable
	 */
	public void setDragable(boolean dragable) {
		if(mSlidingPaneLayout != null){
			this.mSlidingPaneLayout.setDragable(dragable);
		}
	}

	/**
	 * 是否支持滑动
	 * @param slideable
	 */
	public void setSlideable(boolean slideable){
		this.mSlideable = slideable;
	}

    /**
     * 是否为滑动出现
     * @return
     */
    public boolean isSlideable() {
        return mSlideable;
    }

	@Override
	public void onPanelOpened(final View pPanel) {
		if (this.mPanelSlideListener != null) {
			this.mPanelSlideListener.onPanelOpened(pPanel);
		}
	}
	
	@Override
	public void onPanelSlide(final View pPanel, final float pSlideOffset) {
		if (this.mPanelSlideListener != null) {
			this.mPanelSlideListener.onPanelSlide(pPanel, pSlideOffset);
		}
		if(mShadowView != null){
			mShadowView.setBackgroundColor(Color.argb((int) (150 *  (1 - pSlideOffset)), 0, 0, 0));
		}
	}

	@Override
	public void onPanelClosed(final View pPanel) {
		if (this.mPanelSlideListener != null) {
			this.mPanelSlideListener.onPanelClosed(pPanel);
		}
	}

	private PanelSlideListener mSlideListener = new PanelSlideListener() {
		
		@Override
		public void onPanelSlide(View panel, float slideOffset) {
			SlidingBackFragment.this.onPanelSlide(panel, slideOffset);
		}
		
		@Override
		public void onPanelOpened(View panel) {
			SlidingBackFragment.this.onPanelClosed(panel);
		}
		
		@Override
		public void onPanelClosed(View panel) {
			SlidingBackFragment.this.onPanelOpened(panel);
		}
	};
}
