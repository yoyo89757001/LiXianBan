package megvii.testfacepass;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.ImageFormat;
import android.graphics.Matrix;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.SpannableString;

import android.text.TextUtils;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import butterknife.internal.Utils;
import megvii.facepass.FacePassException;
import megvii.facepass.FacePassHandler;
import megvii.facepass.types.FacePassAddFaceResult;
import megvii.facepass.types.FacePassConfig;
import megvii.facepass.types.FacePassDetectionResult;
import megvii.facepass.types.FacePassFace;

import megvii.facepass.types.FacePassImage;
import megvii.facepass.types.FacePassImageRotation;
import megvii.facepass.types.FacePassImageType;
import megvii.facepass.types.FacePassModel;
import megvii.facepass.types.FacePassPose;
import megvii.facepass.types.FacePassRecognitionResult;
import megvii.facepass.types.FacePassRecognitionResultType;

import megvii.testfacepass.adapter.FaceTokenAdapter;
import megvii.testfacepass.adapter.GroupNameAdapter;
import megvii.testfacepass.beans.YanZhiBean;
import megvii.testfacepass.camera.CameraManager;
import megvii.testfacepass.camera.CameraPreview;
import megvii.testfacepass.camera.CameraPreviewData;

import megvii.testfacepass.cookies.CookiesManager;
import megvii.testfacepass.utils.FileUtil;
import megvii.testfacepass.utils.GsonUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class YanShiActivity extends Activity implements CameraManager.CameraListener {

    private static final String DEBUG_TAG = "FacePassDemo";

    private static final int MSG_SHOW_TOAST = 1;
    private int dw,dh;
    private static final int DELAY_MILLION_SHOW_TOAST = 2000;

    /* 识别服务器IP */

  //  private static final String serverIP_offline = "10.104.44.50";//offline
   // private static final String serverIP_online = "10.199.1.14";
  //  private static String serverIP;

    private static final String authIP = "https://api-cn.faceplusplus.com";
    private static final String apiKey = "CKbSYQqAuc5AzCMoOK-kbo9KaabtEciQ";
    private static final String apiSecret = "HeZgW5ILE83nKkqF-QO5IqEEmeRxPgeI";

    private static String recognize_url;

    /* 人脸识别Group */
    private static final String group_name = "face-pass-test-x";

    /* 程序所需权限 ：相机 文件存储 网络访问 */
    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    private static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private String[] Permission = new String[]{PERMISSION_CAMERA, PERMISSION_WRITE_STORAGE, PERMISSION_READ_STORAGE, PERMISSION_INTERNET, PERMISSION_ACCESS_NETWORK_STATE};


    /* SDK 实例对象 */
    FacePassHandler mFacePassHandler;

    /* 相机实例 */
    private CameraManager manager;


    /* 相机预览界面 */
    private CameraPreview cameraView;

    private boolean isLocalGroupExist = false;

    /* 在预览界面圈出人脸 */
    private FaceView faceView;



    /* 相机是否使用前置摄像头 */
    private static boolean cameraFacingFront = true;
    /* 相机图片旋转角度，请根据实际情况来设置
     * 对于标准设备，可以如下计算旋转角度rotation
     * int windowRotation = ((WindowManager)(getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
     * Camera.CameraInfo info = new Camera.CameraInfo();
     * Camera.getCameraInfo(cameraFacingFront ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK, info);
     * int cameraOrientation = info.orientation;
     * int rotation;
     * if (cameraFacingFront) {
     *     rotation = (720 - cameraOrientation - windowRotation) % 360;
     * } else {
     *     rotation = (windowRotation - cameraOrientation + 360) % 360;
     * }
     */
    private int cameraRotation;

    private static final int cameraWidth = 1920;
    private static final int cameraHeight = 1080;

    private int mSecretNumber = 0;
    private static final long CLICK_INTERVAL = 600;
    private long mLastClickTime;


    private int heightPixels;
    private int widthPixels;

    int screenState = 0;// 0 横 1 竖

    private final int TIMEOUT=1000*10;
    FacePassModel trackModel;
    FacePassModel poseModel;
    FacePassModel blurModel;
    FacePassModel livenessModel;
    FacePassModel searchModel;
    FacePassModel detectModel;
    FacePassModel ageGenderModel;

    private static List<YanZhiBean> yanZhiBeans=new ArrayList<>(5);


    FrameLayout frameLayout;
    private int buttonFlag = 0;
    private Button settingButton;

    /*Toast 队列*/
    LinkedBlockingQueue<Toast> mToastBlockQueue;

    LinkedBlockingQueue<YanZhiBean> yanzhengBlockQueue;
    /*DetectResult queue*/
    ArrayBlockingQueue<byte[]> mDetectResultQueue;

    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;

    /*recognize thread*/
    RecognizeThread mRecognizeThread;

    FeedFrameThread mFeedFrameThread;


    /*底库同步*/
    private ImageView ceshi;
    private AlertDialog mSyncGroupDialog;

    private Handler mAndroidHandler;

    private  static ValueAnimator animator = ValueAnimator.ofFloat(1f, 10f);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToastBlockQueue = new LinkedBlockingQueue<>();
        yanzhengBlockQueue = new LinkedBlockingQueue<>();
        mDetectResultQueue = new ArrayBlockingQueue<>(5);
        mFeedFrameQueue = new ArrayBlockingQueue<>(1);
        initAndroidHandler();


        /* 初始化界面 */
        initView();
        /* 申请程序所需权限 */
        if (!hasPermission()) {
            requestPermission();
        } else {
            initFacePassSDK();
        }

        initFaceHandler();
        mRecognizeThread = new RecognizeThread();
        mRecognizeThread.start();

        mFeedFrameThread = new FeedFrameThread();
        mFeedFrameThread.start();

        animator.setDuration(1000L);
        animator.setRepeatCount(-1);
        animator.start();


    }

    private void initAndroidHandler() {

        mAndroidHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_SHOW_TOAST:
                        if (mToastBlockQueue.size() > 0) {
                            Toast toast = mToastBlockQueue.poll();
                            if (toast != null) {
                                toast.show();
                            }
                        }
                        if (mToastBlockQueue.size() > 0) {
                            removeMessages(MSG_SHOW_TOAST);
                            sendEmptyMessageDelayed(MSG_SHOW_TOAST, DELAY_MILLION_SHOW_TOAST);
                        }
                        break;
                }
            }
        };
    }

    private void initFacePassSDK() {
        FacePassHandler.getAuth(authIP, apiKey, apiSecret);
        FacePassHandler.initSDK(getApplicationContext());

    }

    private void initFaceHandler() {

        new Thread() {
            @Override
            public void run() {
                while (true && !isFinishing()) {
                    if (FacePassHandler.isAvailable()) {
                        Log.d(DEBUG_TAG, "start to build FacePassHandler");
                         /* FacePass SDK 所需模型， 模型在assets目录下 */
                        trackModel = FacePassModel.initModel(getApplicationContext().getAssets(), "tracker.DT1.4.1.dingding.20180315.megface2.9.bin");
                        poseModel = FacePassModel.initModel(getApplicationContext().getAssets(), "pose.alfa.tiny.170515.bin");
                        blurModel = FacePassModel.initModel(getApplicationContext().getAssets(), "blurness.v5.l2rsmall.bin");
                        livenessModel = FacePassModel.initModel(getApplicationContext().getAssets(), "panorama.facepass.offline.180312.bin");
                        searchModel = FacePassModel.initModel(getApplicationContext().getAssets(), "feat.small.facepass.v2.9.bin");
                        detectModel = FacePassModel.initModel(getApplicationContext().getAssets(), "detector.mobile.v5.fast.bin");
                        ageGenderModel = FacePassModel.initModel(getApplicationContext().getAssets(), "age_gender.bin");
                        /* SDK 配置 */
                        float searchThreshold = 75f;
                        float livenessThreshold = 70f;
                        boolean livenessEnabled = true;
                        int faceMinThreshold = 80;
                        FacePassPose poseThreshold = new FacePassPose(30f, 30f, 30f);
                        float blurThreshold = 0.2f;
                        float lowBrightnessThreshold = 70f;
                        float highBrightnessThreshold = 210f;
                        float brightnessSTDThreshold = 60f;
                        int retryCount = 2;
                        int rotation = cameraRotation;
                        String fileRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        FacePassConfig config;
                        try {

                            /* 填入所需要的配置 */
                            config = new FacePassConfig(searchThreshold, livenessThreshold, livenessEnabled,
                                    faceMinThreshold, poseThreshold, blurThreshold,
                                    lowBrightnessThreshold, highBrightnessThreshold, brightnessSTDThreshold,
                                    retryCount, rotation, fileRootPath,
                                    trackModel, poseModel, blurModel, livenessModel, searchModel, detectModel, ageGenderModel);
                            /* 创建SDK实例 */
                            mFacePassHandler = new FacePassHandler(config);
                            checkGroup();
                        } catch (FacePassException e) {
                            e.printStackTrace();
                            Log.d(DEBUG_TAG, "FacePassHandler is null");
                            return;
                        }
                        return;
                    }
                    try {
                        /* 如果SDK初始化未完成则需等待 */
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        checkGroup();
        initToast();
        /* 打开相机 */
        if (hasPermission()) {
            manager.open(getWindowManager(), cameraFacingFront, cameraWidth, cameraHeight);
        }
        adaptFrameLayout();
        super.onResume();
    }


    private void checkGroup() {
        if (mFacePassHandler == null) {
            return;
        }
        String[] localGroups = mFacePassHandler.getLocalGroups();
        isLocalGroupExist = false;
        if (localGroups == null || localGroups.length == 0) {
            faceView.post(new Runnable() {
                @Override
                public void run() {
                    toast("请创建" + group_name + "底库");
                }
            });
            return;
        }
        for (String group : localGroups) {
            if (group_name.equals(group)) {
                isLocalGroupExist = true;
            }
        }
        if (!isLocalGroupExist) {
            faceView.post(new Runnable() {
                @Override
                public void run() {
                    toast("请创建" + group_name + "底库");
                }
            });
        }
    }


    /* 相机回调函数 */
    @Override
    public void onPictureTaken(CameraPreviewData cameraPreviewData) {

        /* 如果SDK实例还未创建，则跳过 */
        if (mFacePassHandler == null) {
            return;
        }
         /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */

        FacePassImage image;
        try {
            image = new FacePassImage(cameraPreviewData.nv21Data, cameraPreviewData.width, cameraPreviewData.height, cameraRotation, FacePassImageType.NV21);
        } catch (FacePassException e) {
            e.printStackTrace();
            return;
        }

        mFeedFrameQueue.offer(image);
    }

    private class FeedFrameThread extends Thread {
        boolean isIterrupt;

        @Override
        public void run() {
            while (!isIterrupt) {
                try {
                    FacePassImage image = mFeedFrameQueue.take();
                    /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */
                    FacePassDetectionResult detectionResult = null;
                    detectionResult = mFacePassHandler.feedFrame(image);
                    if (detectionResult == null || detectionResult.faceList.length == 0) {
                        faceView.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                faceView.invalidate();
                            }
                        });
                    } else {
                      //  Log.d("FeedFrameThread", "image:" + image);
                        showFacePassFace(detectionResult.faceList,image);
                    }


                        /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                        if (detectionResult != null && detectionResult.message.length != 0) {
                          //  Log.d(DEBUG_TAG, "mDetectResultQueue.offer");
                            mDetectResultQueue.offer(detectionResult.message);
                            Log.d(DEBUG_TAG, "1 mDetectResultQueue.size = " + mDetectResultQueue.size());
                        }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (FacePassException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RecognizeThread extends Thread {

        boolean isInterrupt;

        @Override
        public void run() {
            while (!isInterrupt) {
                try {
                    Log.d(DEBUG_TAG, "2 mDetectResultQueue.size = " + mDetectResultQueue.size());
                    byte[] detectionResult = mDetectResultQueue.take();

                    Log.d(DEBUG_TAG, "mDetectResultQueue.isLocalGroupExist");
                    if (isLocalGroupExist) {
                        Log.d(DEBUG_TAG, "mDetectResultQueue.recognize");
                        FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(group_name, detectionResult);
                        if (recognizeResult != null && recognizeResult.length > 0) {
                            for (FacePassRecognitionResult result : recognizeResult) {
                                String faceToken = new String(result.faceToken);
                                if (FacePassRecognitionResultType.RECOG_OK == result.facePassRecognitionResultType) {
                                    getFaceImageByFaceToken(result.trackId, faceToken);
                                }
//                                showRecognizeResult(result.trackId, result.detail.searchScore, result.detail.livenessScore, !TextUtils.isEmpty(faceToken));
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (FacePassException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void interrupt() {
            isInterrupt = true;
            super.interrupt();
        }
    }




    /* 判断程序是否有所需权限 android22以上需要自申请权限 */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_READ_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_INTERNET) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /* 请求程序所需权限 */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(Permission, PERMISSIONS_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED)
                    granted = false;
            }
            if (!granted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (!shouldShowRequestPermissionRationale(PERMISSION_CAMERA)
                            || !shouldShowRequestPermissionRationale(PERMISSION_READ_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_WRITE_STORAGE)
                            || !shouldShowRequestPermissionRationale(PERMISSION_INTERNET)
                            || !shouldShowRequestPermissionRationale(PERMISSION_ACCESS_NETWORK_STATE)) {
                        Toast.makeText(getApplicationContext(), "需要开启摄像头网络文件存储权限", Toast.LENGTH_SHORT).show();
                    }
            } else {
                initFacePassSDK();
            }
        }
    }

    private void adaptFrameLayout() {
        SettingVar.isButtonInvisible = false;
        SettingVar.iscameraNeedConfig = false;
    }

    private void initToast() {
        SettingVar.isButtonInvisible = false;
    }

    private void initView() {

        @SuppressLint("WrongConstant")
        int windowRotation = ((WindowManager) (getApplicationContext().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation() * 90;
        if (windowRotation == 0) {
            cameraRotation = FacePassImageRotation.DEG90;
        } else if (windowRotation == 90) {
            cameraRotation = FacePassImageRotation.DEG0;
        } else if (windowRotation == 270) {
            cameraRotation = FacePassImageRotation.DEG180;
        } else {
            cameraRotation = FacePassImageRotation.DEG270;
        }
        Log.i(DEBUG_TAG, "cameraRation: " + cameraRotation);
        cameraFacingFront = true;
        SharedPreferences preferences = getSharedPreferences(SettingVar.SharedPrefrence, Context.MODE_PRIVATE);
        SettingVar.isSettingAvailable = preferences.getBoolean("isSettingAvailable", SettingVar.isSettingAvailable);
        SettingVar.isCross = preferences.getBoolean("isCross", SettingVar.isCross);
        SettingVar.faceRotation = preferences.getInt("faceRotation", SettingVar.faceRotation);
        SettingVar.cameraPreviewRotation = preferences.getInt("cameraPreviewRotation", SettingVar.cameraPreviewRotation);
        SettingVar.cameraFacingFront = preferences.getBoolean("cameraFacingFront", SettingVar.cameraFacingFront);


        if (SettingVar.isSettingAvailable) {
            cameraRotation = SettingVar.faceRotation;
            cameraFacingFront = SettingVar.cameraFacingFront;
        }


        Log.i("orientation", String.valueOf(windowRotation));
        final int mCurrentOrientation = getResources().getConfiguration().orientation;

        if (mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            screenState = 1;
        } else if (mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenState = 0;
        }
        setContentView(R.layout.activity_yanshi);

        ceshi= (ImageView) findViewById(R.id.ceshi);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        heightPixels = displayMetrics.heightPixels;
        widthPixels = displayMetrics.widthPixels;
        SettingVar.mHeight = heightPixels;
        SettingVar.mWidth = widthPixels;
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/Univers LT 57 Condensed.ttf");
        /* 初始化界面 */
        faceView = (FaceView) this.findViewById(R.id.fcview);

        settingButton = (Button) this.findViewById(R.id.settingid);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YanShiActivity.this, SettingActivity.class);
                startActivity(intent);
                YanShiActivity.this.finish();
            }
        });
        SettingVar.cameraSettingOk = false;
        manager = new CameraManager();
        cameraView = (CameraPreview) findViewById(R.id.preview);
        manager.setPreviewDisplay(cameraView);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        /* 注册相机回调函数 */
        manager.setListener(this);

        dw = getDisplaySize(YanShiActivity.this).x;
        dh = getDisplaySize(YanShiActivity.this).y;
        faceView.setWH(dw,dh);

    }

    /**
     * Returns the screen/display size
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    @Override
    protected void onStop() {
        SettingVar.isButtonInvisible = false;
        mToastBlockQueue.clear();
        mDetectResultQueue.clear();
        mFeedFrameQueue.clear();
        if (manager != null) {
            manager.release();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        faceView.clear();
        faceView.invalidate();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mRecognizeThread.isInterrupt = true;

        mRecognizeThread.interrupt();

        if (manager != null) {
            manager.release();
        }
        if (mToastBlockQueue != null) {
            mToastBlockQueue.clear();
        }
        if (mAndroidHandler != null) {
            mAndroidHandler.removeCallbacksAndMessages(null);
        }

        if (mFacePassHandler != null) {
            mFacePassHandler.release();
        }
        if (mFeedFrameQueue != null) {
            mFeedFrameQueue.clear();
        }
        super.onDestroy();
    }


    private void showFacePassFace(FacePassFace[] detectResult, final FacePassImage image) {
        final FacePassFace[] result = detectResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceView.clear();
                YanZhiBean yanZhiBean=yanzhengBlockQueue.poll();
                int size = 0;
                if (yanZhiBean!=null) {
                    yanZhiBeans.add(yanZhiBean);
                     size = yanZhiBeans.size();
                    if (size > 6) {
                        yanZhiBeans.remove(0);
                        size =size- 1;

                    }
                }else {
                    size = yanZhiBeans.size();
                }


                for (FacePassFace face : result) {
                    if (size==0){
                        boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
                        // SpannableString faceViewString = new SpannableString(faceIdString);
                        // faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        StringBuilder faceGenderString = new StringBuilder();
                        switch (face.gender) {
                            case 0:
                                faceGenderString.append("性别: 男性");
                                break;
                            case 1:
                                faceGenderString.append("性别: 女性");
                                break;
                            default:
                                faceGenderString.append("性别: 未知");
                        }

                        Matrix mat = new Matrix();
                        int w = cameraView.getMeasuredWidth();
                        int h = cameraView.getMeasuredHeight();

                        int cameraHeight = manager.getCameraheight();
                        int cameraWidth = manager.getCameraWidth();

                        float left = 0;
                        float top = 0;
                        float right = 0;
                        float bottom = 0;
                        switch (cameraRotation) {
                            case 0:
                                left = face.rect.left;
                                top = face.rect.top;
                                right = face.rect.right;
                                bottom = face.rect.bottom;
                                mat.setScale(mirror ? -1 : 1, 1);
                                mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
                                mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                                break;
                            case 90:
                                mat.setScale(mirror ? -1 : 1, 1);
                                mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                                mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                                left = face.rect.top;
                                top = cameraWidth - face.rect.right;
                                right = face.rect.bottom;
                                bottom = cameraWidth - face.rect.left;
                                break;
                            case 180:
                                mat.setScale(1, mirror ? -1 : 1);
                                mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
                                mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                                left = face.rect.right;
                                top = face.rect.bottom;
                                right = face.rect.left;
                                bottom = face.rect.top;
                                break;
                            case 270:
                                mat.setScale(mirror ? -1 : 1, 1);
                                mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                                mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                                left = cameraHeight - face.rect.bottom;
                                top = face.rect.left;
                                right = cameraHeight - face.rect.top;
                                bottom = face.rect.right;
                        }

                        RectF drect = new RectF();
                        RectF srect = new RectF(left, top, right, bottom);

                        mat.mapRect(drect, srect);
                        faceView.addRect(drect);
                        faceView.addId("ID = " + face.trackId);
                        faceView.addRoll("旋转: " + (int) face.pose.roll + "°");
                        faceView.addPitch("上下: " + (int) face.pose.pitch + "°");
//                        faceView.addYaw("左右: " + (int) face.pose.yaw + "°");
//                    faceView.addBlur("模糊: " + String.format("%.2f", face.blur));
                        if (face.pose.pitch<20 && face.pose.pitch>-20 && face.pose.roll<16 && face.pose.roll>-16 && face.pose.yaw<16 && face.pose.yaw>-16 && face.blur<0.2){
                            faceView.addAge("年龄: " + face.age);
                            faceView.addGenders(faceGenderString.toString());
                            faceView.addBlur("颜值: 请正对屏幕" );
                            faceView.addYaw("眼镜: 请正对屏幕");

                            //获取角度较好的bitmap 然后去公网拿到颜值等信息，带入id,  将id跟现在的id比对 相同的话就更新颜值等信息上去，不相同不做更新
                            try{
                                //获取图片
                                YuvImage image2 = new YuvImage(image.image, ImageFormat.NV21, image.width, image.height, null);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                image2.compressToJpeg(new Rect(0, 0, image.width, image.height), 100, stream);
                                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                                stream.close();
                                Bitmap bitmap=Bitmap.createBitmap(bmp,face.rect.left,face.rect.top,
                                        face.rect.right-face.rect.left,face.rect.bottom-face.rect.top);
                                getOkHttpClient2(bitmap,face);


                            }catch(Exception ex){
                                Log.e("Sys","Error:"+ex.getMessage());
                            }

                        }else {
                            faceView.addAge("年龄: 请正对屏幕" );
                            faceView.addGenders("性别: 请正对屏幕" );
                            faceView.addYaw("眼镜: 分析中...");
                            faceView.addBlur("颜值: 分析中..." );
                        }
                    }

                    int p=0;
                    YanZhiBean lingshi=null;
                    for (int i=0;i<size;i++){
                        lingshi=yanZhiBeans.get(i);
                        if (face.trackId==lingshi.getFacePassFace().trackId){
                            p=1;
                            break;
                        }
                    }
                    if (p==1){

                        //相等
                        boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
                        // SpannableString faceViewString = new SpannableString(faceIdString);
                        // faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        StringBuilder faceGenderString = new StringBuilder();
                        switch (lingshi.getFacePassFace().gender) {
                            case 0:
                                faceGenderString.append("性别: 男性");
                                break;
                            case 1:
                                faceGenderString.append("性别: 女性");
                                break;
                            default:
                                faceGenderString.append("性别: 未知");
                        }

                        Matrix mat = new Matrix();
                        int w = cameraView.getMeasuredWidth();
                        int h = cameraView.getMeasuredHeight();

                        int cameraHeight = manager.getCameraheight();
                        int cameraWidth = manager.getCameraWidth();

                        float left = 0;
                        float top = 0;
                        float right = 0;
                        float bottom = 0;
                        switch (cameraRotation) {
                            case 0:
                                left = face.rect.left;
                                top = face.rect.top;
                                right = face.rect.right;
                                bottom = face.rect.bottom;
                                mat.setScale(mirror ? -1 : 1, 1);
                                mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
                                mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                                break;
                            case 90:
                                mat.setScale(mirror ? -1 : 1, 1);
                                mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                                mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                                left = face.rect.top;
                                top = cameraWidth - face.rect.right;
                                right = face.rect.bottom;
                                bottom = cameraWidth - face.rect.left;
                                break;
                            case 180:
                                mat.setScale(1, mirror ? -1 : 1);
                                mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
                                mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                                left = face.rect.right;
                                top = face.rect.bottom;
                                right = face.rect.left;
                                bottom = face.rect.top;
                                break;
                            case 270:
                                mat.setScale(mirror ? -1 : 1, 1);
                                mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                                mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                                left = cameraHeight - face.rect.bottom;
                                top = face.rect.left;
                                right = cameraHeight - face.rect.top;
                                bottom = face.rect.right;
                        }

                        RectF drect = new RectF();
                        RectF srect = new RectF(left, top, right, bottom);

                        mat.mapRect(drect, srect);
                        faceView.addRect(drect);
                        faceView.addId("ID = " + face.trackId);
                        faceView.addRoll("旋转: " + (int) lingshi.getFacePassFace().pose.roll + "°");
                        faceView.addPitch("上下: " + (int) lingshi.getFacePassFace().pose.pitch + "°");
                     //   faceView.addYaw("左右: " + (int) lingshi.getFacePassFace().pose.yaw + "°");
//                           faceView.addBlur("模糊: " + String.format("%.2f", face.blur));

                        faceView.addAge("年龄: " + lingshi.getFacePassFace().age);
                        faceView.addGenders(faceGenderString.toString());
                        faceView.addBlur("颜值: " + (lingshi.getFacePassFace().gender==1?lingshi.getFaces().get(0).getAttributes().getBeauty().getFemale_score():lingshi.getFaces().get(0).getAttributes().getBeauty().getMale_score()));
                        String sp=lingshi.getFaces().get(0).getAttributes().getGlass().getValue();
                        if (sp.equals("None")){
                            faceView.addYaw("未佩戴眼镜");
                        }else if (sp.equals("Dark")){
                            faceView.addYaw("佩戴了墨镜");
                        }else {
                            faceView.addYaw("佩戴了普通眼镜");
                        }

                    }
                    else {
                            //不想等的
                            boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
                            // SpannableString faceViewString = new SpannableString(faceIdString);
                            // faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            StringBuilder faceGenderString = new StringBuilder();
                            switch (face.gender) {
                                case 0:
                                    faceGenderString.append("性别: 男性");
                                    break;
                                case 1:
                                    faceGenderString.append("性别: 女性");
                                    break;
                                default:
                                    faceGenderString.append("性别: 未知");
                            }

                            Matrix mat = new Matrix();
                            int w = cameraView.getMeasuredWidth();
                            int h = cameraView.getMeasuredHeight();

                            int cameraHeight = manager.getCameraheight();
                            int cameraWidth = manager.getCameraWidth();

                            float left = 0;
                            float top = 0;
                            float right = 0;
                            float bottom = 0;
                            switch (cameraRotation) {
                                case 0:
                                    left = face.rect.left;
                                    top = face.rect.top;
                                    right = face.rect.right;
                                    bottom = face.rect.bottom;
                                    mat.setScale(mirror ? -1 : 1, 1);
                                    mat.postTranslate(mirror ? (float) cameraWidth : 0f, 0f);
                                    mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                                    break;
                                case 90:
                                    mat.setScale(mirror ? -1 : 1, 1);
                                    mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                                    mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                                    left = face.rect.top;
                                    top = cameraWidth - face.rect.right;
                                    right = face.rect.bottom;
                                    bottom = cameraWidth - face.rect.left;
                                    break;
                                case 180:
                                    mat.setScale(1, mirror ? -1 : 1);
                                    mat.postTranslate(0f, mirror ? (float) cameraHeight : 0f);
                                    mat.postScale((float) w / (float) cameraWidth, (float) h / (float) cameraHeight);
                                    left = face.rect.right;
                                    top = face.rect.bottom;
                                    right = face.rect.left;
                                    bottom = face.rect.top;
                                    break;
                                case 270:
                                    mat.setScale(mirror ? -1 : 1, 1);
                                    mat.postTranslate(mirror ? (float) cameraHeight : 0f, 0f);
                                    mat.postScale((float) w / (float) cameraHeight, (float) h / (float) cameraWidth);
                                    left = cameraHeight - face.rect.bottom;
                                    top = face.rect.left;
                                    right = cameraHeight - face.rect.top;
                                    bottom = face.rect.right;
                            }

                            RectF drect = new RectF();
                            RectF srect = new RectF(left, top, right, bottom);

                            mat.mapRect(drect, srect);
                            faceView.addRect(drect);
                            faceView.addId("ID = " + face.trackId);
                            faceView.addRoll("旋转: " + (int) face.pose.roll + "°");
                            faceView.addPitch("上下: " + (int) face.pose.pitch + "°");
                         //   faceView.addYaw("左右: " + (int) face.pose.yaw + "°");
//                    faceView.addBlur("模糊: " + String.format("%.2f", face.blur));
                            if (face.pose.pitch<20 && face.pose.pitch>-20 && face.pose.roll<16 && face.pose.roll>-16 && face.pose.yaw<16 && face.pose.yaw>-16 && face.blur<0.2){
                                faceView.addAge("年龄: " + face.age);
                                faceView.addGenders(faceGenderString.toString());
                                faceView.addBlur("颜值: 请正对屏幕" );
                                faceView.addYaw("眼镜: 识别中...");

                                //获取角度较好的bitmap 然后去公网拿到颜值等信息，带入id,  将id跟现在的id比对 相同的话就更新颜值等信息上去，不相同不做更新
                                try{
                                    //获取图片
                                    YuvImage image2 = new YuvImage(image.image, ImageFormat.NV21, image.width, image.height, null);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    image2.compressToJpeg(new Rect(0, 0, image.width, image.height), 100, stream);
                                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                                    stream.close();
                                    Bitmap bitmap=Bitmap.createBitmap(bmp,face.rect.left,face.rect.top,
                                            face.rect.right-face.rect.left,face.rect.bottom-face.rect.top);
                                    getOkHttpClient2(bitmap,face);

                                }catch(Exception ex){
                                    Log.e("Sys","Error:"+ex.getMessage());
                                }

                            }else {
                                faceView.addAge("年龄: 请正对屏幕" );
                                faceView.addGenders("性别: 请正对屏幕" );
                                faceView.addBlur("颜值: 分析中..." );
                                faceView.addYaw("眼镜: 识别中...");
                            }
                    }

                }
                faceView.invalidate();
            }
        });

    }

    public void showToast(CharSequence text, int duration, boolean isSuccess, Bitmap bitmap) {
        LayoutInflater inflater = getLayoutInflater();
        View toastView = inflater.inflate(R.layout.toast, null);
        LinearLayout toastLLayout = (LinearLayout) toastView.findViewById(R.id.toastll);
        if (toastLLayout == null) {
            return;
        }
        toastLLayout.getBackground().setAlpha(100);
        ImageView imageView = (ImageView) toastView.findViewById(R.id.toastImageView);
        TextView idTextView = (TextView) toastView.findViewById(R.id.toastTextView);
        TextView stateView = (TextView) toastView.findViewById(R.id.toastState);
        SpannableString s;
        if (isSuccess) {
            s = new SpannableString("验证成功");
            imageView.setImageResource(R.drawable.success);
        } else {
            s = new SpannableString("验证失败");
            imageView.setImageResource(R.drawable.success);
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        stateView.setText(s);
        idTextView.setText(text);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(toastView);

        if (mToastBlockQueue.size() == 0) {
            mAndroidHandler.removeMessages(MSG_SHOW_TOAST);
            mAndroidHandler.sendEmptyMessage(MSG_SHOW_TOAST);
            mToastBlockQueue.offer(toast);
        } else {
            mToastBlockQueue.offer(toast);
        }
    }


    //首先登录-->获取所有主机-->创建或者删除或者更新门禁
    private void getOkHttpClient2(final Bitmap bitmap, final FacePassFace facePassFace){
        final String batt=FileUtil.bitmapToBase64(bitmap);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new CookiesManager())
                .retryOnConnectionFailure(true)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("api_key", "BBDRR-nwJM38qGHUiBV0k4eMUZ2jDsa1")
                .add("api_secret", "YDhcIhcjc5OVnDVQvspwSoSQnjM-fWYn")
                .add("image_base64", batt)
                .add("return_attributes", "emotion,eyestatus,beauty")
                .build();

        Request.Builder requestBuilder = new Request.Builder();
        //requestBuilder.header("User-Agent", "Koala Admin");
        //requestBuilder.header("Content-Type","application/json");
        requestBuilder.post(body);
        requestBuilder.url("https://api-cn.faceplusplus.com/facepp/v3/detect");
        final Request request = requestBuilder.build();

        Call mcall= okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ffffff", "请求失败"+e.getMessage());
             //   SystemClock.sleep(800);
//                try {
//                    basket.take();
//                    if (basket.size()==0)
//                        isA=true;
//                } catch (InterruptedException e1) {
//                    basket.clear();
//                    isA=true;
//                    e1.printStackTrace();
//                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String s=response.body().string();
                    Log.d("fffffff", "检测"+s);
                    JsonObject jsonObject = GsonUtil.parse(s).getAsJsonObject();
                    Gson gson = new Gson();
                    YanZhiBean menBean = gson.fromJson(jsonObject, YanZhiBean.class);
                    if (menBean.getFaces()!=null && menBean.getFaces().get(0)!=null ){
                        menBean.setFacePassFace(facePassFace);
                        yanzhengBlockQueue.put(menBean);
                    }


                }catch (Exception e){
                    Log.d("YanShiActivity", e.getMessage()+"");
                }finally {
//                    SystemClock.sleep(800);
//                    try {
//                        basket.take();
//                        if (basket.size()==0)
//                            isA=true;
//                    } catch (InterruptedException e1) {
//                        basket.clear();
//                        isA=true;
//                        e1.printStackTrace();
//                    }
                }

            }
        });

    }

    private void getFaceImageByFaceToken(final long trackId, String faceToken) {
        if (TextUtils.isEmpty(faceToken)) {
            return;
        }

            try {
                final Bitmap bitmap = mFacePassHandler.getFaceImage(faceToken.getBytes());
                mAndroidHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(DEBUG_TAG, "getFaceImageByFaceToken cache is null");
                        showToast("ID = " + String.valueOf(trackId), Toast.LENGTH_SHORT, true, bitmap);
                    }
                });

            } catch (FacePassException e) {
                e.printStackTrace();
            }

    }





    private void toast(String msg) {
        Toast.makeText(YanShiActivity.this, msg, Toast.LENGTH_SHORT).show();
    }



}
