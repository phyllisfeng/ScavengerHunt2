package com.example.scavengerhunt;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class CreatePhotoActivity extends Activity {
    final static String DEBUG_TAG = "CreatePhotoActivity";
    private Camera camera;
    private ImageButton photoButton;
    private SurfaceView surfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_photo);

        if (camera == null){
            try{
                camera = Camera.open();
                photoButton.setEnabled(true);
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "No camera with exception: " + e.getMessage());
                photoButton.setEnabled(false);
                Toast.makeText(this, "No camera detected", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // look for a camera, and if none available notify user
        // via toast
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            // Find front facing camera id
            int cameraId = findCamera();
            // If no valid id found then report to user
            if (cameraId < 0)
                Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG)
                        .show();
                // Open the camera to prepare to take picture (upon onClick)
            else
                camera = Camera.open(cameraId);
        }
    }

    public void onClick(View view) {
        // Make sure camera is still non-null
        if (camera != null) {
            // Take picture, and provide photo handler
            camera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                    //nothing to do
                }
            }, null, new CreatePhotoHandler(
                    getApplicationContext()));
        } else {
            Toast.makeText(this, "No camera found.", Toast.LENGTH_LONG).show();
        }

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){
            public void surfaceCreated(SurfaceHolder holder){
                try{
                    if (camera!=null){
                        camera.setDisplayOrientation(90);
                        camera.setPreviewDisplay(holder);
                        camera.startPreview();
                    }
                } catch (IOException e){
                    Log.e(DEBUG_TAG, "Error setting up preview", e);
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //nothing to do here
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                //nothing to do here
            }
        });
    }

    private int findCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 0)
            return -1;
        int cameraId = 0;
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(DEBUG_TAG, "Front Facing Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        // On pause, release the camera
        // if it hasn't been released before
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }

}