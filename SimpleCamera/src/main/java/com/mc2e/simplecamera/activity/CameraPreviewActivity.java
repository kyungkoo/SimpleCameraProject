package com.mc2e.simplecamera.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mc2e.simplecamera.R;
import com.mc2e.simplecamera.module.SimpleBitmapEditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by mc2e on 2013. 11. 30..
 */
public class CameraPreviewActivity extends Activity implements View.OnClickListener, PhotoViewAttacher.OnMatrixChangedListener {

    private ImageView mPicturePreview;
    private PhotoViewAttacher mAttacher;

    private RectF mRectF;

    private Button mSaveBtn;

    private Bitmap mFactory  = null;

    private View mGudeView;

    private FrameLayout mFrameView, mImageFrame;

    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_preview);

        mImageFrame = (FrameLayout)findViewById(R.id.image_frame);

        mFrameView = (FrameLayout)findViewById(R.id.frame_view);

        mGudeView = findViewById(R.id.pre_guide_view);

        mSaveBtn = (Button)findViewById(R.id.crop_and_save_btn);

        byte[] imageData = getIntent().getByteArrayExtra("IMAGE_BYTE");

        mHeight = getIntent().getIntExtra("IMAGE_HEIGHT", 0);

        mFactory = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        if(mFactory.getHeight() < mFactory.getWidth()){
            mFactory = SimpleBitmapEditor.imgRotate(mFactory, -90);
        }

        mPicturePreview = (ImageView)findViewById(R.id.pre_image_view);

        mPicturePreview.setImageBitmap(mFactory);

        mAttacher = new PhotoViewAttacher(mPicturePreview);
        mAttacher.setOnMatrixChangeListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.crop_and_save_btn) {

            String path = "/sdcard/DCIM/Camera/test_image_.png";

            File file = new File(path);

            mImageFrame.setDrawingCacheEnabled(true);

            Bitmap scaledBitmap = mImageFrame.getDrawingCache(false);

            Bitmap changedBitmap = SimpleBitmapEditor.cropBitmapToOval(scaledBitmap, mGudeView.getWidth(), mGudeView.getHeight());

            try {
                FileOutputStream fos = new FileOutputStream(file);

                fos.write(bitmapToByteArray(changedBitmap));
                fos.flush();
                fos.close();

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failded!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMatrixChanged(RectF rectF) {

        mRectF = rectF;
    }

    public byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
