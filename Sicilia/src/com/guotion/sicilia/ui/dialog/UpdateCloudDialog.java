package com.guotion.sicilia.ui.dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guotion.common.utils.CacheUtil;
import com.guotion.sicilia.R;
import com.guotion.sicilia.bean.net.CloudEvent;
import com.guotion.sicilia.bean.net.CloudItem;
import com.guotion.sicilia.constants.AndroidRequestCode;
import com.guotion.sicilia.data.AppData;
import com.guotion.sicilia.im.util.CloudManager;
import com.guotion.sicilia.ui.adapter.NetImageAdapter;
import com.guotion.sicilia.ui.listener.OnUpdateListener;
import com.guotion.sicilia.util.AndroidFileUtils;
import com.guotion.sicilia.util.PreferencesHelper;
import com.guotion.sicilia.util.ZipUtil;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCloudDialog extends Dialog implements View.OnClickListener{
	
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
	private List<String> picUrls = null;
	private List<CloudItem> cloudItems = null;
	private StringBuffer deleteCloudItemIds = null;
	private float size = 0.00F;
	private float usedSize = 0.00F;
	private final int UPDATE_STATE_SUCCESS = 10001;
	private final int UPDATE_STATE_ERROR = 10002;
	private final int FILE_HANDLE_ERROR = 10003;
	private GridView gvImages = null;
	private NetImageAdapter netImageAdapter = null;
	private CloudEvent cloudEvent = null;
	private CloudManager cloudManager = null;
	private OnUpdateListener<CloudEvent> updateCloudListener = null;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE_STATE_SUCCESS) {
				if (updateCloudListener != null) {
					updateCloudListener.onUpdate(cloudEvent);
				}
				dismiss();
				Toast.makeText(getContext(), "修改成功", Toast.LENGTH_LONG).show();				
			}else if (msg.what == UPDATE_STATE_ERROR) {
				Toast.makeText(getContext(), "修改失败，请重试", Toast.LENGTH_LONG).show();
			}else if (msg.what == FILE_HANDLE_ERROR) {
				Toast.makeText(getContext(), "文件压缩出错", Toast.LENGTH_LONG).show();
			}
			super.handleMessage(msg);
		}
		
	};
	
	public UpdateCloudDialog(Activity activity, CloudEvent cloudEvent) {
		super(activity,R.style.dialog_full_screen);
		setContentView(R.layout.dialog_upload_file);
		this.activity = activity;
		this.cloudEvent = cloudEvent;
		initData();
		initView();
		initListener();
	}
		
	private void initData() {
		filePaths = new LinkedList<String>();
		picUrls = new LinkedList<String>();
		cloudManager = new CloudManager();
		deleteCloudItemIds = new StringBuffer();
	}
	private void initView() {
		top =  (RelativeLayout) findViewById(R.id.relativeLayout1);		
		llBack = (LinearLayout)findViewById(R.id.LinearLayout_chat_back);
		ivBack = (ImageView) findViewById(R.id.ImageView_back);
		
		back = (TextView) findViewById(R.id.textView_back);
		upload = (TextView) findViewById(R.id.textView_upload);
		tvSize = (TextView) findViewById(R.id.textView_size);
		tvChooseSize = (TextView)findViewById(R.id.textView_choosed_file_size);
		progressBar = (ProgressBar) findViewById(R.id.ProgressBar_size);
		try {
			float capacity = Float.valueOf(AppData.getUser(getContext()).capacity);//System.out.println("capacity="+capacity);
			float size = (1073741824.00F - capacity)/1048576.0000F;//System.out.println("size="+size);
			//usedSize =  Math.round(size*100)/100;
			DecimalFormat df = new DecimalFormat("#.00");
			usedSize = Float.parseFloat(df.format(size));//System.out.println("usedSize="+usedSize);
		} catch (Exception e) {}		
		progressBar.setProgress((int)(usedSize+0.99F));
		tvSize.setText(usedSize + "M/1G");
		
		chooseFile = (ImageView) findViewById(R.id.imageView_choose_file);
		
		edFileName = (EditText) findViewById(R.id.editText_file_name);
		edFileName.setText(cloudEvent.name);
		
		edDesc = (EditText) findViewById(R.id.editText_dec);
		edDesc.setText(cloudEvent.desc);
		
		cbPrivate = (CheckBox)findViewById(R.id.imageView_private);
		if (cloudEvent.isPrivate.equals("1")) {
			cbPrivate.setChecked(true);
		}else {
			cbPrivate.setChecked(false);
		}
		
		gvImages = (GridView)findViewById(R.id.gridView_choosed_files);
		cloudItems = new Gson().fromJson(cloudEvent.files+"",new TypeToken<List<CloudItem>>(){}.getType());
		if (cloudItems != null) {
			for(CloudItem cloudItem : cloudItems){
				picUrls.add(cloudItem.url);
			}
		}
		netImageAdapter = new NetImageAdapter(getContext(), picUrls);
		gvImages.setAdapter(netImageAdapter);
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
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				picUrls.remove(position);
				deleteCloudItemIds.append(cloudItems.remove(position)._id+"|");	
				netImageAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void updateCloud(){
		final String fileName = edFileName.getText().toString();
		if(fileName == null || fileName.equals("")){
			Toast.makeText(activity, "云文件名不能为空", Toast.LENGTH_LONG).show();
			return ;
		}
		final String desc = edDesc.getText().toString();
		if(desc == null || desc.equals("")){
			Toast.makeText(activity, "云文件描述不能为空", Toast.LENGTH_LONG).show();
			return ;
		}
		final String isPrivate;
		if(cbPrivate.isChecked()){
			isPrivate = "1";
		}else{
			isPrivate = "0";
		}
		Toast.makeText(getContext(), "正在修改中，请稍等", Toast.LENGTH_LONG).show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File file = getFile();
					if (file == null) {
						handler.sendEmptyMessage(FILE_HANDLE_ERROR);
					}
					if (deleteCloudItemIds.length() > 0) {
						deleteCloudItemIds.deleteCharAt(deleteCloudItemIds.length() - 1);
					}
					CloudEvent cloud = cloudManager.updateCloudFile(cloudEvent._id, fileName, desc, isPrivate, file, deleteCloudItemIds.toString());
					if (cloud != null) {
						cloudEvent = cloud;
						handler.sendEmptyMessage(UPDATE_STATE_SUCCESS);
					}else {
						handler.sendEmptyMessage(UPDATE_STATE_ERROR);
					}
					
				} catch (Exception e) {
					handler.sendEmptyMessage(UPDATE_STATE_ERROR);
				}
			}
		}).start();
	}
	
	private File getFile(){
		String time = System.currentTimeMillis()+"";		
		String zipPath = CacheUtil.cloudImageCachePath+"/"+time+".zip";
		try {
			ZipUtil.compress(filePaths, zipPath, "GBK", "云压缩文件");
			return new File(zipPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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



	
	
	public void setOnUpdateListener(OnUpdateListener<CloudEvent> l) {
		this.updateCloudListener = l;
	}

	@Override
	public void onClick(View v) {
		if (v== llBack) {
			dismiss();
		}else if (v == upload) {
			updateCloud();
			dismiss();
		}else if (v == chooseFile) {
			AndroidFileUtils.openFileChooser(activity, AndroidRequestCode.REQ_CODE_UPDATE_FILE);
		}
	}

	public void addUpdateFile(String filePath) {System.out.println("filePath="+filePath);
		if (filePath != null) {			
			File file = new File(filePath);
			float fileSize = (float)file.length()/1048576.0000F;
			size += fileSize; 
			size = (float)(Math.round(size*100))/100;						
			filePaths.add(filePath);
			tvChooseSize.setText("附件大小:" + size+"M");
			tvChooseSize.setVisibility(View.VISIBLE);
			netImageAdapter.notifyDataSetChanged();
		}
	}
}
