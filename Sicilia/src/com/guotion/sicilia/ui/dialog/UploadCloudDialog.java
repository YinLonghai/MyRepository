package com.guotion.sicilia.ui.dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.guotion.common.upload.Uploader2;
import com.guotion.common.upload.UploaderListener;
import com.guotion.common.utils.CacheUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.constant.ChatServerConstant;
import com.guotion.sicilia.im.util.GsonTransferUtil;
import com.guotion.sicilia.ui.adapter.ImageAdapter;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.ZipUtil;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UploadCloudDialog extends Dialog implements View.OnClickListener{

	private TextView back;
	private TextView upload;
	private TextView tvSize;
	private TextView tvChooseSize;
	private ProgressBar progressBar;
	private ImageView chooseFile;
	private EditText edFileName;
	private EditText edDesc;
	private RelativeLayout top;
	private ImageView ivBack;
	private LinearLayout llBack;
	private CheckBox cbPrivate;
	private Activity activity = null;
	private List<String> filePaths = null;
	private float size = 0.00F;
	private float usedSize = 0.00F;
	private final int UPLOAD_STATE_SUCCESS = 10001;
	private final int UPLOAD_STATE_ERROR = 10002;
	private GridView gvImages = null;
	private ImageAdapter imageAdapter = null;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPLOAD_STATE_SUCCESS) {
				imageAdapter.notifyDataSetChanged();
				Toast.makeText(getContext(), "上传成功", Toast.LENGTH_LONG).show();				
			}else if (msg.what == UPLOAD_STATE_ERROR) {
				imageAdapter.notifyDataSetChanged();
				Toast.makeText(getContext(), "上传失败，请重试", Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}
		
	};
	public UploadCloudDialog(Activity activity) {
		super(activity,R.style.dialog_full_screen);
		setContentView(R.layout.dialog_upload_file);
		this.activity = activity;
		initData();
		initView();
		initListener();
	}
	
	private void initData() {
		filePaths = new LinkedList<String>();
	}
	private void initView() {
		back = (TextView) findViewById(R.id.textView_back);
		upload = (TextView) findViewById(R.id.textView_upload);
		tvSize = (TextView) findViewById(R.id.textView_size);
		tvChooseSize = (TextView)findViewById(R.id.textView_choosed_file_size);
		progressBar = (ProgressBar) findViewById(R.id.ProgressBar_size);
		try {
			float capacity = Float.valueOf(AppData.getUser(getContext()).capacity);
			float size = (1073741824.00F - capacity)/1048576.0000F;
			//usedSize =  Math.round(size*100)/100;		
			DecimalFormat df = new DecimalFormat("#.00");
			usedSize = Float.parseFloat(df.format(size));
		} catch (Exception e) {}
		progressBar.setProgress((int)(usedSize+0.99F));
		tvSize.setText(usedSize + "M/1G");
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);
		chooseFile = (ImageView) findViewById(R.id.imageView_choose_file);
		edFileName = (EditText) findViewById(R.id.editText_file_name);
		edDesc = (EditText) findViewById(R.id.editText_dec);
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		cbPrivate = (CheckBox)findViewById(R.id.imageView_private);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		gvImages = (GridView)findViewById(R.id.gridView_choosed_files);
		imageAdapter = new ImageAdapter(getContext(), filePaths);
		gvImages.setAdapter(imageAdapter);
		updateTheme();
	}
	private void updateTheme(){
		int theme = new PreferencesHelper(getContext()).getInt(AppData.THEME);
		int color ;
		try{
			switch(theme){
			case AppData.THEME_MALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				upload.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			case AppData.THEME_RED:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				upload.setTextColor(color);
				break;
			case AppData.THEME_BLUE:
				top.setBackgroundResource(AppData.getThemeImgResId(theme, "bg_title"));
				color = getContext().getResources().getColor(AppData.getThemeColor(theme));
				back.setTextColor(color);
				upload.setTextColor(color);
				break;
			case AppData.THEME_FEMALE:
				top.setBackgroundResource(AppData.getThemeColor(theme));
				back.setTextColor(getContext().getResources().getColor(R.color.white));
				upload.setTextColor(getContext().getResources().getColor(R.color.white));
				break;
			}
			progressBar.setProgressDrawable(getContext().getResources().getDrawable(AppData.getThemeImgResId(theme, "progressbar")));
			chooseFile.setImageResource(AppData.getThemeImgResId(theme, "select_file"));
			ivBack.setImageResource(AppData.getThemeImgResId(theme, "back"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void initListener() {
		llBack.setOnClickListener(this);
		upload.setOnClickListener(this);
		chooseFile.setOnClickListener(this);
	}
	
	private void uploadFile(){
		final String fileName = edFileName.getText().toString();
		if(fileName == null || fileName.equals("")){
			return ;
		}
		final String desc = edDesc.getText().toString();
		if(desc == null || desc.equals("")){
			return ;
		}
		final String isPrivate;
		if(cbPrivate.isChecked()){
			isPrivate = "1";
		}else{
			isPrivate = "0";
		}
		Toast.makeText(getContext(), "已加入上传队列，请稍等", Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {
			@Override
			public void run() {System.out.println("upload file....");
				String actionUrl=ChatServerConstant.URL.SERVER_HOST+"/Cloud/Create";
				HashMap<String,String> params = new HashMap<String, String>();
				params.put("id_", AppData.getUser(activity)._id);
				params.put("name", fileName);
				params.put("desc", desc);
				params.put("isPrivate", isPrivate);
//				HashMap<String, File> files = new HashMap<String, File>();
//				files.put("chat", new File(CacheUtil.cachePath+"/chat.zip"));
				Uploader2.uploadCloud(actionUrl, params, getFileMap(), new UploaderListener() {
					@Override
					public void onUploading(int size) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onPrepared(int fileSize) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onException(Exception e) {						
						handler.sendEmptyMessage(UPLOAD_STATE_ERROR);
						imageAdapter.clear();
					}
					@Override
					public void onError() {						
						handler.sendEmptyMessage(UPLOAD_STATE_ERROR);
						imageAdapter.clear();
					}
					@Override
					public void finished(byte[] data) {
						
						try {
							CloudEvent cloudEvent = (CloudEvent) GsonTransferUtil.transferToObject(new String(data), new CloudEvent());
							if(uploadCloudListener != null){
								uploadCloudListener.uploadCloud(cloudEvent);
							}
							handler.sendEmptyMessage(UPLOAD_STATE_SUCCESS);
						} catch (Exception e) {
							handler.sendEmptyMessage(UPLOAD_STATE_ERROR);
						}
						imageAdapter.clear();
					}
				});
			}
		}).start();
	}
	
	public void addFile(String filePath){
		if (filePath != null) {			
			File file = new File(filePath);
			float fileSize = (float)file.length()/1048576.0000F;
			size += fileSize; 
			size = (float)(Math.round(size*100))/100;						
			filePaths.add(filePath);
			tvChooseSize.setText("附件大小:" + size+"M");
			tvChooseSize.setVisibility(View.VISIBLE);
			imageAdapter.notifyDataSetChanged();
		}
	}
	
	private Map<String, File> getFileMap(){
		Map<String, File> fileMap = new HashMap<String, File>();
		String time = System.currentTimeMillis()+"";		
		String zipPath = CacheUtil.cloudImageCachePath+"/"+time+".zip";
		try {
			ZipUtil.compress(filePaths, zipPath, "GBK", "云压缩文件");
			fileMap.put(time, new File(zipPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileMap;
	}
	
	
	@Override
	public void dismiss() {
		tvSize.setText("0.00M/1G");
		edDesc.setText("");
		edFileName.setText("");		
		size = 0.00F;
		tvChooseSize.setVisibility(View.GONE);
		super.dismiss();
	}



	private UploadCloudListener uploadCloudListener;
	
	public void setUploadCloudListener(UploadCloudListener uploadCloudListener) {
		this.uploadCloudListener = uploadCloudListener;
	}

	public interface UploadCloudListener{
		public void uploadCloud(CloudEvent cloudEvent);
	}

	@Override
	public void onClick(View v) {
		if (v== llBack) {
			dismiss();
		}else if (v == upload) {
			uploadFile();
			dismiss();
		}else if (v == chooseFile) {
			AndroidFileUtils.openFileChooser(activity, AndroidRequestCode.REQ_CODE_UPLOAD_FILE);
		}
	}
}
