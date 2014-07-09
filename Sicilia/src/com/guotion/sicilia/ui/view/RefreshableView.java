package com.guotion.sicilia.ui.view;

import com.guotion.sicilia.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 刷新控制view
 * scrollview下拉刷新
 * @author Nono
 * 
 */
public class RefreshableView extends LinearLayout {

	private static final String TAG = "LILITH";
	private Scroller scroller;
	private int refreshTargetTop = -120;

	private RefreshListener refreshListener;

	private Long refreshTime = null;
	private int lastY;
	// 拉动标记
	private boolean isDragging = false;
	private View headView;
	private ImageView ivScroll;
	private TextView tvFresh;

	private Context mContext;
	private AnimationDrawable animationDrawable;
	// 是否可刷新标记
	private boolean isRefreshEnabled = true;	
	// 在刷新中标记
	private boolean isRefreshing = false;

	public RefreshableView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();

	}

	private void init() {
		// 滑动对象，
		scroller = new Scroller(mContext);
		headView = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.layout_listview_pulldown_head, null);
		ivScroll = (ImageView) headView.findViewById(R.id.ImageView_scroll);
		tvFresh = (TextView) headView.findViewById(R.id.TextView_fresh);
		ivScroll.setImageResource(R.drawable.loading_animation);
		animationDrawable = (AnimationDrawable) ivScroll
				.getDrawable();
		animationDrawable.start();

		LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, -refreshTargetTop);
		lp.topMargin = refreshTargetTop;
		lp.gravity = Gravity.CENTER;
		addView(headView, lp);
	}
	
	public void setTvFreshIsVisible(boolean isVisible){
		if (isVisible) {
			tvFresh.setVisibility(View.VISIBLE);
		} else {
			tvFresh.setVisibility(View.GONE);
		}
	}

	/**
	 * 刷新
	 * 
	 * @param time
	 */
	private void setRefreshText(String time) {
		// TODO Auto-generated method stub
		// timeTextView.setText(time);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int y = (int) event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录下y坐标
			lastY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "ACTION_MOVE");
			// y移动坐标
			int m = y - lastY;
			if (((m < 6) && (m > -1)) || (!isDragging)) {
				doMovement(m);
			}
			// 记录下此刻y坐标
			this.lastY = y;
			break;

		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ACTION_UP");

			fling();

			break;
		}
		return true;
	}

	/**
	 * up事件处理
	 */
	private void fling() {
		LinearLayout.LayoutParams lp = (LayoutParams) headView.getLayoutParams();
		
		Log.i(TAG, "fling()" + lp.topMargin);
		
		if (lp.topMargin > 0) {// 拉到了触发可刷新事件
			refresh();
		} else {
			returnInitState();
		}
	}

	private void returnInitState() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.headView.getLayoutParams();
		int i = lp.topMargin;
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}

	private void refresh() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.headView.getLayoutParams();
		int i = lp.topMargin;
		scroller.startScroll(0, i, 0, 0 - i);
		invalidate();
		if (refreshListener != null) {
			refreshListener.onRefresh(this);
			 isRefreshing = true;
		}
	}

	/**
	 * 
	 */
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			int i = this.scroller.getCurrY();
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.headView.getLayoutParams();
			int k = Math.max(i, refreshTargetTop);
			lp.topMargin = k;
			this.headView.setLayoutParams(lp);
			this.headView.invalidate();
			invalidate();
		}
	}

	/**
	 * 下拉move事件处理
	 * 
	 * @param moveY
	 */
	private void doMovement(int moveY) {
		LinearLayout.LayoutParams lp = (LayoutParams) headView.getLayoutParams();
		if (moveY > 0) {
			// 获取view的上边距
			float f1 = lp.topMargin;
			float f2 = moveY * 0.3F;
			int i = (int) (f1 + f2);
			// 修改上边距
			lp.topMargin = i;
			// 修改后刷新
			headView.setLayoutParams(lp);
			headView.invalidate();
			invalidate();
		}
		if (refreshTime != null) {
			setRefreshTime(refreshTime);
		}
//		if (lp.topMargin > 0) {
//		} else {
//		}

	}

	public void setRefreshEnabled(boolean b) {
		this.isRefreshEnabled = b;
	}

	public void setRefreshListener(RefreshListener listener) {
		this.refreshListener = listener;
	}

	/**
	 * 刷新时间
	 * 
	 * @param refreshTime2
	 */
	private void setRefreshTime(Long time) {
		// TODO Auto-generated method stub

	}

	/**
	 * 结束刷新事件
	 */
	public void finishRefresh() {
		Log.i(TAG, "执行了=====finishRefresh");
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.headView.getLayoutParams();
		int i = lp.topMargin;
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	    isRefreshing = false; 
	}

	/*
	 * 该方法一般和ontouchEvent 一起用 (non-Javadoc)
	 * 
	 * @see
	 * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		int action = e.getAction();
		int y = (int) e.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			// y移动坐标
			int m = y - lastY;

			// 记录下此刻y坐标
			this.lastY = y;
			if (m > 6 && canScroll()) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		case MotionEvent.ACTION_CANCEL:

			break;
		}
		return false;
	}

	private boolean canScroll() {
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof ListView) {
				int top = ((ListView) childView).getChildAt(0).getTop();
				int pad = ((ListView) childView).getListPaddingTop();
				if ((Math.abs(top - pad)) < 3
						&& ((ListView) childView).getFirstVisiblePosition() == 0) {
					return true;
				} else {
					return false;
				}
			} else if (childView instanceof ScrollView) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	/**
	 * 刷新监听接口
	 * 
	 * @author Nono
	 * 
	 */
	public interface RefreshListener {
		public void onRefresh(RefreshableView view);
	}
}
