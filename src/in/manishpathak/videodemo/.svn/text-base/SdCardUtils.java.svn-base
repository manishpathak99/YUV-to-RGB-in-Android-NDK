package in.manishpathak.videodemo;

import in.manishpathak.videodemo.R;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public final class SdCardUtils
{

	public static final String EXPORT_DIR = "DEBUGGING";

			
	//  private static boolean checkFsWritable()
	//  {
	//    File localFile = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM");
	//    if ((!localFile.isDirectory()) && (!localFile.mkdirs()));
	//    for (boolean bool = false; ; bool = localFile.canWrite())
	//      return bool;
	//  }
	//
	//  public static String getRingtonesDirectory(boolean paramBoolean)
	//  {
	//    StringBuilder localStringBuilder = new StringBuilder();
	//    if (!App.IS_FROYO_OR_LATER)
	//      localStringBuilder.append(Environment.getExternalStorageDirectory().toString()).append("/Ringtones");
	//    while (true)
	//    {
	//      if (paramBoolean)
	//        localStringBuilder.append('/');
	//      return localStringBuilder.toString();
	//      localStringBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).toString());
	//    }
	//  }
	//
	//  public static boolean isMediaWritable(boolean paramBoolean)
	//  {
	//    boolean bool = true;
	//    String str = Environment.getExternalStorageState();
	//    if ("mounted".equals(str))
	//      if (!paramBoolean);
	//    for (bool = checkFsWritable(); ; bool = false)
	//      do
	//        return bool;
	//      while ((!paramBoolean) && ("mounted_ro".equals(str)));
	//  }
	//
	//  public static boolean quickHasStorage()
	//  {
	//    return "mounted".equals(Environment.getExternalStorageState());
	//  }

	private static final String TAG = SdCardUtils.class.getSimpleName();

	/***
	 * Read file from SDCard
	 * @param context
	 * @param fileName
	 * @param isAbsolutePath
	 * @return
	 */
	public static FileInputStream readFile(Context context, String fileName, boolean isAbsolutePath){               
		File f = null;
		if(isAbsolutePath){
			f=new File(fileName);
		}else{
			f=new File(Environment.getExternalStorageDirectory() + "/" +  EXPORT_DIR + "/" + fileName);   
		}
		FileInputStream fis = null;             
		try {
			fis=new FileInputStream(f);
		} catch (FileNotFoundException e) {                     
			e.printStackTrace();
		}               
		return fis;
	}

	/**
	 * Read file from SDCard into a byte[] array
	 * @param context
	 * @param fileName
	 * @param isAbsolutePath
	 * @return
	 */
	public static byte[] readBytes(Context context, String fileName, boolean isAbsolutePath){               
		File f = null;
		if(isAbsolutePath){
			f=new File(fileName);
		}else{
			f=new File(Environment.getExternalStorageDirectory() + "/" +  EXPORT_DIR + "/" + fileName);   
		}
		FileInputStream fis = null;             
		ByteArrayOutputStream baos=null;
		try {
			fis=new FileInputStream(f);                     
			baos=new ByteArrayOutputStream(fis.available());
			while(fis.available()>0){
				baos.write(fis.read());
			}
		} catch (FileNotFoundException e) {                     
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}               

		return baos.toByteArray();
	}

	/**
	 * Return absolute path array of all the files
	 * @param context
	 * @param directory
	 * @return
	 */
	public static String[] listFiles(Context context, String directory){            
		File fDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + EXPORT_DIR+"/");
		if(fDir.isDirectory() == false){
			return new String[0];
		}
		String[] fileList=fDir.list();
		int size=fileList.length;
		String[] outList=new String[size];
		for(int i=0;i<size;i++){
			outList[i]=fDir.getAbsolutePath()+"/"+fileList[i];
		}

		return outList;
	}


	public static void writeFile(Context context, String fileName, byte[] data){
		Log.d(TAG, " *************** ABOUT TO MAKE A SAVE**** ");

		// first see if an SD card is even present; abort if not
		if (!Environment.MEDIA_MOUNTED.equals(Environment .getExternalStorageState())) {
			//showOutcomeDialog(R.string.export_failure_title, context.getResources().getString(R.string.export_failure_missing_sd));
			Log.d(TAG, "SD Card is missing");
			return;
		}
		// open a file on the SD card under some useful name, and write out
		FileOutputStream fw = null;
		File f = null;
		try {
			f = new File(Environment.getExternalStorageDirectory() + "/" + EXPORT_DIR);

			f.mkdirs();
			f = new File(Environment.getExternalStorageDirectory() + "/" +  EXPORT_DIR + "/" + fileName);

			if (!f.createNewFile()) {
				f.delete();                             
			}

			fw = new FileOutputStream(f);
			fw.write(data);                 

		} catch (IOException e) {
			//showOutcomeDialog(R.string.export_failure_title, context.getResources().getString(R.string.export_failure_io_error));
			Log.d(TAG, "Io Exception");
			return;
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException ex) {
				}
			}
		}

		if (f == null) {
			//showOutcomeDialog(R.string.export_failure_title,context.getResources().getString(R.string.export_failure_io_error));
			Log.d(TAG, "IO Exception");
			return;
		}
		// Victory! Tell the user and display the filename just written.
		//showOutcomeDialog(R.string.export_success_title, String.format(context.getResources().getString(R.string.export_success), f.getName()));
		Log.d(TAG, String.format("Export succesfull", f.getName()));
	}
	

    public synchronized static boolean hasExternalStorage() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }



    public static boolean isMediaShared() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_SHARED);
    }
    
    
    public static boolean copyFile(Context context,String source, File target) {
        /* force write to sd */

        BufferedInputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(context.getAssets().open(source),8192);
            out = new BufferedOutputStream(new FileOutputStream(target));
            byte[] tmpData = new byte[8192];
            int c;
            while ((c = in.read(tmpData)) != -1) {
                out.write(tmpData, 0, c);
            }

        } catch (Exception e) {
            Log.e(TAG,e.getMessage(),e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {   
                    e.printStackTrace();
                }
                in = null;
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out = null;
            }

        }
        return true;
    }

    public static String getSdcardPath() {
	    File sdDir = null;
	    boolean sdCardExist = Environment.getExternalStorageState().equals(
	        android.os.Environment.MEDIA_MOUNTED); 
	    if (sdCardExist) {
	      sdDir = Environment.getExternalStorageDirectory();
	      return sdDir.toString();
	    }
	    else {
	    	return null;
	    }
	  }
}
