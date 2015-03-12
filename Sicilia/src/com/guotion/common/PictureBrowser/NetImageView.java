package com.guotion.common.PictureBrowser;
import com.guotion.common.download.SimpleDownLoader;
import com.guotion.sicilia.R;
import com.guotion.sicilia.util.AndroidFileUtils;

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
import android.widget.TextView;
import android.widget.Toast;

public class NetImageView extends RelativeLayout{

	private final int PREPARED = 0;
	private final int DOWNLOADING = 1;
	private final int FINISHED = 2;
	private final int FAILED = 3;
	
	private Context context;
	private ImageView imageView;
	private ProgressBar progressBar;
	private TextView resultView;
	
	private String imageUrl;
	private String cacheFile = Environment.getExternalStorageDirectory().getAbsolutePath();
	private String fileNmae;
	
	private DownloadListener downloadListener;
	
	public NetImageView(Context context) {
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
				float num = (float)progressBar.getProgress() / (float)progressBar.getMax();
				int result = (int)(num * 100);
				resultView.setText(result+ "%");
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
		LayoutInflater.from(context).inflate(R.layout.net_image_view, this, true);
		imageView = (ImageView)findViewById(R.id.imageView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		resultView = (TextView) findViewById(R.id.resultView);
	}
	
	public void setImageUrl(String imageUrl) {
		if(this.imageUrl != null && this.imageUrl.equals(imageUrl)){
			return ;
		}
		this.imageUrl = imageUrl;
		if(fileNmae == null){
			fileNmae = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
			fileNmae = fileNmae.replace(":", "_");
		}
		initImg();
	}

	private void initImg(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				SimpleDownLoader simpleDownLoader = new SimpleDownLoader();
				simpleDownLoader.downLoad(imageUrl,cacheFile,fileNmae, new com.guotion.common.download.DownloadListener() {
					
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
						if(downloadListener != null){
							downloadListener.finished(filepath);
						}
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
		Bitmap bm = AndroidFileUtils.getBitmap(imagePath, 800, 800);
		imageView.setImageBitmap(bm);
		this.setProgressVisibility(View.INVISIBLE);
	}
	public void setThumbnailPath(String thumbnailPath) {
		imageView.setImageURI(Uri.parse(thumbnailPath));
	}
	public void setImage(Bitmap bitmap) {
		//this.imagePath = imagePath;
		imageView.setImageBitmap(bitmap);
	}
	
	public void setImage(int resId){
		imageView.setImageResource(resId);
	}
	private void setProgressVisibility(int v){
//		progressBar.setVisibility(v);
		resultView.setVisibility(v);
	}
	
	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	public void setFileNmae(String fileNmae) {
		this.fileNmae = fileNmae;
	}

	public void setDownloadListener(DownloadListener downloadListener) {
		this.downloadListener = downloadListener;
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		imageView.setOnClickListener(l);
	}
}
