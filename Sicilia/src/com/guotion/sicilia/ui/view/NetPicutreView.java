package com.guotion.sicilia.ui.view;

import com.guotion.common.download.DownloadListener;
import com.guotion.common.download.SimpleDownLoader;
import com.guotion.sicilia.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NetPicutreView extends RelativeLayout{

	private final int PREPARED = 0;
	private final int DOWNLOADING = 1;
	private final int FINISHED = 2;
	private final int FAILED = 3;
	
	private Context context;
	private ImageView imageView;
	private ProgressBar progressBar;
	
	private String imageUrl;
	private String cacheFile = Environment.getExternalStorageDirectory().getAbsolutePath();
	private String fileNmae;
	
	public NetPicutreView(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case DOWNLOADING:
				int size = msg.getData().getInt("size");
				progressBar.setProgress(size);				
				break;
			case PREPARED:
				progressBar.setMax(msg.getData().getInt("filesize"));
				break;
			case FINISHED:
				String filepath = msg.getData().getString("filepath");
				setImagePath(filepath);
				break;
			case FAILED:
				Toast.makeText(context, "图片下载失败", Toast.LENGTH_SHORT).show();
				setProgressVisibility(View.INVISIBLE);
				break;
			}
		}
	};

	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_net_picture, this, true);
		imageView = (ImageView)findViewById(R.id.imageView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}
	
	public void setImageUrl(String imageUrl) {
		if(this.imageUrl != null && this.imageUrl.equals(imageUrl)){
			return ;
		}
		this.imageUrl = imageUrl;
		fileNmae = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
		initImg();
	}

	private void initImg(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SimpleDownLoader simpleDownLoader = new SimpleDownLoader();
				simpleDownLoader.downLoad(imageUrl,cacheFile,fileNmae, new DownloadListener() {
					
					@Override
					public void onPrepared(int filesize) {
						Message msg = new Message();
						msg.what = PREPARED;
						msg.getData().putInt("filesize", filesize);
						handler.sendMessage(msg);
					}
					
					@Override
					public void onException(Exception e) {
						Message msg = new Message();
						msg.what = FAILED;
						handler.sendMessage(msg);
						System.out.println("图片下载失败");
					}
					
					@Override
					public void onDownloadSize(int size) {
						Message msg = new Message();
						msg.what = DOWNLOADING;
						msg.getData().putInt("size", size);
						handler.sendMessage(msg);
					}
					
					@Override
					public void finished(String filepath) {
						Message msg = new Message();
						msg.what = FINISHED;
						msg.getData().putString("filepath", filepath);
						handler.sendMessage(msg);
						System.out.println("图片下载成功");
					}
				});
			}
		}).start();
	}
	public void setImagePath(String imagePath) {
		//this.imagePath = imagePath;
		imageView.setImageURI(Uri.parse(imagePath));
		this.setProgressVisibility(View.INVISIBLE);
	}
	public void setThumbnailPath(String thumbnailPath) {
		imageView.setImageURI(Uri.parse(thumbnailPath));
	}
	public void setImage(Bitmap bitmap) {
		//this.imagePath = imagePath;
		imageView.setImageBitmap(bitmap);
	}
	public void setProgressVisibility(int v){
		progressBar.setVisibility(v);
	}
	
	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	public void setFileNmae(String fileNmae) {
		this.fileNmae = fileNmae;
	}
	
}
