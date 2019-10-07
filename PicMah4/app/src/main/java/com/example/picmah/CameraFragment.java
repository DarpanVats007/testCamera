package com.example.picmah;

import com.example.picmah.captureactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.opencv.utils.Converters;


public class CameraFragment extends Fragment implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int REQUEST_PERMISSION = 100;
    CameraBridgeViewBase mOpenCvCameraView;
    TextView tvName;
    private int w, h;
    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);

    int borderType = Core.BORDER_CONSTANT;

    Mat mat1,mat2,mat3;

    BaseLoaderCallback baseLoaderCallback;
    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;
    Mat img1;
    Mat descriptors2,descriptors1;
    MatOfKeyPoint keypoints1,keypoints2;
    static Mat trans;
    private Button mStartCamera;

    public static final String TAG = "YOUR-TAG-NAME";


    private void initializeOpenCVDependencies() throws IOException {
        mOpenCvCameraView.enableView();
        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        img1 = new Mat();
        Mat resizeimg = new Mat ();
        AssetManager assetManager = getActivity ().getAssets ();
        InputStream istr = assetManager.open("abc.jpg");






        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        Utils.bitmapToMat(bitmap, resizeimg);


        Size sz = new Size (200,250);
        Imgproc.resize (resizeimg, img1, sz);

        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGB2GRAY);
        img1.convertTo(img1, 0); //converting the image to match with the type of the cameras image

        descriptors1 = new Mat();

        keypoints1 = new MatOfKeyPoint ();
        detector.detect(img1, keypoints1);
        descriptor.compute(img1, keypoints1, descriptors1);




    }




    public CameraFragment() {

        Log.i(TAG, "Instantiated new " + this.getClass());
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate (R.layout.fragment_camera, container,false);

        getActivity ().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity ().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //..... Adding Camera permissions......//

        if(ContextCompat.checkSelfPermission(getActivity ().getApplicationContext (), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity (), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }


        // Loading OpenCV
        {

            if (OpenCVLoader.initDebug()) {

                Toast.makeText(getActivity ().getApplicationContext(), "OpenCV loaded sucessfully", Toast.LENGTH_SHORT).show();

            }
            else {

                Toast.makeText(getActivity ().getApplicationContext(), "Could not load OpenCV", Toast.LENGTH_SHORT).show();

            }
        }

        Log.d("verify",String.valueOf(OpenCVLoader.initDebug()));


        mOpenCvCameraView = (JavaCameraView) view.findViewById (R.id.myCameraView);
        mOpenCvCameraView.setVisibility (JavaCameraView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener (this);
//        tvName = (TextView) view.findViewById(R.id.text1);
        mStartCamera = view.findViewById(R.id.take_picture);

        mStartCamera.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {

//                Date sdf = new Date ();
//                String currentDateAndTime = sdf.toString ();
//                String fileName = Environment.getExternalStorageDirectory ().getPath () + "/sample" + currentDateAndTime + ".jpeg";
//
//                mOpenCvCameraView.takePicture(fileName);



            }
        });


        return view;

    }

    //....... Baseloader......//

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback (getContext ()) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected (status);
            switch (status) {

                case BaseLoaderCallback.SUCCESS: {
                    mOpenCvCameraView.enableView ();
                    try {
                        initializeOpenCVDependencies ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                    break;
                }
                default: {
                    super.onManagerConnected (status);
                }
                break;
            }
        }
    };




    @Override
    public void onCameraViewStarted(int width, int height) {
        w = width;
        h = height;



    }

    @Override
    public void onCameraViewStopped() {
//        mat1.release ();
    }

    private Mat findLargestRectangle(Mat original_image) {
        Mat imgSource = original_image;
        //Mat untouched = original_image.clone();

        //convert the image to black and white
        Imgproc.cvtColor(imgSource, imgSource, Imgproc.COLOR_BGR2GRAY);

        //convert the image to black and white does (8 bit)
        Imgproc.Canny(imgSource, imgSource, 50, 50);

        //apply gaussian blur to smoothen lines of dots
        Imgproc.GaussianBlur(imgSource, imgSource, new Size(5, 5), 5);

        //find the contours
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(imgSource, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        double maxArea = -1;
        int maxAreaIdx = -1;
        MatOfPoint temp_contour = contours.get(0); //the largest is at the index 0 for starting point
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        MatOfPoint2f maxCurve = new MatOfPoint2f();
        List<MatOfPoint> largest_contours = new ArrayList<MatOfPoint>();
        for (int idx = 0; idx < contours.size(); idx++) {
            temp_contour = contours.get(idx);
            double contourarea = Imgproc.contourArea(temp_contour);
            //compare this contour to the previous largest contour found
            if (contourarea > maxArea) {
                //check if this contour is a square
                MatOfPoint2f new_mat = new MatOfPoint2f( temp_contour.toArray() );
                int contourSize = (int)temp_contour.total();
                Imgproc.approxPolyDP(new_mat, approxCurve, contourSize*0.05, true);
                if (approxCurve.total() == 4) {
                    maxCurve = approxCurve;
                    maxArea = contourarea;
                    maxAreaIdx = idx;
                    largest_contours.add(temp_contour);
                }
            }
        }

        //create the new image here using the largest detected square
        Mat new_image = new Mat(imgSource.size(), CvType.CV_8U); //we will create a new black blank image with the largest contour
        Imgproc.cvtColor(new_image, new_image, Imgproc.COLOR_BayerBG2RGB);
        Imgproc.drawContours(new_image, contours, maxAreaIdx, new Scalar(255, 255, 255), 1); //will draw the largest square/rectangle

        double temp_double[] = maxCurve.get(0, 0);
        Point p1 = new Point(temp_double[0], temp_double[1]);
        Imgproc.circle(new_image, new Point(p1.x, p1.y), 20, new Scalar(255, 0, 0), 5); //p1 is colored red
        String temp_string = "Point 1: (" + p1.x + ", " + p1.y + ")";

        temp_double = maxCurve.get(1, 0);
        Point p2 = new Point(temp_double[0], temp_double[1]);
        Imgproc.circle(new_image, new Point(p2.x, p2.y), 20, new Scalar(0, 255, 0), 5); //p2 is colored green
        temp_string += "\nPoint 2: (" + p2.x + ", " + p2.y + ")";

        temp_double = maxCurve.get(2, 0);
        Point p3 = new Point(temp_double[0], temp_double[1]);
        Imgproc.circle(new_image, new Point(p3.x, p3.y), 20, new Scalar(0, 0, 255), 5); //p3 is colored blue
        temp_string += "\nPoint 3: (" + p3.x + ", " + p3.y + ")";

        temp_double = maxCurve.get(3, 0);
        Point p4 = new Point(temp_double[0], temp_double[1]);
        Imgproc.circle(new_image, new Point(p4.x, p4.y), 20, new Scalar(0, 255, 255), 5); //p1 is colored violet
        temp_string += "\nPoint 4: (" + p4.x + ", " + p4.y + ")";

//        TextView temp_text = (TextView)findViewById(R.id.temp_text);
//        temp_text.setText(temp_string);

        return new_image;
    }

    public Mat recognize(Mat aInputFrame) {

        Mat aInput = aInputFrame;

        Imgproc.cvtColor (aInputFrame, aInputFrame, Imgproc.COLOR_RGB2GRAY);
        descriptors2 = new Mat ();
        keypoints2 = new MatOfKeyPoint ();
        detector.detect (aInputFrame, keypoints2);
        descriptor.compute (aInputFrame, keypoints2, descriptors2);
//   N     Mat outputImg = new Mat();




        // Matching
        MatOfDMatch matches = new MatOfDMatch();
        if (img1.type() == aInputFrame.type()) {

            if (descriptors1.type () == descriptors2.type () && descriptors1.cols () == descriptors2.cols ()){
                matcher.match(descriptors1, descriptors2, matches);
            }

        } else {
            return aInputFrame;
        }

        List<DMatch> matchesList = matches.toList();

        //...for new code ...//
//   N     List<MatOfDMatch> matches2 = new ArrayList<MatOfDMatch> ();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for (int i = 0; i < matchesList.size(); i++) {
            Double dist = (double) matchesList.get(i).distance;
            if (dist < min_dist)
                min_dist = dist;
            if (dist > max_dist)
                max_dist = dist;
        }
        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
        for (int i = 0; i < matchesList.size(); i++) {
            if (matchesList.get(i).distance <= (1.5 * min_dist))
                good_matches.addLast(matchesList.get(i));
        }

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(good_matches);
        Imgproc.cvtColor (aInputFrame, aInputFrame, Imgproc.COLOR_GRAY2RGB);
        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();
        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
            return aInputFrame;
        }

        else {


        }



