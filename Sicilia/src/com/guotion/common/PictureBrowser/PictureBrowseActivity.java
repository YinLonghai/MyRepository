package com.guotion.common.PictureBrowser;

import com.guotion.sicilia.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

public class PictureBrowseActivity extends Activity{
	
	PictureBrowseView pictureBrowseView;
    @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.picture_browes);  
        pictureBrowseView = (PictureBrowseView) findViewById(R.id.pictureBrowseView);  
        
        String[] imageUrls = {"http://192.168.2.103:8080/upload_server/files/sg.jpg",
        		"http://192.168.2.103:8080/upload_server/files/psb.jpg"};
        String[] imgPaths = {
        		Environment.getExternalStorageDirectory().getAbsolutePath()+"/sg.jpg",
        		Environment.getExternalStorageDirectory().getAbsolutePath()+"/psb.jpg"
        };
        pictureBrowseView.setImageUrls(imageUrls,imgPaths,null,1);
    }  
  
    
}
