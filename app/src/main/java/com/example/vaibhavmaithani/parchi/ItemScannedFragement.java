package com.example.vaibhavmaithani.parchi;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class ItemScannedFragement extends Fragment {
    ImageButton imgButton;
    EditText desc;
    Button submit;
    String pathFile;
    TextView gobackCancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imgButton= (ImageButton) getView().findViewById(R.id.camera_openId);
        desc= (EditText) getView().findViewById(R.id.imagedisc_id);
        submit= (Button) getView().findViewById(R.id.submitbtn_id);
        gobackCancel= (TextView) getView().findViewById(R.id.goback_id);

        if(Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPictureTakenAction();
            }
        });

        gobackCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return inflater.inflate(R.layout.fragment_item_scanned_fragement, container, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                Bitmap bitmap= BitmapFactory.decodeFile(pathFile);
                imgButton.setImageBitmap(bitmap);
            }
        }
    }


    private void displayPictureTakenAction() {
        Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile=null;
        try {
            photoFile=createPhotoFile();
            if(photoFile!=null){
                 pathFile=photoFile.getAbsolutePath();
                Uri photoUri= FileProvider.getUriForFile(getActivity().getApplicationContext(),"com.thecodecity.cameraandroid.fileprovider",photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePic,1);

            }
        }catch (Exception e){}
    }

    private File createPhotoFile() {
        String name=new SimpleDateFormat("yyyyyMMdd_HHmmss").format(new Date());
        File storageDir=getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image=File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {

        }
        return image;
    }

    public ItemScannedFragement(){}


}
