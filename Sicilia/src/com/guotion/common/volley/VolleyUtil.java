package com.guotion.common.volley;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.guotion.common.net.FakeX509TrustManager;
import com.guotion.common.utils.CacheUtil;
import com.guotion.common.utils.ImgUtil;

public class VolleyUtil {
	private RequestQueue requestQueue;
	private DownloadImageListener downloadImageListener = null;
	//强引用
	//private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
	//弱引用
	private HashMap<String,SoftReference<Bitmap>> bitmapCaches = new HashMap<String, SoftReference<Bitmap>>();
	
	private ImageCache imageCache;
	
	private String url;
	
	public static final int GET = Request.Method.GET;
	public static final int POST = Request.Method.POST;
	
	public VolleyUtil(Context context){
		requestQueue = Volley.newRequestQueue(context);
		imageCache = new ImageCache() {  
            @Override  
            public void putBitmap(String key, final Bitmap value) { 
            	bitmapCaches.put(key,new SoftReference<Bitmap>(value));  
                if(downloadImageListener != null)
                	downloadImageListener.finished(value);
                final String imgUrl = url;
                new Thread(new Runnable() {
					@Override
					public void run() {
						CacheUtil.getInstence().cacheImage(value, CacheUtil.avatarCachePath, imgUrl.substring(imgUrl.lastIndexOf("/")+1));
					}
				}).start();
               
            }
            @Override  
            public Bitmap getBitmap(String key) {  
            	SoftReference<Bitmap> soft = bitmapCaches.get(key);
            	if(soft == null){
            		return null;
            	}
                return soft.get();  
            }  
        };
	}
	
	/** 
     * 利用Volley获取JSON数据 
     */  
	public void getJSONByVolley(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {    
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,url,jsonRequest,listener,errorListener);  
        requestQueue.add(jsonObjectRequest);  
    }
	public void getBitmap(final String imageUrl,final DownloadImageListener downloadImageListener){
		ImageRequest imgRequest=new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {  
            @Override  
            public void onResponse(Bitmap value) {  
            	bitmapCaches.put(imageUrl,new SoftReference<Bitmap>(value));
            	if(downloadImageListener != null)
                	downloadImageListener.finished(value);
            }  
        }, 300, 200, Config.ARGB_8888, new ErrorListener(){  
            @Override  
            public void onErrorResponse(VolleyError arg0) {  
                // TODO Auto-generated method stub   
            }             
        });  
		requestQueue.add(imgRequest);
	}
	public void getBitmap(final String imageUrl){
		getBitmap(imageUrl,downloadImageListener);
	}
    /**
     * 利用Volley异步加载图片 
     * @param imageUrl
     * @param view 显示图片的ImageView
     * @param defaultImageResId 默认显示的图片资源
     * @param errorImageResId 加载错误时显示的图片资源
     */
	public void loadImageByVolley(String imageUrl,ImageView view, int defaultImageResId, int errorImageResId){
		url = imageUrl;
//		FakeX509TrustManager.allowAllSSL();
//        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache); // System.out.println("imageLoader="+imageLoader);
//        ImageListener listener = ImageLoader.getImageListener(view, defaultImageResId,errorImageResId);  //System.out.println("listener="+listener);
//        imageLoader.get(imageUrl, listener);  
    }  
      
    /** 
     * 利用NetworkImageView显示网络图片 
     */  
	public void showImageByNetworkImageView(String imageUrl,NetworkImageView networkImageView){  
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);  
        networkImageView.setTag("url");  
        networkImageView.setImageUrl(imageUrl,imageLoader);  
    }

	public void setDownloadImageListener(DownloadImageListener downloadImageListener) {
		this.downloadImageListener = downloadImageListener;
	}  
	
	public void putCache(String key,Bitmap value){
		bitmapCaches.put(key, new SoftReference<Bitmap>(value));
	}
	public Bitmap getCache(String key){
		SoftReference<Bitmap> value = bitmapCaches.get(key);
		if(value != null)
			return value.get();
		return null;
	}
}
