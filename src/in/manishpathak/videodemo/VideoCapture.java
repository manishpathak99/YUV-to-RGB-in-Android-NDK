package in.manishpathak.videodemo;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class VideoCapture extends Activity implements SurfaceHolder.Callback {
	MediaRecorder recorder;
	SurfaceHolder holder;
	boolean recording = false;
	private Button buttonRecording;
		private Button buttonFlipFrontToRear;
	private Camera mCamera;
	boolean isCameraOPen;
	private OverlayView mOverSV;	
	File file;
	private SurfaceView mCamSV; 
	private SurfaceHolder mCamSH;
	private Camera mCam;
	private boolean mPreview;
	private Camera.Size mPreviewSize = null;

	public static int CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;
	
	static {
        System.loadLibrary("hello-jni");
    }
	public native String  stringFromJNI();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		// for S2 it would be fine
//		mCamera = Camera.open();
//		isCameraOPen = true;

		
//		initRecorder();
		setContentView(R.layout.activity_main);
		

//		SurfaceView mySurfaceView = (SurfaceView) findViewById(R.id.mypreview);
//		ViewGroup.LayoutParams params = mySurfaceView.getLayoutParams();                     
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);               
//		params.width =dm.widthPixels/2;
//		params.height = dm.heightPixels;
//		mySurfaceView.setLayoutParams(params);
//
//		holder = mySurfaceView.getHolder();
//		holder.addCallback(this);
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//		buttonRecording = (Button)findViewById(R.id.record);
		//		buttonFlipFrontToRear = (Button)findViewById(R.id.flip);
		//		((TextView)findViewById(R.id.textView1)).setText(text);


		//        mySurfaceView.setClickable(true);
//		buttonRecording.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				if (recording) {
//					buttonRecording.setText("Record");
//					recorder.stop();
//					recording = false;
//					recorder.release();
//					//					recorder = null;
//					mCamera.lock();
//					mCamera.stopPreview();
//					mCamera.setPreviewCallback(null);
//					mCamera.release();
//					mCamera = null;
//					isCameraOPen = false;
//					// Let's initRecorder so we can record again
//					initRecorder();
//					prepareRecorder();
//				} else {
//					if(!isCameraOPen) {
//						mCamera = Camera.open();
//						//						Camera.Parameters parameters = mCamera.getParameters();
//						//						parameters.set("camera-id", 0);
//						//						parameters.setPreviewSize(320, 240); // or (800,480)
//						//						mCamera.setParameters(parameters);
//						//						Toast.makeText(VideoCapture.this, "camera-id"+CAMERA_ID, Toast.LENGTH_LONG).show();
//						isCameraOPen = true;
//					}
//					buttonRecording.setText("Stop");
//					recording = true;
//					recorder.start();
//				}
//
//			}
//		});
		//		buttonFlipFrontToRear.setOnClickListener(new OnClickListener() {
		//			public void onClick(View v) {
		//				if (v.equals(buttonFlipFrontToRear)) {
		//					if (Camera.getNumberOfCameras() > 1 && CAMERA_ID < Camera.getNumberOfCameras() - 1) {
		//						CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;
		//						Toast.makeText(VideoCapture.this, "Front Face Camera is active", Toast.LENGTH_LONG).show();
		//					} else {
		//						CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;
		//						Toast.makeText(VideoCapture.this, "Back Face Camera is active", Toast.LENGTH_LONG).show();
		//					}
		//				} 
		//			}
		//		});
	}
	private void initCamera() {
		mCamSV = (SurfaceView)findViewById(R.id.mypreview);
		mCamSH = mCamSV.getHolder();
		mCamSH.addCallback(this);
		mCamSH.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		ViewGroup.LayoutParams params = mCamSV.getLayoutParams();                     
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);               
		params.width =dm.widthPixels/2;
		params.height = dm.heightPixels;
		mCamSV.setLayoutParams(params);

		mOverSV = (OverlayView)findViewById(R.id.ScrollView01);
		mOverSV.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		mOverSV.setCamera(mCam);
		if(mCam == null)
		{
			mCam = Camera.open();
		}
		mOverSV.setRunning(true);
		mPreview = false;
		
