//package in.manishpathak.videodemo;
//
//import java.io.IOException;
//
//import android.app.Activity;
//import android.hardware.Camera;
//import android.media.CamcorderProfile;
//import android.media.MediaRecorder;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//public class MainActivity extends Activity {
//
//	Camera myCamera;
//	SurfaceView mySurfaceView;
//	SurfaceHolder mySurfaceHolder;
//	boolean isPreview;
//	Button buttonRecording;
////	Button buttonFlipFrontToRear;
//	static int REQUEST_VIDEO_CAPTURED = 1;
//	private MediaRecorder recorder;
////	private Preview preview;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		recorder = new MediaRecorder();
//		isPreview = false;
//		mySurfaceView = (SurfaceView)findViewById(R.id.mypreview);
//		mySurfaceHolder = mySurfaceView.getHolder();
//		mySurfaceHolder.addCallback(mySurfaceCallback);
//		mySurfaceHolder.addCallback(mySurfaceCallback);
//
//		ViewGroup.LayoutParams params = mySurfaceView.getLayoutParams();                     
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);               
//		params.width =dm.widthPixels/2;
//		params.height = dm.heightPixels;
//		mySurfaceView.setLayoutParams(params);
//		
//		buttonRecording = (Button)findViewById(R.id.record);
////		buttonFlipFrontToRear = (Button)findViewById(R.id.flip);
//		buttonRecording.setOnClickListener(new Button.OnClickListener(){
//
//			 @Override
//			 public void onClick(View arg0) {
//			  // TODO Auto-generated method stub
////			  Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
////			  startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
//				 recording = true;
//		            recorder.start();
//			 }});
////		buttonFlipFrontToRear.setOnClickListener(new Button.OnClickListener(){
////
////			 @Override
////			 public void onClick(View arg0) {
////				 recorder.stop();
////			        recording = false;
////
////			        // Let's initRecorder so we can record again
////			        initRecorder();
////			        prepareRecorder();
////
////			 }});
//	}
//
//	SurfaceHolder.Callback mySurfaceCallback
//	= new SurfaceHolder.Callback(){
//
//		@Override
//		public void surfaceChanged(SurfaceHolder holder, int format, int width,
//				int height) {
//
//
//
//			//			Camera.Parameters parameters = myCamera.getParameters();
//			//			List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
//			//
//			//			// You need to choose the most appropriate previewSize for your app
//			//			Camera.Size previewSize = new Camera.Size(300, 300);// .... select one of previewSizes here
//			//
//			//			parameters.setPreviewSize(previewSize.width, previewSize.height);
//			//			myCamera.setParameters(parameters);
//			//			myCamera.startPreview();
//			//			isPreview = true;
//
//			Camera.Parameters myParameters = myCamera.getParameters();
//			Camera.Size myBestSize = getBestPreviewSize(width, height, myParameters);
//			//preview-size-values=1920x1080,1280x720,960x720,800x480,720x576,720x480,768x576,640x480,320x240,352x288,240x160,176x144,128x96)
//			if(myBestSize != null){
//				myParameters.setPreviewSize(1280, 720);
//				myCamera.setParameters(myParameters);
//				myCamera.startPreview();
//				isPreview = true;
//			}
//			//			Toast.makeText(getApplicationContext(), 
//			//					"Best Size:\n" +
//			//					String.valueOf(myBestSize.width) + " : " + String.valueOf(myBestSize.height), 
//			//					Toast.LENGTH_LONG).show();
//			//			}
//		}
//
//		@Override
//		public void surfaceCreated(SurfaceHolder holder) {
//			try {
////				File myFile = new File("/sdcard/1.mp4");
////				myFile.createNewFile();
//				recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//		        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//		        CamcorderProfile cpHigh = CamcorderProfile
//		                .get(CamcorderProfile.QUALITY_HIGH);
//		        recorder.setProfile(cpHigh);
//		        recorder.setOutputFile("/sdcard/videocapture_example.mp4");
//		        recorder.setMaxDuration(50000); // 50 seconds
//		        recorder.setMaxFileSize(5000000); 
////					recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
////					recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
////					recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
////					recorder.setOutputFile("/sdcard/1.mp4");
//					recorder.setPreviewDisplay(mySurfaceHolder.getSurface());
////					recorder.prepare();
////					myCamera.setPreviewDisplay(mySurfaceHolder);
//
////				} catch (IOException e) {
////					e.printStackTrace();
//				} catch (Exception e) {
//					String message = e.getMessage();
//					e.printStackTrace();
//				}
//		}
//
//		@Override
//		public void surfaceDestroyed(SurfaceHolder holder) {
//			if (recording) {
//	            recorder.stop();
//	            recording = false;
//	        }
//			if(recorder!=null)
//			{
//				recorder.release();
//		        finish();
//				recorder = null;
//			}
//
//		}
//
//	};
//	private boolean recording;
//
//	//	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){
//	//		Camera.Size bestSize = null;
//	//		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//	//
//	//		bestSize = sizeList.get(0);
//	//
//	//		for(int i = 1; i < sizeList.size(); i++){
//	//			if((sizeList.get(i).width * sizeList.get(i).height) >
//	//			(bestSize.width * bestSize.height)){
//	//				bestSize = sizeList.get(i);
//	//			}
//	//		}
//	//
//	//		return bestSize;
//	//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		myCamera = Camera.open();
//	}
//
//	@Override
//	protected void onPause() {
//
//		if(isPreview){
//			myCamera.stopPreview();
//		}
//		recorder.stop();
//		myCamera.release();
//		myCamera = null;
//		isPreview = false;
//
//		super.onPause();
//	}
//
//	private Camera.Size getBestPreviewSize(int width, int height,
//			Camera.Parameters parameters) {
//		Camera.Size result=null;
//
//		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
//			if (size.width<=width && size.height<=height) {
//				if (result==null) {
//					result=size;
//				}
//				else {
//					int resultArea=result.width*result.height;
//					int newArea=size.width*size.height;
//
//					if (newArea>resultArea) {
//						result=size;
//					}
//				}
//			}
//		}
//		return(result);
//	}
//	
////	@Override
////	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////	// TODO Auto-generated method stub
////	if(resultCode == RESULT_OK){
////	 if(requestCode == REQUEST_VIDEO_CAPTURED){
////	 Uri uriVideo = data.getData();
////	  Toast.makeText(MainActivity.this,
////	    uriVideo.getPath(),
////	    Toast.LENGTH_LONG)
////	    .show();
////	 }
////	}else if(resultCode == RESULT_CANCELED){
////	 Toast.makeText(MainActivity.this,"Cancelled",  Toast.LENGTH_LONG) .show();
////	}
////	}
//	
//	private void initRecorder() {
//        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
//        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
//
//        CamcorderProfile cpHigh = CamcorderProfile
//                .get(CamcorderProfile.QUALITY_HIGH);
//        recorder.setProfile(cpHigh);
//        recorder.setOutputFile("/sdcard/videocapture_example.mp4");
//        recorder.setMaxDuration(50000); // 50 seconds
//        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
//    }
//
//    private void prepareRecorder() {
//        recorder.setPreviewDisplay(mySurfaceHolder.getSurface());
//
//        try {
//            recorder.prepare();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            finish();
//        } catch (IOException e) {
//            e.printStackTrace();
//            finish();
//        }
//    }
//
//}