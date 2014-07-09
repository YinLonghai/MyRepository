package com.guotion.common.media;

/**
 * SongInfoUtils
 * 
 * this class is used to get the message of the assign audio file from database  
 * 
 * @author wenliang
 * Created :2011.06.22
 */

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class SongInfoUtils {
    /**
     * mSongsList
     */
    private ArrayList<Song> mSongsList = new ArrayList<Song>();

    /**
     * mContext
     */
    private Context mContext = null;

    /**
     * SongInfoUtils()
     * @param aContext
     */
    public SongInfoUtils(Context aContext) {
        mContext = aContext;
    }

    /**
     * getFileInfo()
     * @param aFileAbsoulatePath
     * @return
     */
    public String[] getFileInfo(String aFileAbsoulatePath) {
        String[] fileMessage = new String[1];
        File file = new File(aFileAbsoulatePath);
        String fileName = file.getName();
        //String filePath = "/mnt" + file.getPath();
        String filePath = file.getPath();

        if (file.exists()) {System.out.println("aaa--"+fileName);
            if (mContext != null) {System.out.println("ccc---"+filePath);
                readDataFromSD();

                int count = mSongsList.size();//System.out.println("count=="+count);
                for (int i = 0; i < count; i++) {System.out.println(mSongsList.get(i).getmFilePath()+"---"+mSongsList.get(i).getmFileName());
                    if (mSongsList.get(i).getmFilePath().equals(filePath)
                            && mSongsList.get(i).getmFileName()
                                    .equals(fileName)) {
                        fileMessage[0] = mSongsList.get(i).getmDuration()/1000+"";
//                        fileMessage[1] = mSongsList.get(i).getmAlbum();
//                        fileMessage[2] = mSongsList.get(i).getmSinger();
                        break;
                    }
                }
            }
        }

        return fileMessage;
    }
    
    /**
     * readDataFromSD()
     */
    public void readDataFromSD() {

//        Cursor cursor = mContext.getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                new String[] { MediaStore.Audio.Media._ID,
//                        MediaStore.Audio.Media.DISPLAY_NAME,
//                        MediaStore.Audio.Media.TITLE,
//                        MediaStore.Audio.Media.DURATION,
//                        MediaStore.Audio.Media.ARTIST,
//                        MediaStore.Audio.Media.ALBUM,
//                        MediaStore.Audio.Media.YEAR,
//                        MediaStore.Audio.Media.MIME_TYPE,
//                        MediaStore.Audio.Media.SIZE,
//                        MediaStore.Audio.Media.DATA },
//                MediaStore.Audio.Media.MIME_TYPE + "=? or "
//                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
//                new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);
        Cursor cursor = mContext.getContentResolver().query(
        		MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        		new String[] { MediaStore.Audio.Media._ID,
                      MediaStore.Audio.Media.DISPLAY_NAME,
                      MediaStore.Audio.Media.TITLE,
                      MediaStore.Audio.Media.DURATION,
                      MediaStore.Audio.Media.ARTIST,
                      MediaStore.Audio.Media.ALBUM,
                      MediaStore.Audio.Media.YEAR,
                      MediaStore.Audio.Media.MIME_TYPE,
                      MediaStore.Audio.Media.SIZE,
                      MediaStore.Audio.Media.DATA },null,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            getSongList(cursor);
        }
        return;
    }
    
    /**
     * getSongList()
     * @param cursor
     */
    public void getSongList(Cursor cursor) {
        Song song = null;
        do {
            song = new Song();
            song.setmFileName(cursor.getString(1));// file Name
            song.setmFileTitle(cursor.getString(2));// song name
            song.setmDuration(cursor.getInt(3));// play time
            song.setmSinger(cursor.getString(4));// artist
            song.setmAlbum(cursor.getString(5));// album
            if (cursor.getString(6) != null) {
                song.setmYear(cursor.getString(6));
            } else {
                song.setmYear("undefine");
            }
//            if ("audio/mpeg".equals(cursor.getString(7).trim())) {// file type
//                song.setmFileType("mp3");
//            } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
//                song.setmFileType("wma");
//            }
            if (cursor.getString(8) != null) {// fileSize
                float temp = cursor.getInt(8) / 1024f / 1024f;
                String sizeStr = (temp + "").substring(0, 4);
                song.setmFileSize(sizeStr + "M");
            } else {
                song.setmFileSize("undefine");
            }

            if (cursor.getString(9) != null) {//file path
                song.setmFilePath(cursor.getString(9));
            }

            mSongsList.add(song);
        } while (cursor.moveToNext());

        cursor.close();

        return;
    }
}
 
