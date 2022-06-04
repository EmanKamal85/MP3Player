package com.example.mp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MusicAdapter adapter;

    ArrayList<String> audioList = new ArrayList<>();
    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("Media Directory", MEDIA_PATH);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MusicAdapter(audioList, this);
        recyclerView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else {
            //getAllAudioFiles();
            scanDeviceForMp3Files();
        }
    }


        public void scanDeviceForMp3Files(){
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
            String[] projection = {
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION
            };
            final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

            Cursor cursor = null;
            try {
                Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);
                if( cursor != null){
                    cursor.moveToFirst();

                    while( !cursor.isAfterLast() ){
                        String title = cursor.getString(0);
                        String artist = cursor.getString(1);
                        String path = cursor.getString(2);
                        String displayName  = cursor.getString(3);
                        String songDuration = cursor.getString(4);
                        cursor.moveToNext();
                        if(path != null && path.endsWith(".mp3")) {
                            audioList.add(path);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }

                // print to see list of mp3 files
                for( String file : audioList) {
                    Log.i("TAG", file);
                }

            } catch (Exception e) {
                Log.e("TAG", e.toString());
            }finally{
                if( cursor != null){
                    cursor.close();
                }
            }
        }


//    public void getAllAudioFiles(){
//
//        if (MEDIA_PATH != null){
//
//            File mainFile = new File(MEDIA_PATH);
//            File [] fileList = mainFile.listFiles();
//
//            if (fileList != null){
//                for (File file : fileList){
//                    Log.e("Media Path", file.toString());
//
//                    if (file.isDirectory()){
//                        scanFiles(file);
//                    } else{
//                        String filePath = file.getAbsolutePath();
//                        if (filePath.endsWith(".mp3")){
//                            audioList.add(filePath);
//                            adapter.notifyDataSetChanged();
//                        }
//
//                    }
//                }
//
//            }
//
//
//                }
//    }

//    private void scanFiles(File directory) {
//        if (directory != null){
//
//            File [] fileList = directory.listFiles();
//
//            for (File file : fileList){
//                Log.e("Media Path", file.toString());
//
//                if (file.isDirectory()){
//                    scanFiles(file);
//                } else{
//                    String filePath = file.getAbsolutePath();
//                    if (filePath.endsWith(".mp3")){
//                        audioList.add(filePath);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==1 && permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //getAllAudioFiles();
            scanDeviceForMp3Files();
        }
    }
}