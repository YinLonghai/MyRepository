package com.guotion.common.PictureBrowser;

import com.guotion.common.net.FakeX509TrustManager;
import com.guotion.common.utils.MyUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SimpleNetImageView extends ImageView{
//	private String url;
	private String cachePath;
	
	private Bitmap bitmap;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(cachePath.equals(msg.obj)){
				setImageBitmap(bitmap);
			}
		}
	};

	public SimpleNetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SimpleNetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SimpleNetImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void loadImage(final String imgUrl,final String cachePath,int defaultImageResId){
		this.cachePath = cachePath;
		setImageResource(defaultImageResId);
		new Thread(new Runnable() {
			@Override
			public void run() {
				FakeX509TrustManager.allowAllSSL();
				bitmap = MyUtil.getbitmapFromUrl(imgUrl, cachePath);
				if(bitmap != null){
					Message msg = new Message();
					msg.what = 1;
					msg.obj = cachePath;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
}
