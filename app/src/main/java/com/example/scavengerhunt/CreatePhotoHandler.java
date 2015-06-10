package com.example.scavengerhunt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class CreatePhotoHandler implements Camera.PictureCallback {

    private final Context context;
    ParseFile file;

    public CreatePhotoHandler(Context context) {
        // Store context
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        file = new ParseFile("pictureFile", data);

        // Get the directory for pictures
        File pictureFileDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraAPIDemo");
        // Make sure it exists
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
            Log.d(CreatePhotoActivity.DEBUG_TAG,
                    "Can't create directory to save image.");
            Toast.makeText(context, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Create filename from current date and time
        // First get the date formatted as a string...with year month day hours minutes seconds
        // e.g. 20130229134551
        String date = new SimpleDateFormat("yyyymmddhhmmss").format(new Date());
        // Prefix with "Picture_" and add suffix ".jpg"
        String photoFileName = "Picture_" + date + ".jpg";

        // Get the whole path (directory + File.separator + photo
        String photoFilePath = pictureFileDir.getPath() + File.separator + photoFileName;

        // This creates a new file at the given location
        File photoFile = new File(photoFilePath);

        try {
            // Create file output stream from photoFile
            FileOutputStream fos = new FileOutputStream(photoFile);
            // Write the image data to the file output stream
            fos.write(data);
            // Close file output stream
            fos.close();
            Toast.makeText(context, "New Image saved:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.e(CreatePhotoActivity.DEBUG_TAG, "File" + photoFilePath
                    + "not saved: " + error.getMessage());
            Toast.makeText(context, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }

}

