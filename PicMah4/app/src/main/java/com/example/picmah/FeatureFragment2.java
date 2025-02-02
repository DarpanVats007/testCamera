package com.example.picmah;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class FeatureFragment2 extends Fragment {

//    private static final int REQUEST_IMAGE_CAPTURE = 1;
//    private static final int REQUEST_STORAGE_PERMISSION = 1;
//
//    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
//
//    private AppExecutor mAppExcutor;
//
//    private ImageView mImageView;
//
//    private Button mStartCamera;
//
//    private String mTempPhotoPath;
//
//    private Bitmap mResultsBitmap;
//
//    private FloatingActionButton mClear,mSave,mShare;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
////        return inflater.inflate (R.layout.fragment_feature,container,false);
//        View view = inflater.inflate (R.layout.fragment_feature, container,false);
//
//        mAppExcutor = new AppExecutor();
//
//        mImageView = findViewById(R.id.imageView);
//        mClear = findViewById(R.id.clear);
//        mSave = findViewById(R.id.Save);
//        mShare = findViewById(R.id.Share);
//        mStartCamera = findViewById(R.id.startCamera);
//
//        mImageView.setVisibility(View.GONE);
//        mShare.hide ();
//        mSave.hide ();
//        mClear.hide ();
//
//
//        mStartCamera.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    // If you do not have permission, request it
//                    ActivityCompat.requestPermissions((Activity) getApplicationContext (),
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            REQUEST_STORAGE_PERMISSION);
//                } else {
//                    // Launch the camera if the permission exists
//                    launchCamera();
//                }
//
//            }
//        });
//
//
//
//        mSave.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//                mAppExcutor.diskIO().execute(() -> {
//                    // Delete the temporary image file
//                    BitmapUtils.deleteImageFile(getApplicationContext (), mTempPhotoPath);
//
//                    // Save the image
//                    BitmapUtils.saveImage(getApplicationContext (), mResultsBitmap);
//
//                });
//
//                Toast.makeText (getActivity (), "Image Saved",Toast.LENGTH_SHORT).show ();
//
//            }
//        });
//
//        mClear.setOnClickListener(v -> {
//            // Clear the image and toggle the view visibility
//            mImageView.setImageResource(0);
//            mStartCamera.setVisibility(View.VISIBLE);
//            mSave.hide ();
//            mShare.hide ();
//            mClear.hide ();
//
//            mAppExcutor.diskIO().execute(() -> {
//                // Delete the temporary image file
//                BitmapUtils.deleteImageFile(this, mTempPhotoPath);
//            });
//
//        });
//
//        mShare.setOnClickListener((View v) -> {
//
//            mAppExcutor.diskIO().execute(() ->{
//                // Delete the temporary image file
//                BitmapUtils.deleteImageFile(this, mTempPhotoPath);
//
//                // Save the image
//                BitmapUtils.saveImage(this, mResultsBitmap);
//
//            });
//
//            // Share the image
//            BitmapUtils.shareImage(this, mTempPhotoPath);
//
//        });
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        // Called when you request permission to read and write to external storage
//        switch (requestCode) {
//            case REQUEST_STORAGE_PERMISSION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // If you get permission, launch the camera
//                    launchCamera();
//                } else {
//                    // If you do not get permission, show a Toast
//                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
//                }
//                break;
//            }
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // If the image capture activity was called and was successful
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            // Process the image and set it to the TextView
//            processAndSetImage();
//        } else {
//
//            // Otherwise, delete the temporary image file
//            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
//        }
//    }
//
//    /**
//     * Creates a temporary image file and captures a picture to store in it.
//     */
//    private void launchCamera() {
//
//        // Create the capture image intent
//        Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the temporary File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = BitmapUtils.createTempImageFile(this);
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                ex.printStackTrace();
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//
//                // Get the path of the temporary file
//                mTempPhotoPath = photoFile.getAbsolutePath();
//
//                // Get the content URI for the image file
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        FILE_PROVIDER_AUTHORITY,
//                        photoFile);
//
//                // Add the URI so the camera can store the image
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//
//                // Launch the camera activity
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }
//
//
//    /**
//     * Method for processing the captured image and setting it to the TextView.
//     */
//    private void processAndSetImage() {
//
//        // Toggle Visibility of the views
//        mStartCamera.setVisibility(View.GONE);
//        mSave.show ();
//        mShare.show ();
//        mClear.show ();
//        mImageView.setVisibility(View.VISIBLE);
//
//        // Resample the saved image to fit the ImageView
//        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
//
//
//        // Set the new bitmap to the ImageView
//        mImageView.setImageBitmap(mResultsBitmap);
//    }
//
//


}
