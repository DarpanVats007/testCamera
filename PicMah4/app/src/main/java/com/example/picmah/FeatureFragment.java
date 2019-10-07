package com.example.picmah;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeatureFragment extends Fragment {

//    private static final int IMAGE_CAPTURE_CODE = 1001;
//    private static final int PERMISSION_CODE = 1000;
//    Button mCaptureBtn;
//    ImageView mImageView;
//    String currentPhotoPath;
//    private AppExecutor mAppExcutor;
//    private Button mStartCamera;
//
//
//    Uri image_uri;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;


    private static final String FILE_PROVIDER_AUTHORITY = "com.example.picmah.fileprovider";
    private static String data;

    private AppExecutor mAppExcutor;

    private ImageView mImageView;


    private Button mStartCamera;

    private String mTempPhotoPath;

    private Bitmap mResultsBitmap;

    private FloatingActionButton mClear,mSave,mShare;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate (R.layout.fragment_feature,container,false);
        View view = inflater.inflate (R.layout.fragment_feature, container,false);

//        mCaptureBtn = view.findViewById (R.id.capture_image);
//        mImageView = view.findViewById (R.id.image_view);
//        mStartCamera = view.findViewById(R.id.startCamera);
        mAppExcutor = new AppExecutor();

        mImageView = view.findViewById (R.id.imageView);
        mClear = view.findViewById(R.id.clear);
        mSave = view.findViewById(R.id.Save);
        mShare = view.findViewById(R.id.Share);
        mStartCamera = view.findViewById(R.id.startCamera);

        mImageView.setVisibility(View.GONE);
        mShare.hide ();
        mSave.hide ();
        mClear.hide ();

        mStartCamera.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    // If you do not have permission, request it
                    ActivityCompat.requestPermissions((Activity) getActivity ().getApplicationContext (),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                    ActivityCompat.requestPermissions(getActivity (), new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION );

                }

                else {

                    // Permissions Granted
//                    try {
//                        createImageFile();
//                    } catch (IOException e) {
//                        e.printStackTrace ();
//                    }
//                    openCamera();
                    // Launch the camera if the permission exists
                    launchCamera();
                }
            }
        });

        mSave.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mAppExcutor.diskIO().execute(() -> {
                    // Delete the temporary image file
                    BitmapUtils.deleteImageFile(getContext (), mTempPhotoPath);

                    // Save the image
                    BitmapUtils.saveImage( getContext (), mResultsBitmap);

                });

                Toast.makeText (getActivity (), "Image Saved",Toast.LENGTH_SHORT).show ();

            }
        });

        mClear.setOnClickListener(v -> {
            // Clear the image and toggle the view visibility
            mImageView.setImageResource(0);
            mStartCamera.setVisibility(View.VISIBLE);
            mSave.hide ();
            mShare.hide ();
            mClear.hide ();

            mAppExcutor.diskIO().execute(() -> {
                // Delete the temporary image file
                BitmapUtils.deleteImageFile(getActivity (), mTempPhotoPath);
            });

        });

        mShare.setOnClickListener((View v) -> {

            mAppExcutor.diskIO().execute(() ->{
                // Delete the temporary image file
                BitmapUtils.deleteImageFile(getActivity (), mTempPhotoPath);

                // Save the image
                BitmapUtils.saveImage(getActivity (), mResultsBitmap);

            });

            // Share the image
            BitmapUtils.shareImage(getActivity (), mTempPhotoPath);



        });




        return view;

    }

//    private void openCamera(){
//
//
//
//        // add permissions
////        ContentValues values = new ContentValues ();
////        values.put (MediaStore.Images.Media.TITLE, "New Pic");
////        values.put (MediaStore.Images.Media.DESCRIPTION, "From Camera");
////        Context applicationContext = MainActivity.getContextOfApplication();
////        applicationContext.getContentResolver();
//
////        image_uri = applicationContext.getContentResolver ().insert (MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//        Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//        if (cameraIntent.resolveActivity(MainActivity.getContextOfApplication ().getPackageManager()) != null) {
//
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(getActivity (),
//                        "com.example.picmah.fileprovider",
//                        photoFile);
//                cameraIntent.putExtra (MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult (cameraIntent, IMAGE_CAPTURE_CODE);
//            }
//
//        }
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(getActivity (), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(getActivity (), mTempPhotoPath);
        }
    }
    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(MainActivity.getContextOfApplication ().getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getActivity ());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                data = mTempPhotoPath;

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(getActivity (),
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private void processAndSetImage() {

        // Toggle Visibility of the views
        mStartCamera.setVisibility(View.GONE);
        mSave.show ();
        mShare.show ();
        mClear.show ();
        mImageView.setVisibility(View.VISIBLE);

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(getActivity (), mTempPhotoPath);
        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);
    }
    public static String getData(){
        return data;
    }


}
