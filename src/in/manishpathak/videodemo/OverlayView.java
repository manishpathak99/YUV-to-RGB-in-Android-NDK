package in.manishpathak.videodemo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class OverlayView extends SurfaceView  implements SurfaceHolder.Callback {
	private Camera mCam;
	private SurfaceHolder mCamSH;
	private SurfaceHolder mOverSH;
	private byte[] mFrame;
	private IntBuffer mFrameDiff;
	private Camera.Size mFrameSize;
	private boolean mRunning;
	private OverlayView mOverSV;
	int width = 640;
	int height = 480;
	
	static {
        System.loadLibrary("hello-jni");
    }
//	public native String  stringFromJNI();
	 private native void YUVtoRBG(int[] rgb, byte[] yuv, int width, int height);
	
	public OverlayView(Context c, AttributeSet attr) {
		super(c, attr);
		mOverSH = getHolder();
		mOverSH.addCallback(this);

	}
	
	 

	public void setCamera(Camera c)
	{
		mCam = c;
		if(mCam == null) return;
		final Bitmap bmp =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mCam.setPreviewCallback(new PreviewCallback()
		{
			private int[] out;
			int frameSize = width*height;
		    int[] rgba = new int[frameSize+1];
			public void onPreviewFrame(final byte[] data, Camera c)
			{
				
		    long startTime = System.currentTimeMillis();
		    // Convert YUV to RGB
//		    for (int i = 0; i < height; i++)
//		        for (int j = 0; j < width; j++) {
//		            int y = (0xff & ((int) data[i * width + j]));
//		            int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
//		            int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
//		            y = y < 16 ? 16 : y;
//
//		            int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
//		            int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
//		            int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
//
//		            r = r < 0 ? 0 : (r > 255 ? 255 : r);
//		            g = g < 0 ? 0 : (g > 255 ? 255 : g);
//		            b = b < 0 ? 0 : (b > 255 ? 255 : b);
//
//		            rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
//		        }
//		    FrameHandler frameHandler = new FrameHandler();
//		    frameHandler.execute(data);
//		    frameHandler.getBitmapRGB();
		    
//		    Bitmap bmp =  frameHandler.getBitmapRGB();
		    YUVtoRBG(rgba, data, width, height);
		    bmp.setPixels(rgba, 0/* offset */, width /* stride */, 0, 0, width, height);
		    Canvas canvas = mOverSH.lockCanvas();
		    if (canvas != null) {
		        canvas.drawBitmap(bmp, (canvas.getWidth() - width) / 2, (canvas.getHeight() - height) / 2, null);
		        mOverSH.unlockCanvasAndPost(canvas);
		    } else {
		        Log.w("canvas", "Canvas is null!");
		    }
		    
//		    bmp.recycle();
		    long endTime = System.currentTimeMillis();
			 Log.d("EXECUTIONTIME:   ",""+(endTime-startTime));
			 
		    }
			 
		});
	}

	public void setRunning(boolean r)
	{
		mRunning = r;
	}

	public void setPreviewSize(Camera.Size s)
	{
		Log.v("OverlayView", "Setting new preview size: "+s.width+"x"+s.height);
		//		pmeh.preErrStr += "Setting new preview size: "+s.width+"x"+s.height+"\n";
		mFrameSize = s;
		mFrame = new byte[s.width*s.height];
		mFrameDiff = ByteBuffer.allocateDirect((s.width*s.height)<<2).asIntBuffer();
	}
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	// decode Y, U, and V values on the YUV 420 buffer described as YCbCr_422_SP by Android 
	// David Manpearl 081201 
	public void decodeYUV(int[] out, byte[] fg, int width, int height)
			throws NullPointerException, IllegalArgumentException {
		int sz = width * height;
		if (out == null)
			throw new NullPointerException("buffer out is null");
		if (out.length < sz)
			throw new IllegalArgumentException("buffer out size " + out.length
					+ " < minimum " + sz);
		if (fg == null)
			throw new NullPointerException("buffer 'fg' is null");
		if (fg.length < sz)
			throw new IllegalArgumentException("buffer fg size " + fg.length
					+ " < minimum " + sz * 3 / 2);
		int i, j;
		int Y, Cr = 0, Cb = 0;
		for (j = 0; j < height; j++) {
			int pixPtr = j * width;
			final int jDiv2 = j >> 1;
		for (i = 0; i < width; i++) {
			Y = fg[pixPtr];
			if (Y < 0)
				Y += 255;
			if ((i & 0x1) != 1) {
				final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
				Cb = fg[cOff];
				if (Cb < 0)
					Cb += 127;
				else
					Cb -= 128;
				Cr = fg[cOff + 1];
				if (Cr < 0)
					Cr += 127;
				else
					Cr -= 128;
			}
			int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
			if (R < 0)
				R = 0;
			else if (R > 255)
				R = 255;
			int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
					+ (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
			if (G < 0)
				G = 0;
			else if (G > 255)
				G = 255;
			int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
			if (B < 0)
				B = 0;
			else if (B > 255)
				B = 255;
			out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
		}
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}
	
	public class FrameHandler extends AsyncTask<byte[], Void, Boolean> {

        // Final Variables
        private final static int WIDTH = 480;
        private final static int HEIGHT = 320;
        private final static int ARRAY_LENGTH = 480*320*3/2;
        
        // pre-allocated working arrays
        private int[] argb8888 = new int[ARRAY_LENGTH];
        
        // filename of image
        int quality = 75;
        private String filename;
        int imageIndex = 0;
        
        Bitmap bitmap = null;
        @Override
        protected Boolean doInBackground(byte[]... args) {
                Log.v("GlobeTrotter", "Beginning AsyncTask");
                
                imageIndex = args[1][0];
                filename = "/sdcard/globetrotter/bufferdump/" + (args[1][0]) +".jpg";

                // creates an RGB array in argb8888 from the YUV btye array
                decodeYUV(argb8888, args[0], WIDTH, HEIGHT);
                bitmap = Bitmap.createBitmap(argb8888, WIDTH, HEIGHT, Config.ARGB_8888);
                
                // save a jpeg file locally
                try {
                        save(bitmap);
                } catch (IOException e) {
                        e.printStackTrace();
                }

                // upload that file to the server
//                postData();
                
                return true;
        }  
        
        synchronized Bitmap getBitmapRGB() {
        	if (bitmap != null)
        		return bitmap;
        	return Bitmap.createBitmap(argb8888, WIDTH, HEIGHT, Config.ARGB_8888);
        }
        
        public void save(Bitmap bmp) throws IOException {
                //  BitmapFactory.Options options=new BitmapFactory.Options();          
                //      options.inSampleSize = 20;
                
                FileOutputStream fos = new FileOutputStream(filename);
                
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bmp.compress(CompressFormat.JPEG, quality, bos);

                bos.flush();
                bos.close();            
        }
        
        // decode Y, U, and V values on the YUV 420 buffer described as YCbCr_422_SP by Android 
        // David Manpearl 081201 
        public void decodeYUV(int[] out, byte[] fg, int width, int height)
                throws NullPointerException, IllegalArgumentException {
            int sz = width * height;
            if (out == null)
                throw new NullPointerException("buffer out is null");
            if (out.length < sz)
                throw new IllegalArgumentException("buffer out size " + out.length
                        + " < minimum " + sz);
            if (fg == null)
                throw new NullPointerException("buffer 'fg' is null");
            if (fg.length < sz)
                throw new IllegalArgumentException("buffer fg size " + fg.length
                        + " < minimum " + sz * 3 / 2);
            int i, j;
            int Y, Cr = 0, Cb = 0;
            for (j = 0; j < height; j++) {
                int pixPtr = j * width;
                final int jDiv2 = j >> 1;
                for (i = 0; i < width; i++) {
                    Y = fg[pixPtr];
                    if (Y < 0)
                        Y += 255;
                    if ((i & 0x1) != 1) {
                        final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
                        Cb = fg[cOff];
                        if (Cb < 0)
                            Cb += 127;
                        else
                            Cb -= 128;
                        Cr = fg[cOff + 1];
                        if (Cr < 0)
                            Cr += 127;
                        else
                            Cr -= 128;
                    }
                    int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                    if (R < 0)
                        R = 0;
                    else if (R > 255)
                        R = 255;
                    int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
                            + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                    if (G < 0)
                        G = 0;
                    else if (G > 255)
                        G = 255;
                    int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                    if (B < 0)
                        B = 0;
                    else if (B > 255)
                        B = 255;
                    out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
                }
            }
        }


}
}