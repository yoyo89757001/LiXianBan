package megvii.testfacepass;



import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import android.os.Environment;
import android.support.multidex.MultiDexApplication;
import android.util.Log;



import java.io.File;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;


/**
 * Created by tangjun on 14-8-24.
 */
public class MyApplication extends MultiDexApplication {


	private final static String TAG = "CookiesManager";
	public static MyApplication myApplication;
	public static OkHttpClient okHttpClient=null;
//	private DaoMaster.DevOpenHelper mHelper;
//	public DaoMaster mDaoMaster;
//	public DaoSession mDaoSession;
	// 超时时间


	@Override
	public void onCreate() {
		super.onCreate();

		try {


//			setDatabase();

		} catch (Exception e) {
			Log.d(TAG, e.getMessage()+"主程序");
		}
			myApplication = this;

		String path= Environment.getExternalStorageDirectory()+ File.separator+"yanshi";
		File destDir = new File(path);

		if (!destDir.exists()) {
			destDir.mkdirs();
		}


	}



//	//旋转适配,如果应用屏幕固定了某个方向不旋转的话(比如qq和微信),下面可不写.
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		ScreenAdapterTools.getInstance().reset(this);
//	}

	/**
	 * @param path
	 * @return
	 */
	public static Bitmap decodeImage(String path) {
		Bitmap res;
		try {
			ExifInterface exif = new ExifInterface(path);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = 1;
			op.inJustDecodeBounds = false;
			//op.inMutable = true;
			res = BitmapFactory.decodeFile(path, op);
			//rotate and scale.
			Matrix matrix = new Matrix();

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				matrix.postRotate(90);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				matrix.postRotate(180);
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				matrix.postRotate(270);
			}

			Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
			Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

			if (!temp.equals(res)) {
				res.recycle();
			}
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




	public static MyApplication getAppContext() {
		return myApplication;
	}

	/**
	 * 设置greenDao
	 */
//	private void setDatabase() {
//		// 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
//		// 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
//		// 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
//		// 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//		mHelper = new DaoMaster.DevOpenHelper(this, "noteukyooy", null);
//		SQLiteDatabase db = mHelper.getWritableDatabase();
//		// 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//		mDaoMaster = new DaoMaster(db);
//		mDaoSession = mDaoMaster.newSession();
//
//	}


	//public  DaoSession getDaoSession() {
	//	return mDaoSession;
	//}



	public File getDiskCacheDir(String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = MyApplication.getAppContext().getExternalCacheDir().getPath();
		} else {
			cachePath = MyApplication.getAppContext().getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	public int getAppVersion() {
		try {
			PackageInfo info = MyApplication.getAppContext().getPackageManager().getPackageInfo(MyApplication.getAppContext().getPackageName(), 0);
			return info.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

}
