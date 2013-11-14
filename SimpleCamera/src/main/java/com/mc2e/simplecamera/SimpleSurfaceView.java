package com.mc2e.simplecamera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera.Size;
import java.io.IOException;
import java.util.List;

/**
 * Created by mc2e on 2013. 11. 14..
 */
public class SimpleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public SimpleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public Camera getCamera() {
        return mCamera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = Camera.open();

        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
        }catch(IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Camera.Parameters params = mCamera.getParameters();
        List<Size> arSize = params.getSupportedPreviewSizes();

        if (arSize == null) {
            params.setPreviewSize(width, height);
        }else {
            int diff = 10000;
            Size opti = null;
            for (Size s : arSize) {
                if (Math.abs(s.height - height) < diff) {
                    diff = Math.abs(s.height - height);
                    opti = s;

                }
            }
            params.setPreviewSize(opti.width, opti.height);
        }

        mCamera.setParameters(params);
        mCamera.startPreview();
    }
}
