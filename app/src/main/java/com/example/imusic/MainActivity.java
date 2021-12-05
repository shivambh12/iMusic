package com.example.imusic;
import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
      ListView listView;
       String[] items;
     @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

         listView=findViewById(R.id.listview);
        runtimePermission();

    }
    public void runtimePermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                diplaysongs();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                 permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> fetchsong(File file)
    {
        //Log.d("shivam",file.getName());
        ArrayList<File> arrayList=new ArrayList<>();
        File[] songs=file.listFiles();
        if(songs!=null) {
            for (File myfile : songs) {
                if (!myfile.isHidden() && myfile.isDirectory()) {
                    arrayList.addAll(fetchsong(myfile));
                } else {
                    if (myfile.getName().endsWith(".mp3")&&!myfile.getName().startsWith(".")) {
                        arrayList.add(myfile);
                    }
                }
            }
        }
        return arrayList;
    }
    void diplaysongs()
    {
        final ArrayList<File> mysongs=fetchsong(Environment.getExternalStorageDirectory());
            items=new String[mysongs.size()];
            for(int i=0;i< mysongs.size();i++)
            {
                items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
            }
            ArrayAdapter<String> myadapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
            listView.setAdapter(myadapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(MainActivity.this,PlaySong.class);
                    String currentsong=listView.getItemAtPosition(position).toString();
                    intent.putExtra("songlist",mysongs);
                    intent.putExtra("currentsong",currentsong);
                    intent.putExtra("position",position);
                    startActivity(intent);

                }
            });
    }

}
