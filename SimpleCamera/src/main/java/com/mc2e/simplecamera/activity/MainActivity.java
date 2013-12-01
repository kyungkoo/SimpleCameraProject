package com.mc2e.simplecamera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mc2e.simplecamera.R;
import com.mc2e.simplecamera.module.SimpleBitmapEditor;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int CALL_CAMERA = 2002;

    private Bitmap mFrontBitmap, mLeftBitmap, mRightBitmap;

    private ImageView mFrontView, mLeftView, mRightView;
    private Button mTakePhotoBtn;

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

        Intent intent = new Intent(getApplicationContext(), SimpleCameraActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, CALL_CAMERA);
     //   takePhoto();
    }

    /**
     * Crop 된 데이터를 Bitmap 으로 저장하는 메소드
     * 저장된 Bitmap 은 후에 ImageView 에 추가하여 화면에 출력
     * @param data
     */
    private void saveImage(Intent data) {



        mFrontBitmap = BitmapFactory.decodeFile(data.getStringExtra("PATH1"));
        mLeftBitmap = BitmapFactory.decodeFile(data.getStringExtra("PATH2"));

        // -90도 회전
        mLeftBitmap = SimpleBitmapEditor.imgRotate(mLeftBitmap, -90);

        mRightBitmap = BitmapFactory.decodeFile(data.getStringExtra("PATH3"));

        // 90 도 회전
        mRightBitmap = SimpleBitmapEditor.imgRotate(mRightBitmap, 90);

        if (mFrontBitmap != null)
            mFrontView.setImageBitmap(mFrontBitmap);
        if (mLeftBitmap != null)
            mLeftView.setImageBitmap(mLeftBitmap);
        if (mRightBitmap != null)
            mRightView.setImageBitmap(mRightBitmap);

    }

    /*
    카메라앱, Crop 앱을 통해 생성된 데이터를 전달받는 메소드
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch(requestCode) {

            case CALL_CAMERA:
                saveImage(data);
                break;
        }
    }
} // end Class
