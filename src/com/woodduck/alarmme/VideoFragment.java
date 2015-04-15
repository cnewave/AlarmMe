
package com.woodduck.alarmme;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

public class VideoFragment extends Fragment {
    private String TAG = "VideoFragment_";
    private ImageButton mVideoRecord;
    private ImageButton mReplay;
    private ImageView mImageView;
    private VideoView mVideoView;

    public static VideoFragment newInstance() {
        VideoFragment f = new VideoFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        Log.d(TAG, "create view...." + savedInstanceState);
        View view = inflater.inflate(R.layout.videorecorder, container, false);
        mVideoRecord = (ImageButton) view.findViewById(R.id.recording);
        mVideoRecord.setOnClickListener(new RecoringListener());

        mReplay = (ImageButton) view.findViewById(R.id.replay);
        mReplay.setOnClickListener(new PlayListener());

        mImageView = (ImageView) view.findViewById(R.id.image);
        mVideoView = (VideoView) view.findViewById(R.id.videoView1);
        // To do [kent] should move to other places.
        if (mVideoUri != null) {
            mVideoView.setVideoURI(mVideoUri);
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        ViewTreeObserver vto = mImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "create view....W " + mImageView.getWidth());
                Log.d(TAG, "create view....H " + mImageView.getHeight());
                targetW = mImageView.getWidth();
                targetH = mImageView.getHeight();
                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    class RecoringListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "do take button...");
            // takePhotowithCamera();
            dispatchTakeVideoIntent();
        }
    }

    class PlayListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "do replay button...");
            mVideoView.requestFocus();
            mVideoView.start();
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;

    private void takePhotowithCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            Log.d(TAG, "do piecture..");
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Show thumbnail...");

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            mImageView.setVisibility(View.VISIBLE);

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Show Image...");
            setPic();
            mImageView.setVisibility(View.VISIBLE);
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "video capture Image...");
            handleCameraVideo(data);
        }
    }

    private void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();
        Log.d(TAG, "handleCameraVideo." + mVideoUri);
        mVideoView.setVideoURI(mVideoUri);
        addThumbnail();
    }

    private void addThumbnail() {

    }

    Uri mVideoUri;

    public String getRecordPath() {
        return mVideoUri.toString();
    }

    public void setRecordPath(String uri) {
        mVideoUri = Uri.parse(uri);
        Log.d(TAG, "setRecordPath:" + mVideoUri);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        Calendar calendar = Calendar.getInstance();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(calendar.getTime());
        String imageFileName = timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/record/");
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
                );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private int targetW;
    private int targetH;

    private void setPic() {
        // Get the dimensions of the View
        // int targetW = 60;// mImageView.getWidth();
        // int targetH = 60;// mImageView.getHeight();
        Log.d(TAG, "setPic." + targetW + " " + targetH + " mCurrentPhotoPath " + mCurrentPhotoPath);
        if (targetW == 0 || targetH == 0) {
            Log.d(TAG, "fail to set pcic");
            return;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        if (bitmap != null) {
            Log.d(TAG, "set bitmap");
        } else {
            Log.d(TAG, "set bitmap null");
        }
        mImageView.setImageBitmap(bitmap);
    }
}