// ........ For poly lines
//        List<MatOfPoint> goodMatches2 = new ArrayList<> ();
//        for (int i = 0; i < matchesList.size(); i++) {
//            if (matchesList.get(i).distance <= (1.5 * min_dist)) {
//
//               goodMatches2.add(matchesList.get(i));
//            }
//        }


//        MatOfDMatch goodMatches = new MatOfDMatch();
//
//        goodMatches.fromList(good_matches);



        //....N      //............ratio test......//   (part needs to be done for drawing polylines)


//        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
//        for (Iterator<MatOfDMatch> iterator = matches2.iterator(); iterator.hasNext();) {
//            MatOfDMatch matOfDMatch = (MatOfDMatch) iterator.next();
//            if (matOfDMatch.toArray()[0].distance / matOfDMatch.toArray()[1].distance < 0.9) {
//                good_matches.add(matOfDMatch.toArray()[0]);
//            }
//        }
//
//        // get keypoint coordinates of good matches to find homography and remove outliers using ransac
//        List<Point> pts1 = new ArrayList<Point>();
//        List<Point> pts2 = new ArrayList<Point>();
//        for(int i = 0; i<good_matches.size(); i++){
//            pts1.add(keypoints1.toList().get(good_matches.get(i).queryIdx).pt);
//            pts2.add(keypoints2.toList().get(good_matches.get(i).trainIdx).pt);
//        }
//
//
//        // convertion of data types - there is maybe a more beautiful way
//        Mat outputMask = new Mat();
//        MatOfPoint2f pts1Mat = new MatOfPoint2f();
//        pts1Mat.fromList(pts1);
//        MatOfPoint2f pts2Mat = new MatOfPoint2f();
//        pts2Mat.fromList(pts2);
//
//        // mat of points not mat of point 2f
//        MatOfPoint pts1Data = new MatOfPoint ();
//
//        pts1Data.fromList (pts1);
//
//        MatOfPoint pts2Data = new MatOfPoint ();
//        pts2Data.fromList (pts2);
//
//
//        // Find homography - here just used to perform match filtering with RANSAC, but could be used to e.g. stitch images
//        // the smaller the allowed reprojection error (here 15), the more matches are filtered
//        Mat Homography = Calib3d.findHomography(pts2Mat, pts1Mat, Calib3d.RANSAC, 15, outputMask, 2000, 0.995);
//
//        // outputMask contains zeros and ones indicating which matches are filtered
//        LinkedList<DMatch> better_matches = new LinkedList<DMatch>();
//        for (int i = 0; i < good_matches.size(); i++) {
//            if (outputMask.get(i, 0)[0] != 0.0) {
//                better_matches.add(good_matches.get(i));
//            }
//        }
//
//
//
//        //Create the unscaled array of corners, representing the object size
//
//        Point cornersObject[] = new Point[4];
//        cornersObject[0] = new Point(0, 0);
//        cornersObject[1] = new Point(img1.cols(), 0);
//        cornersObject[2] = new Point(img1.cols(), img1.rows());
//        cornersObject[3] = new Point(0, img1.rows());
//
//        Point[] cornersSceneTemp = new Point[0];
//
//        MatOfPoint2f cornersSceneMatrix = new MatOfPoint2f(cornersSceneTemp);
//        MatOfPoint2f cornersObjectMatrix = new MatOfPoint2f(cornersObject);
//
//
//        //Transform the object coordinates to the scene coordinates by the homography matrix
//        Core.perspectiveTransform(cornersObjectMatrix, cornersSceneMatrix, Homography);
//
//
//
//
//        Imgproc.polylines (aInputFrame, (List<MatOfPoint>) pts2Data,true, new Scalar (0,0,255),2);

