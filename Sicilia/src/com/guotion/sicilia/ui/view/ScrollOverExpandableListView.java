package com.guotion.sicilia.ui.view;


import com.guotion.sicilia.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

public class ScrollOverExpandableListView extends ExpandableListView implements OnScrollListener {

	/** 松开更新 **/
	private final static int RELEASE_To_REFRESH = 0;
	/** 下拉更新 **/
	private final static int PULL_To_REFRESH = 1;
	/** 更新中 **/
	private final static int REFRESHING = 2;
	/** 无 **/
	private final static int DONE = 3;
	/** 加载中 **/
	private final static int LOADING = 4;
	/** 实际的padding的距离与界面上偏移距离的比例 **/
	private final static int RATIO = 3;

	private LayoutInflater inflater;
	private View headView;
	private ImageView ivScroll;
	private TextView tvFresh;
	private int headContentHeight;
	/** 第一个item **/
	private int firstItemIndex;
	/** 用于保证startY的值在一个完整的touch事件中只被记录一次 **/
	private boolean isRecored;
	/** 开始的Y坐标 **/
	private int startY;
	/** 状态 **/
	private int state;

	private boolean isBack;
	/** 是否要使用下拉刷新功能 **/
	public boolean showRefresh = true;

	public static boolean canRefleash = true;
	private int mLastY;
	private int mBottomPosition;

	public ScrollOverExpandableListView(Context context) {
		super(context);
		initView(context);
	}

	public ScrollOverExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ScrollOverExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(
				R.layout.layout_listview_pulldown_head, null);
		ivScroll = (ImageView) headView.findViewById(R.id.ImageView_scroll);
		tvFresh = (TextView) headView.findViewById(R.id.TextView_fresh);
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		/** 列表添加头部 **/
		addHeaderView(headView, null, false);
		setOnScrollListener(this);
		ivScroll.setImageResource(R.drawable.loading_animation);
		AnimationDrawable animationDrawable = (AnimationDrawable)ivScroll.getDrawable();
		animationDrawable.start();
		// ivScroll的动画
	}
	
	public void setTvFreshIsVisible(boolean isVisible){
		if (isVisible) {
			tvFresh.setVisibility(View.VISIBLE);
		} else {
			tvFresh.setVisibility(View.GONE);
		}
	}

	/**触摸事件的处理**/
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		final int y = (int) ev.getRawY();
		cancelLongPress();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {  //按下的时候
			if (firstItemIndex == 0 && !isRecored) {
				isRecored = true;
				startY = (int) ev.getY();
			}
			// ===========================
			mLastY = y;
			final boolean isHandled = mOnScrollOverListener.onMotionDown(ev);
			if (isHandled) {
				mLastY = y;
				return isHandled;
			}
			break;
		}

		case MotionEvent.ACTION_MOVE: {   //手指正在移动的时候
			int tempY = (int) ev.getY();
			if (showRefresh) {
				
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							changeHeaderViewByState();

						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);
					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}
			}
			// ==============================================
			final int childCount = getChildCount();
			if (childCount == 0)
				return super.onTouchEvent(ev);
			final int deltaY = y - mLastY;
			final boolean isHandleMotionMove = mOnScrollOverListener.onMotionMove(ev, deltaY);

			if (isHandleMotionMove) {
				mLastY = y;
				return true;
			}

			break;
		}

		case MotionEvent.ACTION_UP: {   //手指抬起来的时候
			if (state != REFRESHING && state != LOADING) {
				if (state == DONE) {
					// 什么都不做
				}
				if (state == PULL_To_REFRESH) {
					state = DONE;
					changeHeaderViewByState();
				}

				if (state == RELEASE_To_REFRESH) {
					state = REFRESHING;
					changeHeaderViewByState();

					canRefleash = true;

				}
			}

			isRecored = false;

			// /======================
			final boolean isHandlerMotionUp = mOnScrollOverListener.onMotionUp(ev);
			if (isHandlerMotionUp) {
				mLastY = y;
				return true;
			}
			break;
		}
		}
		mLastY = y;
		return super.onTouchEvent(ev);
	}
	
	
	
	
	
	
	
	

	/** 空的 */
	private OnScrollOverListener mOnScrollOverListener = new OnScrollOverListener() {

		@Override
		public boolean onExpandableListViewTopAndPullDown(int delta) {
			return false;
		}
		
		@Override
		public boolean onMotionDown(MotionEvent ev) {
			return false;
		}

		@Override
		public boolean onMotionMove(MotionEvent ev, int delta) {
			return false;
		}

		@Override
		public boolean onMotionUp(MotionEvent ev) {
			return false;
		}

	};

	// =============================== public method
	/**
	 * 可以自定义其中一个条目为头部，头部触发的事件将以这个为准，默认为第一个
	 * 
	 * @param index  正数第几个，必须在条目数范围之内
	 */
	public void setTopPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setTopPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Top position must > 0");
	}

	/**
	 * 设置这个Listener可以监听是否到达顶端，或者是否到达低端等事件</br>
	 * 
	 * @see OnScrollOverListener
	 */
	public void setOnScrollOverListener(
			OnScrollOverListener onScrollOverListener) {
		mOnScrollOverListener = onScrollOverListener;
	}

	/**
	 * 滚动监听接口
	 * 
	 * @see MainScrollOverListView#setOnScrollOverListener(OnScrollOverListener)
	 * 
	 */
	public interface OnScrollOverListener {
		/**
		 * 到达最顶部触发
		 * 
		 * @param delta
		 *            手指点击移动产生的偏移量
		 * @return
		 */
		boolean onExpandableListViewTopAndPullDown(int delta);
		/**
		 * 手指触摸按下触发，相当于{@link MotionEvent#ACTION_DOWN}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionDown(MotionEvent ev);

		/**
		 * 手指触摸移动触发，相当于{@link MotionEvent#ACTION_MOVE}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionMove(MotionEvent ev, int delta);

		/**
		 * 手指触摸后提起触发，相当于{@link MotionEvent#ACTION_UP}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionUp(MotionEvent ev);

	}

	
	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
		firstItemIndex = firstVisiableItem;
		if(scrollListener != null){
			scrollListener.onScroll(arg0, firstVisiableItem, arg2, arg3);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollListener != null){
			scrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void onRefreshComplete() {
		state = DONE;
		changeHeaderViewByState();
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:	
			break;
		case PULL_To_REFRESH:
			
			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			break;
		}
	}
	private ScrollListener scrollListener;
	
	public void setScrollListener(ScrollListener scrollListener) {
		this.scrollListener = scrollListener;
	}

	public interface ScrollListener{
		public void onScroll(AbsListView view, int firstVisiableItem, int arg2, int arg3);
		public void onScrollStateChanged(AbsListView view, int scrollState);
	}
}
