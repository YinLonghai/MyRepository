package com.guotion.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.View;

public class ImgUtil {
	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 合并2张图片
	 * create the bitmap from a byte array
	 * 
	 * @param src
	 *            the bitmap object you want proecss
	 * @param watermark
	 *            the water mark above the src
	 * @return return a bitmap object ,if paramter's length is 0,return null
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
		// String tag = "createBitmap";
		// Log.d( tag, "create a new bitmap" );
		if (src == null) {
			return null;
		}

		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, 2, 0, null);
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}
	//压缩图片大小   
    public static Bitmap compressImage(Bitmap image) {   
  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中   
        int options = 100;   
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩          
            baos.reset();//重置baos即清空baos   
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中   
            options -= 10;//每次都减少10   
        }   
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中   
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片   
        return bitmap;   
    }  
    /**   
     * 将彩色图转换为灰度图   
     * @param img 位图   
     * @return  返回转换好的位图   
     */     
    public Bitmap convertGreyImg(Bitmap img) {     
        int width = img.getWidth();         //获取位图的宽     
        int height = img.getHeight();       //获取位图的高     
             
        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组     
             
        img.getPixels(pixels, 0, width, 0, 0, width, height);     
        int alpha = 0xFF << 24;      
        for(int i = 0; i < height; i++)  {     
            for(int j = 0; j < width; j++) {     
                int grey = pixels[width * i + j];     
                     
                int red = ((grey  & 0x00FF0000 ) >> 16);     
                int green = ((grey & 0x0000FF00) >> 8);     
                int blue = (grey & 0x000000FF);     
                     
                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);     
                grey = alpha | (grey << 16) | (grey << 8) | grey;     
                pixels[width * i + j] = grey;     
            }     
        }     
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);     
        result.setPixels(pixels, 0, width, 0, 0, width, height);     
        return result;     
    }
  //图片Url保存为位图并进行缩放操作   
  //通过传入图片url获取位图方法   
  public Bitmap returnBitMap(String url) {   
          URL myFileUrl = null;   
          Bitmap bitmap = null;   
          try {   
              myFileUrl = new URL(url);   
          } catch (MalformedURLException e) {   
              e.printStackTrace();   
          }   
          try {   
              HttpURLConnection conn = (HttpURLConnection) myFileUrl   
                      .openConnection();   
              conn.setDoInput(true);   
              conn.connect();   
              InputStream is = conn.getInputStream();   
              bitmap = BitmapFactory.decodeStream(is);   
              is.close();   
          } catch (IOException e) {   
              e.printStackTrace();   
          }   
          
          return bitmap;   
      }   
  //通过传入位图,新的宽.高比进行位图的缩放操作   
  public static Drawable resizeImage(Bitmap bitmap, int w, int h) {   
    
          // load the origial Bitmap   
          Bitmap BitmapOrg = bitmap;   
    
          int width = BitmapOrg.getWidth();   
          int height = BitmapOrg.getHeight();   
          int newWidth = w;   
          int newHeight = h;   
    
          // calculate the scale   
          float scaleWidth = ((float) newWidth) / width;   
          float scaleHeight = ((float) newHeight) / height;   
    
          // create a matrix for the manipulation   
          Matrix matrix = new Matrix();   
          // resize the Bitmap   
          matrix.postScale(scaleWidth, scaleHeight);   
          // if you want to rotate the Bitmap   
          // matrix.postRotate(45);   
    
          // recreate the new Bitmap   
          Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,   
                  height, matrix, true);   
    
          // make a Drawable from Bitmap to allow to set the Bitmap   
          // to the ImageView, ImageButton or what ever   
          return new BitmapDrawable(resizedBitmap);   
    
      }  

	/***
	 * 加载本地图片
	 * 
	 * @param context
	 *            ：主运行函数实例
	 * @param bitAdress
	 *            ：图片地址，一般指向R下的drawable目录
	 * @return
	 */
	public final Bitmap CreatImage(Context context, int bitAdress) {
		Bitmap bitmaptemp = null;
		bitmaptemp = BitmapFactory.decodeResource(context.getResources(),
				bitAdress);
		return bitmaptemp;
	}

	/***  
	* 图片的缩放方法  
	*  
	* @param bgimage  
	* ：源图片资源  
	* @param newWidth  
	* ：缩放后宽度  
	* @param newHeight  
	* ：缩放后高度  
	* @return  
	*/  
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {   
	// 获取这个图片的宽和高   
	int width = bgimage.getWidth();   
	int height = bgimage.getHeight();   
	// 创建操作图片用的matrix对象   
	Matrix matrix = new Matrix();   
	// 计算缩放率，新尺寸除原始尺寸   
	float scaleWidth = ((float) newWidth) / width;   
	float scaleHeight = ((float) newHeight) / height;   
	// 缩放图片动作   
	matrix.postScale(scaleWidth, scaleHeight);   
	Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,   
	matrix, true);   
	return bitmap;   
	}   

	/**  
	* 放大缩小图片  
	*/  
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {   
	   int width = bitmap.getWidth();   
	   int height = bitmap.getHeight();   
	   Matrix matrix = new Matrix();   
	   float scaleWidht = ((float) w / width);   
	   float scaleHeight = ((float) h / height);   
	   matrix.postScale(scaleWidht, scaleHeight);   
	   Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);   
	   return newbmp;   
	}   
	  
	/**  
	* 将Drawable转化为Bitmap  
	*/  
	public static Bitmap drawableToBitmap(Drawable drawable) {   
	   int width = drawable.getIntrinsicWidth();   
	   int height = drawable.getIntrinsicHeight();   
	   Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);   
	   Canvas canvas = new Canvas(bitmap);   
	   drawable.setBounds(0, 0, width, height);   
	   drawable.draw(canvas);   
	   return bitmap;   
	  
	}   
	  
	/**  
	* 获得圆角图片的方法  
	*/  
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {   
	  
	   Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);   
	   Canvas canvas = new Canvas(output);   
	  
	   final int color = 0xff424242;   
	   final Paint paint = new Paint();   
	   final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());   
	   final RectF rectF = new RectF(rect);   
	  
	   paint.setAntiAlias(true);   
	   canvas.drawARGB(0, 0, 0, 0);   
	   paint.setColor(color);   
	   canvas.drawRoundRect(rectF, roundPx, roundPx, paint);   
	  
	   paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));   
	   canvas.drawBitmap(bitmap, rect, rect, paint);   
	  
	   return output;   
	}   
	  
	/**  
	* 获得带倒影的图片方法  
	*/  
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {   
	   final int reflectionGap = 4;   
	   int width = bitmap.getWidth();   
	   int height = bitmap.getHeight();   
	  
	   Matrix matrix = new Matrix();   
	   matrix.preScale(1, -1);   
	  
	   Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);   
	  
	   Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);   
	  
	   Canvas canvas = new Canvas(bitmapWithReflection);   
	   canvas.drawBitmap(bitmap, 0, 0, null);   
	   Paint deafalutPaint = new Paint();   
	   canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);   
	  
	   canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);   
	  
	   Paint paint = new Paint();   
	   LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);   
	   paint.setShader(shader);   
	   // Set the Transfer mode to be porter duff and destination in   
	   paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));   
	   // Draw a rectangle using the paint with our linear gradient   
	   canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);   
	   return bitmapWithReflection;   
	}  
	
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
	}
	/** 
     * 根据指定的图像路径和大小来获取缩略图 
     * 此方法有两点好处： 
     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度， 
     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 
     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 
     *        用这个工具生成的图像不会被拉伸。 
     * @param imagePath 图像的路径 
     * @param width 指定输出图像的宽度 
     * @param height 指定输出图像的高度 
     * @return 生成的缩略图 
     */  
    private Bitmap getImageThumbnail(String imagePath, int width, int height) {  
        Bitmap bitmap = null;  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        // 获取这个图片的宽和高，注意此处的bitmap为null   
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        options.inJustDecodeBounds = false; // 设为 false   
        // 计算缩放比   
        int h = options.outHeight;  
        int w = options.outWidth;  
        int beWidth = w / width;  
        int beHeight = h / height;  
        int be = 1;  
        if (beWidth < beHeight) {  
            be = beWidth;  
        } else {  
            be = beHeight;  
        }  
        if (be <= 0) {  
            be = 1;  
        }  
        options.inSampleSize = be;  
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false   
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象   
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  
  
    /** 
     * 获取视频的缩略图 
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。 
     * @param videoPath 视频的路径 
     * @param width 指定输出视频缩略图的宽度 
     * @param height 指定输出视频缩略图的高度度 
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
     * @return 指定大小的视频缩略图 
     */  
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,int kind) {  
        Bitmap bitmap = null;  
        // 获取视频的缩略图   
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
        System.out.println("w"+bitmap.getWidth());  
        System.out.println("h"+bitmap.getHeight());  
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  
    
    public static Bitmap resizeBitmap(Bitmap paramBitmap, float width) {
		Bitmap bitmap = null;
		if (paramBitmap != null) {
			int w = paramBitmap.getWidth();
			int h = paramBitmap.getHeight();
			if ((w == 0 && h == 0) || w == width) {
				return bitmap;
			}
			float sx = width / w;
			try {
				Matrix matrix = new Matrix();
				matrix.postScale(sx, sx);
				bitmap = Bitmap.createBitmap(paramBitmap, 0, 0, w, h, matrix, true);
			} catch (Exception e) {
				bitmap = null;
			}
		}
		return bitmap;
	}

	public static Bitmap getBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static BitmapDrawable getDrawable(Context context, int resId) {
		BitmapDrawable bd = new BitmapDrawable(getBitmap(context, resId));
		return bd;
	}
}
