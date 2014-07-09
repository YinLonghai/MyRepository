package com.guotion.sicilia.util;

import android.text.TextPaint;
import android.widget.TextView;

public class TextBold {
 public static void setTextBold(TextView textView){
	 TextPaint textPaint = textView.getPaint();
	 textPaint.setFakeBoldText(true);
 }
}
