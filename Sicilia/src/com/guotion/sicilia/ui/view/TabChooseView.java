package com.guotion.sicilia.ui.view;

import com.guotion.sicilia.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class TabChooseView extends LinearLayout implements OnClickListener{
	private LinearLayout llTabChoose;
	private TabItemView[] tabItemViews;
	private OnSelectChangeListener selectChangeListener = null;
	private int selected = 0;
	public TabChooseView(Context context) {
		super(context);
		initView(context);
	}
	public TabChooseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_tab_choose, this, true);
		llTabChoose = (LinearLayout)view.findViewById(R.id.LinearLayout_tabChoose);
	}
	private void initListener() {
		for (TabItemView tabItemView: tabItemViews) {
			tabItemView.setOnClickListener(this);
		}
		
	}
	
	public void setSelected(int selected){
		if (this.selected == selected) {
			return;
		} else {
			tabItemViews[this.selected].isSelected(false);
			tabItemViews[selected].isSelected(true);
			this.selected = selected;
		}
	}
	
	public void setOnSelectChangeListener(OnSelectChangeListener l){
		this.selectChangeListener = l;
	}
	
	public void setTabNum(int num){
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		tabItemViews = new TabItemView[num];
		for (int i = 0; i < tabItemViews.length; i++) {
			tabItemViews[i] = new TabItemView(getContext());
			llTabChoose.addView(tabItemViews[i], params);
		}
		tabItemViews[selected].isSelected(true);
		initListener();
	}

	public void setTitle(String[] titles){
		for (int i = 0; i < titles.length; i++) {
			tabItemViews[i].setTitle(titles[i]);
		}
	}
	public void setTitle(int[] titles){
		for (int i = 0; i < titles.length; i++) {
			tabItemViews[i].setTitle(titles[i]);
		}
	}
	/**
	 * 
	 * @param icons 样式：{
	 * 					{R.drawable.ic_normal1,R.drawable.ic_prossed1},
	 * 					{R.drawable.ic_normal2,R.drawable.ic_prossed2},
	 * 				}
	 */
	public void setIcon(int[][] icons){
		for (int i = 0; i < icons.length; i++) {
			tabItemViews[i].setIcon(icons[i]);
		}
	}
	public interface OnSelectChangeListener{
		/**
		 * 当前选中的项
		 * @param selected
		 */
		public void selectChange(int selected);
	}

	@Override
	public void onClick(View v) {
		for (int i = 0; i < tabItemViews.length; i++) {
			if (v == tabItemViews[i]) {
				if (selected == i) {
					return;
				}else {
					setSelected(i);
					selectChangeListener.selectChange(i);
					break;
				}
				
			}
		}
		
	};
	
}
