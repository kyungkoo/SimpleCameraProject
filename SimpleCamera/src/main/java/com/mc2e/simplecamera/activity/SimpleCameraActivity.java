package com.mc2e.simplecamera.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mc2e.simplecamera.R;
import com.mc2e.simplecamera.module.SimpleBitmapEditor;
import com.mc2e.simplecamera.view.SimpleSurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mc2e on 2013. 11. 14..
 */
public class SimpleCameraActivity extends Activity {

    private SimpleSurfaceView mSurfaceView;

    private View mGuideView;

    private int counter = 0;

    private String path1, path2, path3;

    public static final int CAMERA_PREVIEW_REQUEST_CODE = 20131202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_camera);

        mGuideView = findViewById(R.id.guide_view);

        mSurfaceView = (SimpleSurfaceView)findViewById(R.id.simple_surface_view);
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    takePicture();
                }
                return false;
            }
        });
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            //savePicture(data);

            Intent intent = new Intent(getApplicationContext(), CameraPreviewActivity.class);
            intent.putExtra("IMAGE_BYTE", data);
            intent.putExtra("IMAGE_COUNT",counter);
            startActivityForResult(intent, CAMERA_PREVIEW_REQUEST_CODE);
        }
    };

    private void takePicture() {

        // 전면 카메라가 오토포커싱이 안되는 문제로 그냥 사진을 찍어야됨
        mSurfaceView.getCamera().takePicture(null, null, mPicture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_PREVIEW_REQUEST_CODE) {

            String filePath = data.getStringExtra("FILE_PATH");

            switch (counter) {
                case 0:
                    path1 = filePath;
                    counter++;
                    break;

                case 1:
                    path2 = filePath;
                    counter++;
                    break;
                case 2:
                    path3 = filePath;
                    sendResult();
                    break;
            }
        }
    }

    private void sendResult() {
        Intent i = new Intent();
        i.putExtra("PATH1", path1);
        i.putExtra("PATH2", path2);
        i.putExtra("PATH3", path3);

        //i.putExtras(bundle);

        setResult(RESULT_OK, i);
        finish();
    }

    // Deprecated Code
    @Deprecated
    public byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Deprecated
    private void savePicture(byte[] data) {
        String path = "/sdcard/DCIM/Camera/test"+counter+".png";

        File file = new File(path);
        try {

            Bitmap factory = BitmapFactory.decodeByteArray(data, 0, data.length);

            if(factory.getHeight() < factory.getWidth()){
                factory = SimpleBitmapEditor.imgRotate(factory, -90);
            }

            int width = mGuideView.getWidth();

            int height = mGuideView.getHeight();

            int totalHeight = mSurfaceView.getHeight();

            Bitmap resize = SimpleBitmapEditor.resizeBitmap(factory, totalHeight);

            Bitmap croped = SimpleBitmapEditor.cropBitmapToOval(resize, width, height);

            if (counter == 1) {
                path1 = path;
                counter ++;
            }else if(counter == 2) {
                path2 = path;
                counter ++;
            }else if (counter == 3) {
                path3 = path;
                counter ++;
            }

            FileOutputStream fos = new FileOutputStream(file);

            fos.write(bitmapToByteArray(croped));
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 중 에러 발생 : " +
                    e.getMessage(), 0).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.parse("file://" + path);
        intent.setData(uri);
        sendBroadcast(intent);

        Toast.makeText(getApplicationContext(), "사진 저장 완료 : " + path, 0).show();

        if (counter == 4) {

            Intent i = new Intent();
            i.putExtra("PATH1", path1);
            i.putExtra("PATH2", path2);
            i.putExtra("PATH3", path3);

            //i.putExtras(bundle);

            setResult(RESULT_OK, i);
            finish();
        }else {
            mSurfaceView.getCamera().startPreview();
        }
    }

}   //  end class
