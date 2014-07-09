package com.guotion.sicilia.ui.view;


import com.guotion.sicilia.R;
import com.guotion.sicilia.ui.view.ScrollOverListView.OnScrollOverListener;
import com.guotion.sicilia.ui.view.ScrollOverListView.ScrollListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class PullDownView extends LinearLayout implements OnScrollOverListener{
	
	private final int START_PULL_DEVIATION = 50; // 移动误差
	private final int WHAT_DID_MORE = 5; // Handler what 已经获取完更多
	private final int WHAT_DID_REFRESH = 3; // Handler what 已经刷新完
	/**已经含有 下拉刷新功能的列表**/
	private ScrollOverListView mListView;
	/**刷新和更多的事件接口**/
	private OnPullDownListener mOnPullDownListener;
	private float mMotionDownLastY; // 按下时候的Y轴坐标
	private boolean mIsPullUpDone; // 是否回推完成
	
	public PullDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderViewListView(context);
	}

	public PullDownView(Context context) {
		super(context);
		initHeaderViewListView(context);
	}
	
	public void setTvFreshIsVisible(boolean isVisible){
		mListView.setTvFreshIsVisible(isVisible);
	}
	/*
	 * ================================== Public method 外部使用，具体就是用这几个就可以了
	 */

	/**
	 * 刷新和获取更多事件接口
	 */
	public interface OnPullDownListener {
		/**刷新事件接口  这里要注意的是获取更多完 要关闭 刷新的进度条RefreshComplete()**/
		void onRefresh();
	}

	/**
	 * 通知已经获取完更多了，要放在Adapter.notifyDataSetChanged后面
	 * 当你执行完更多任务之后，调用这个notyfyDidMore() 才会隐藏加载圈等操作
	 */
	public void notifyDidMore() {
		mUIHandler.sendEmptyMessage(WHAT_DID_MORE);
	}

	/** 刷新完毕 关闭头部滚动条 **/
	public void RefreshComplete() {
		mUIHandler.sendEmptyMessage(WHAT_DID_REFRESH);
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnPullDownListener(OnPullDownListener listener) {
		mOnPullDownListener = listener;
	}

	/**
	 * 获取内嵌的listview
	 * 
	 * @return ScrollOverListView
	 */
	public ScrollOverListView getListView() {
		return mListView;
	}

	/*
	 * ================================== Private method 具体实现下拉刷新等操作
	 * 
	 * ==================================
	 */

	/**
	 * 初始化界面
	 */
	private void initHeaderViewListView(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		/*
		 * ScrollOverListView 同样是考虑到都是使用，所以放在这里 同时因为，需要它的监听事件
		 */
		mListView = new ScrollOverListView(context);
		mListView.setOnScrollOverListener(this);
		mListView.setCacheColorHint(0);
		mListView.setSelector(R.color.transparentwhite);
		mListView.setDividerHeight(0);
		addView(mListView, android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);

		// 空的listener
		mOnPullDownListener = new OnPullDownListener() {
			@Override
			public void onRefresh() {
			}
		};

	}

	private Handler mUIHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_REFRESH: {
				mListView.onRefreshComplete();
				return;
				}
			}
		}

	};


	

	/*
	 * ================================== 实现 OnScrollOverListener接口
	 */

	@Override
	public boolean onListViewTopAndPullDown(int delta) {

		return true;
	}
	
	@Override
	public boolean onMotionDown(MotionEvent ev) {
		mIsPullUpDone = false;
		mMotionDownLastY = ev.getRawY();

		return false;
	}

	@Override
	public boolean onMotionMove(MotionEvent ev, int delta) {
		// 当头部文件回推消失的时候，不允许滚动
		if (mIsPullUpDone)
			return true;

		// 如果开始按下到滑动距离不超过误差值，则不滑动
		final int absMotionY = (int) Math.abs(ev.getRawY() - mMotionDownLastY);
		if (absMotionY < START_PULL_DEVIATION)
			return true;

		return false;
	}

	@Override
	public boolean onMotionUp(MotionEvent ev) {
		if (ScrollOverListView.canRefleash) {
			ScrollOverListView.canRefleash = false;
			mOnPullDownListener.onRefresh();
		}
		return false;
	}

	/**隐藏头部 禁用下拉更新**/
	public void setHideHeader() {
		mListView.showRefresh = false;
	}

	/**显示头部 使用下拉更新**/
	public void setShowHeader() {
		mListView.showRefresh = true;
	}

	public void setScrollListener(ScrollListener scrollListener) {
		mListView.setScrollListener(scrollListener);
	}
}
