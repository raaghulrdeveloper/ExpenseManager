
package com.raaghulr.expensemanager.cameraservice;

import java.io.IOException;

import com.raaghulr.expensemanager.utils.Log;

import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class CameraHolder {
    private android.hardware.Camera mCameraDevice;
    private long mKeepBeforeTime = 0; 
    private final Handler mHandler;
    private int mUsers = 0; 
    
   
    private Parameters mParameters;

    // Use a singleton.
    private static CameraHolder sHolder;
    public static synchronized CameraHolder instance() {
        if (sHolder == null) {
            sHolder = new CameraHolder();
        }
        return sHolder;
    }

    private static final int RELEASE_CAMERA = 1;
    private class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case RELEASE_CAMERA:
                    synchronized (CameraHolder.this) {
                        
                        if (CameraHolder.this.mUsers == 0) releaseCamera();
                    }
                    break;
            }
        }
    }

    private CameraHolder() {
        HandlerThread ht = new HandlerThread("CameraHolder");
        ht.start();
        mHandler = new MyHandler(ht.getLooper());
    }

    public void Assert(boolean cond) {
        if (!cond) {
            throw new AssertionError();
        }
    }
    
    public synchronized android.hardware.Camera open()
            throws CameraHardwareException {
        Assert(mUsers == 0);
        if (mCameraDevice == null) {
            try {
                mCameraDevice = android.hardware.Camera.open();
            } catch (RuntimeException e) {
                Log.d("fail to connect Camera"+ e);
                throw new CameraHardwareException(e);
            }
            mParameters = mCameraDevice.getParameters();
        } else {
            try {
                mCameraDevice.reconnect();
            } catch (IOException e) {
                Log.d("reconnect failed.");
                throw new CameraHardwareException(e);
            }
            mCameraDevice.setParameters(mParameters);
        }
        ++mUsers;
        mHandler.removeMessages(RELEASE_CAMERA);
        mKeepBeforeTime = 0;
        return mCameraDevice;
    }
    
   
    public synchronized android.hardware.Camera tryOpen() {
        try {
            return mUsers == 0 ? open() : null;
        } catch (CameraHardwareException e) {
            
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    public synchronized void release() {
        Assert(mUsers == 1);
        --mUsers;
        mCameraDevice.stopPreview();
        releaseCamera();
    }

    private synchronized void releaseCamera() {
        Assert(mUsers == 0);
        Assert(mCameraDevice != null);
        long now = System.currentTimeMillis();
        if (now < mKeepBeforeTime) {
            mHandler.sendEmptyMessageDelayed(RELEASE_CAMERA,
                    mKeepBeforeTime - now);
            return;
        }
        mCameraDevice.release();
        mCameraDevice = null;
    }

    public synchronized void keep() {
       
        Assert(mUsers == 1 || mUsers == 0);
       
        mKeepBeforeTime = System.currentTimeMillis() + 3000;
    }
}
