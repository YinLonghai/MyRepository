package com.guotion.sicilia.ui.adapter;

import java.util.List;

public class TextWheelAdapter implements WheelAdapter {
	private List<String> list;
	public TextWheelAdapter(List<String> list){
		this.list = list;
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	@Override
	public int getMaximumLength() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