//.... How to get the prespective transform into a variable (Maybe requires mat of Point somethig from previous code)



        //.... wrap this into If condition when the good match is above some threshhold


        //.....Old code draw matches.....//


//        MatOfByte drawnMatches = new MatOfByte();
//        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
//            return aInputFrame;
//        }

//

//        Mat new_image = new Mat((aInputFrame.rows ()-100), (aInputFrame.cols ()-100),  CvType.CV_8UC1, Scalar.all(-1));
//
//
//        new_image.copyTo (aInputFrame);

//        Features2d.drawMatches(img1, keypoints1, aInputFrame, keypoints2, goodMatches, outputImg, Scalar.all(-1),
//                Scalar.all(-1), drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);

//        Features2d.drawKeypoints (img1,keypoints1,aInputFrame,Scalar.all(-1),Features2d.DRAW_OVER_OUTIMG);


//        Imgproc.resize(outputImg, outputImg, aInputFrame.size());


        Mat temp = img1;
        Imgproc.rectangle(aInputFrame, new Point(temp.cols()/2 - 200, temp.rows() / 2 - 200), new Point(temp.cols() / 2 + 200, temp.rows() / 2 + 200), new Scalar(255,255,255),1);

        return aInputFrame;

    }


    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        mat1 = inputFrame.rgba ();
//        return mat1;
        return recognize(inputFrame.rgba());
    }

    @Override
    public void onResume() {
        super.onResume ();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, getActivity ().getApplicationContext (), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    public void onPause() {
        super.onPause ();
        if(mOpenCvCameraView!=null)
        {
            mOpenCvCameraView.disableView ();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        if(mOpenCvCameraView!=null)
        {
            mOpenCvCameraView.disableView ();

        }
    }
}
