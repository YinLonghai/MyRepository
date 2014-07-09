package com.guotion.sicilia.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.LocalImageCache;
import com.guotion.sicilia.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;


public class ExpressionUtil {
	public final String IP_VALIDATE = "http://gcore.oicp.net:8080";
	public static final String EXPRESSION_VALIDATE = "e0[0-9]{2}|e[1-9][0-9][0-9]";
	public static final String IMAGE_VALIDATE = "^\\[图片\\]$";
	public static final String AUDIO_VALIDATE = "^\\[语音\\]$";

	private static SpannableString deal(Context context,String str, Pattern patten,Handler handler){
		SpannableString spannableString = new SpannableString(str);
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			handler.handle(spannableString,key,matcher.start(), matcher.end());
		}
		return spannableString;
	}
	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替（处理表情）
	 * 
	 * @param context
	 */
	public static SpannableString dealExpression(final Context context,String str){
		
		return deal(context,str,Pattern.compile(EXPRESSION_VALIDATE, Pattern.CASE_INSENSITIVE),new Handler() {
			@Override
			public void handle(SpannableString spannableString,String key,int start,int end) {
//				Field field;
//				try {
//					field = R.drawable.class.getDeclaredField(key);
//					int resId = Integer.parseInt(field.get(null).toString()); // 通过上面匹配得到的字符串来生成图片资源id
//					if (resId != 0) {
//						imgReplaceString(spannableString,resId,context,start,end);
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
	}
	// 处理语音
	public static SpannableString dealAudio(final Context context,String str) {
		return deal(context,str,Pattern.compile(AUDIO_VALIDATE,Pattern.CASE_INSENSITIVE),new Handler() {
			@Override
			public void handle(SpannableString spannableString,String key,int start,int end) {
				//imgReplaceString(spannableString,R.drawable.ic_chat_voice_start,context,start,end);
			}
		});
	}
	//变换到放语音
	public static SpannableString toAudioPlay(Context context){
		SpannableString spannableString = new SpannableString("a");
		//imgReplaceString(spannableString,R.drawable.abc_ab_bottom_solid_dark_holo,context,0,1);
		return spannableString;
	}
	//变换到不放语音
	public static SpannableString toNoAudioPlay(Context context){
		SpannableString spannableString = new SpannableString("a");
		//imgReplaceString(spannableString,R.drawable.ic_chat_voice_start,context,0,1);
		return spannableString;
	}
	// 处理图片
	public static SpannableString dealImg(final Context context,String str,final String thumbnaiPath ,final String imgPath) {
//		SpannableString spannableString = new SpannableString(str);
//		ImageSpan imageSpan = new ImageSpan(BitmapFactory.decodeFile(imgPath));
//		spannableString.setSpan(imageSpan, 0, str.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
//		return spannableString;
		return deal(context,str,Pattern.compile(IMAGE_VALIDATE,Pattern.CASE_INSENSITIVE),new Handler() {
			@Override
			public void handle(SpannableString spannableString,String key,int start,int end) {
				if(thumbnaiPath != null){
//					Bitmap thumbnai = LocalImageCache.get().loadImageBitmap(context, thumbnaiPath);
//					if(thumbnai != null)
//						imgReplaceString(spannableString,thumbnai,start,end);
//					else{
//						if(imgPath != null){
//							Bitmap img = LocalImageCache.get().loadImageBitmap(context, imgPath);
//							if(img != null){
//								imgReplaceString(spannableString,AndroidFilesUtil.zoomIn(img, 480, 480),start,end);
//								AndroidFilesUtil.saveToSDCard(imgPath, thumbnaiPath, 100, 100);
//							}else
//								imgReplaceString(spannableString,R.drawable.ic_picture_failure,context,start,end);
//						}else
//							imgReplaceString(spannableString,R.drawable.ic_default_picture,context,start,end);
//					}
				}else{//System.out.println(imgPath);
					if(imgPath != null && !imgPath.equals("")){
						Bitmap bitmap = LocalImageCache.get().loadImageBitmap(imgPath);
						if(bitmap != null){
							imgReplaceString(spannableString,AndroidFileUtils.zoomIn(bitmap, 480, 480),start,end);
							return ;
						}
					}
					imgReplaceString(spannableString,R.drawable.default_img,context,start,end);
				}
			}
		});
	}

	/**
	 * 用图片去替换一段字符串
	 * 
	 * @param spannableString 所有的字符
	 * @param bitmap 图片
	 * @param start 字符串开始的位置
	 * @param end 字符串结束的位置
	 */
	private static void imgReplaceString(SpannableString spannableString,Drawable d, int start, int end) {
		ImageSpan imageSpan = new ImageSpan(d);
		spannableString.setSpan(imageSpan, start, end,Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
	}
	private static void imgReplaceString(SpannableString spannableString,Bitmap d, int start, int end) {
		ImageSpan imageSpan = new ImageSpan(d);
		spannableString.setSpan(imageSpan, start, end,Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
	}
	private static void imgReplaceString(SpannableString spannableString,int resId,Context context, int start, int end) {
		ImageSpan imageSpan = new ImageSpan(context,resId);
		spannableString.setSpan(imageSpan, start, end,Spanned.SPAN_INCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
	}
	private interface Handler{
		public void handle(SpannableString spannableString,String key,int start,int end);
	}
}
