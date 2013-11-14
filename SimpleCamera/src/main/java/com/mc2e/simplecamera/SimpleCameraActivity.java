package com.mc2e.simplecamera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by mc2e on 2013. 11. 14..
 */
public class SimpleCameraActivity extends Activity {

    private SimpleSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_camera);

        mSurfaceView = (SimpleSurfaceView)findViewById(R.id.simple_surface_view);
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mSurfaceView.getCamera().takePicture(null, null, mPicture);
                }
                return false;
            }
        });
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            String path = "/sdcard/cameratest.jpg";

            File file = new File(path);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
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
            mSurfaceView.getCamera().startPreview();
        }
    };
}   //  end class