//		Toast.makeText(getContext(), stringFromJNI(), Toast.LENGTH_SHORT).show();
		 Log.d("JNI HELLO:   ",""+stringFromJNI());
		
		//		mOEL = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL)
		//		{
		//			@Override
		//			public void onOrientationChanged(int o)
		//			{
		//				if(o == ORIENTATION_UNKNOWN) return;
		//				o = (o+45)/90*90;
		//				mOrient = o%360;
		//			}
		//		};
		//		if(mOEL.canDetectOrientation()) mOEL.enable();
	}

	private void stopCamera()
	{
		//		mOEL.disable();
		mOverSV.setRunning(false);
		mCam.stopPreview();
		mPreview = false;
		mCam.setPreviewCallback(null);
		mCam.release();
		mCam = null;
	}


	static int i =1;
	private void initRecorder() {

		recorder = new MediaRecorder();
		recorder.reset();
		if(!isCameraOPen) {
			// for S2 it would be fine
			mCamera = Camera.open();
			//			Camera.Parameters parameters = mCamera.getParameters();
			//			parameters.set("camera-id", 2);
			//			parameters.setPreviewSize(640, 480); // or (800,480)
			//			mCamera.setParameters(parameters);
			//			Toast.makeText(VideoCapture.this, "camera-id"+CAMERA_ID, Toast.LENGTH_LONG).show();
			isCameraOPen = true;
		}
		mCamera.unlock();

		recorder.setCamera(mCamera);
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

		CamcorderProfile cpHigh = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);
		recorder.setProfile(cpHigh);
		//		recorder.setOutputFile("/mnt/sdcard/videocapture_example"+i+++".mp4");
		String path = SdCardUtils.getSdcardPath();
		if(path == null){
			Toast.makeText(VideoCapture.this, "Sd card is not available or it is connected to computer.", Toast.LENGTH_LONG).show();
			finish();
		}
		Time nowTime = new Time();
		nowTime.setToNow();
		long timelong = nowTime.toMillis(true);
		// create file structure on sdcard
		file = new File(path+"/videoStore");
		boolean exists = file.exists();
		boolean fileCreated = false;
		if (!exists){
			fileCreated = file.mkdirs();
		}  
		file = new File(file, timelong+i+++".mp4");
		try {
			fileCreated = file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		AppLog.d("FILE CREATED", String.valueOf(fileCreated));

		//		File f = new File(file.getAbsolutePath()+"//temp.temp");
		//		f.mkdirs();
		//		recorder.setOutputFile(path+"/videoStore/"+nowTime.toMillis(true)+i+++".mp4");

		recorder.setOutputFile(file.getAbsolutePath());
		recorder.setMaxDuration(500000); // 50 seconds
		recorder.setMaxFileSize(50000000); // Approximately 50 megabytes
	}

	private void prepareRecorder() {
		recorder.setPreviewDisplay(holder.getSurface());

		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			finish();
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}
	}

	//	public void onClick(View v) {
	//		if (recording) {
	//			recorder.stop();
	//			recording = false;
	//
	//			// Let's initRecorder so we can record again
	//			initRecorder();
//				prepareRecorder();
	//		} else {
	//			recording = true;
	//			recorder.start();
	//		}
	//	}

	public void surfaceCreated(SurfaceHolder holder) {
		//		Toast.makeText(VideoCapture.this, "surface created", Toast.LENGTH_LONG).show();
		//		Camera.Parameters myParameters = mCamera.getParameters();
		//		Camera.Size myBestSize = getBestPreviewSize(width, height, myParameters);
		//		//preview-size-values=1920x1080,1280x720,960x720,800x480,720x576,720x480,768x576,640x480,320x240,352x288,240x160,176x144,128x96)
		//		if(myBestSize != null){
		//			myParameters.setPreviewSize(240, 160);
		//			mCamera.setParameters(myParameters);
		//			mCamera.startPreview();
		//			isPreview = true;
		//		}
//		prepareRecorder();
	}

	public void surfaceChanged(SurfaceHolder sh, int format, int width,
			int height) {
		//		pmeh.preErrStr += "Surface parameters changed: "+w+"x"+h+"\n";
		if(mCam != null)
		{
			if(mPreview) mCam.stopPreview();
			Camera.Parameters p = mCam.getParameters();
			//		    p.setRotation(mOrient);
			for(Camera.Size s : p.getSupportedPreviewSizes())
			{
//				p.setPreviewSize(s.width, s.height);
				p.setPreviewSize(640,480);
				mOverSV.setPreviewSize(s);
				mPreviewSize = s;
						        Log.v("videocapture", "Supported preview: "+s.width+"x"+s.height);
//				pmeh.preErrStr += "Supported preview: "+s.width+"x"+s.height+"\n";
				break;
			}
			mCam.setParameters(p);
			try
			{
				mCam.setPreviewDisplay(sh);
			}
			catch(Exception e)
			{
				//		    	Log.e(LOGTAG, "Camera preview not set");
			}
			mCam.startPreview();
			
			mPreview = true;
		}
		//		Toast.makeText(VideoCapture.this, "surface changed", Toast.LENGTH_LONG).show();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		//		Toast.makeText(VideoCapture.this, "surface destroyed", Toast.LENGTH_LONG).show();
		if (recording) {
			recorder.stop();
			recording = false;
		}
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		recorder.release();
		recorder = null;
		finish();
	}

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result=null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width<=width && size.height<=height) {
				if (result==null) {
					result=size;
				}
				else {
					int resultArea=result.width*result.height;
					int newArea=size.width*size.height;

					if (newArea>resultArea) {
						result=size;
					}
				}
			}
		}
		return(result);
	}

	@Override
	protected void onPause() {
		//		Toast.makeText(VideoCapture.this, "on pause", Toast.LENGTH_LONG).show();
		if(file!=null)
			file.delete();
		stopCamera();
    	mCamSH.removeCallback(this);
    	this.finish();
		super.onPause();
	}

	@Override
	protected void onResume() {
		//		Toast.makeText(VideoCapture.this, "on resume", Toast.LENGTH_LONG).show();
//		onStart();
		initCamera();
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		initCamera();
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	//	private void stopCamera(){
	//		//		System.out.println("stopCamera method");
	//		if (mCamera != null){
	//			mCamera.stopPreview();
	//			mCamera.setPreviewCallback(null);
	//			mCamera.release();
	//			mCamera = null;
	//			//            holder.removeCallback(listener);
	//			holder = null;
	//		}
	//	}
}