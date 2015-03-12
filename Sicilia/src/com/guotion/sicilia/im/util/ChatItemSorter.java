package com.guotion.sicilia.im.util;

import java.util.ArrayList;
import java.util.List;
import com.guotion.sicilia.bean.net.ChatItem;

public class ChatItemSorter {
	
	/**
	 * 按照ChatItem中的date排序,升序
	 * @param items
	 * @return
	 */
	public static List<ChatItem> sortWithUp(List<ChatItem> items){
		return sort(1,items);
	}
	/**
	 * 按照ChatItem中的date排序,降序
	 * @param items
	 * @return
	 */
	public static List<ChatItem> sortWithDown(List<ChatItem> items){
		return sort(0,items);
	}
	
	private static List<ChatItem> sort(int dir,List<ChatItem> items){
		List<ChatItem> sortItem = new ArrayList<ChatItem>();
		for(int i = 0; i < items.size() ;){
			int maxValueIndex = i;
			for(int j = i + 1;j < items.size()  ;j++){
				if(dir == 1 && items.get(maxValueIndex).date.compareTo(items.get(j).date) > 0){ //升序
					maxValueIndex = j;
				}else if(dir == 0 && items.get(maxValueIndex).date.compareTo(items.get(j).date) < 0){
					maxValueIndex = j;
				}
			}
			sortItem.add(items.get(maxValueIndex));
			items.remove(maxValueIndex);
		}
		return sortItem;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
