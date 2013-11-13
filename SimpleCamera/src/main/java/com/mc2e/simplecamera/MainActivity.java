package com.mc2e.simplecamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int CAPTURE_IMAGE = 0;
    private static final int CROP_IMAGE_FRONT = 1;
    private static final int CROP_IMAGE_LEFT = 2;
    private static final int CROP_IMAGE_RIGHT = 3;

    private Bitmap mFrontBitmap, mLeftBitmap, mRightBitmap;

    private ImageView mFrontView, mLeftView, mRightView;
    private Button mTakePhotoBtn;

    private Uri mImageCaptureUri;

    // change status
    //private String mCaptureStatus = "front";
    private int statusCode = CROP_IMAGE_FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init IamgeView
        mFrontView = (ImageView)findViewById(R.id.front_iv);
        mLeftView = (ImageView)findViewById(R.id.left_iv);
        mRightView = (ImageView)findViewById(R.id.right_iv);

        // init button
        mTakePhotoBtn = (Button)findViewById(R.id.take_photo_btn);
        mTakePhotoBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        takePhoto();
    }

    private void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "take_picture_temp.jpg";

        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    private void cropImage(int status) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, "image/*");

        intent.putExtra("outputX", 90);
        intent.putExtra("outputY", 90);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, status);
    }

    private void saveImage(Intent data) {

        final Bundle extras = data.getExtras();

        if(extras != null) {
            Bitmap photo = extras.getParcelable("data");

            switch(statusCode) {
                case CROP_IMAGE_FRONT:
                    mFrontBitmap = photo;
                    break;

                case CROP_IMAGE_LEFT:
                    mLeftBitmap = photo;
                    break;

                case CROP_IMAGE_RIGHT:
                    mRightBitmap = photo;
                    break;
            }
        }

        File f = new File(mImageCaptureUri.getPath());
        if(f.exists())
            f.delete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch(requestCode) {

            case CAPTURE_IMAGE:
                cropImage(statusCode);
                break;

            case CROP_IMAGE_FRONT:
                saveImage(data);
                Toast.makeText(getApplicationContext(), "image saved.", Toast.LENGTH_SHORT).show();
                statusCode = CROP_IMAGE_LEFT;
                takePhoto();
                break;

            case CROP_IMAGE_LEFT:
                saveImage(data);
                Toast.makeText(getApplicationContext(), "image saved.", Toast.LENGTH_SHORT).show();
                statusCode = CROP_IMAGE_RIGHT;
                takePhoto();
                break;

            case CROP_IMAGE_RIGHT:
                saveImage(data);
                Toast.makeText(getApplicationContext(), "image saved.", Toast.LENGTH_SHORT).show();

                mFrontView.setImageBitmap(mFrontBitmap);
                mLeftView.setImageBitmap(mLeftBitmap);
                mRightView.setImageBitmap(mRightBitmap);
                break;
        }
    }
} // end Class
