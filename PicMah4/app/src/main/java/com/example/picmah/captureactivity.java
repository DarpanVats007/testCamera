package com.example.picmah;

import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Matrix;
        import android.hardware.Camera;
        import android.net.Uri;
        import android.util.AttributeSet;
        import android.hardware.camera2.CaptureRequest;
        import android.util.Log;

        import org.opencv.android.JavaCameraView;

        import java.io.FileOutputStream;
        import android.hardware.Camera;
        import android.hardware.Camera.PictureCallback;
        import java.util.List;
        import com.example.picmah.CameraFragment;

public class captureactivity extends JavaCameraView implements Camera.PictureCallback {


    private String mPictureFileName;

    public captureactivity(Context context, AttributeSet attrs) {
        super (context, attrs);
    }

    public void takePicture (final String fileNAme){

        this.mPictureFileName = fileNAme;
        mCamera.setPreviewCallback (null);
        mCamera.takePicture (null,null,this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        mCamera.startPreview ();
        mCamera.setPreviewCallback (this);

        try {
            FileOutputStream fos = new FileOutputStream (mPictureFileName);
            fos.write (data);
            fos.close ();
        }
        catch (java.io.IOException e) {

            Log.e ("PicDemo", "Extection in photo callback ", e);
        }

    }
}
