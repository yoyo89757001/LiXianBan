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

import android.graphics.Matrix;

import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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


import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
import megvii.testfacepass.camera.CameraManager;
import megvii.testfacepass.camera.CameraPreview;
import megvii.testfacepass.camera.CameraPreviewData;

import megvii.testfacepass.utils.FileUtil;


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


    FacePassModel trackModel;
    FacePassModel poseModel;
    FacePassModel blurModel;
    FacePassModel livenessModel;
    FacePassModel searchModel;
    FacePassModel detectModel;
    FacePassModel ageGenderModel;



    FrameLayout frameLayout;
    private int buttonFlag = 0;
    private Button settingButton;

    /*Toast 队列*/
    LinkedBlockingQueue<Toast> mToastBlockQueue;

    /*DetectResult queue*/
    ArrayBlockingQueue<byte[]> mDetectResultQueue;

    ArrayBlockingQueue<FacePassImage> mFeedFrameQueue;

    /*recognize thread*/
    RecognizeThread mRecognizeThread;

    FeedFrameThread mFeedFrameThread;


    /*底库同步*/
    private ImageView mSyncGroupBtn;
    private AlertDialog mSyncGroupDialog;

    private Handler mAndroidHandler;

    private  static ValueAnimator animator = ValueAnimator.ofFloat(1f, 10f);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToastBlockQueue = new LinkedBlockingQueue<>();
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
                        showFacePassFace(detectionResult.faceList);
                    }


                        /*离线模式，将识别到人脸的，message不为空的result添加到处理队列中*/
                        if (detectionResult != null && detectionResult.message.length != 0) {
                            Log.d(DEBUG_TAG, "mDetectResultQueue.offer");
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


    private void showFacePassFace(FacePassFace[] detectResult) {
        final FacePassFace[] result = detectResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                faceView.clear();
                for (FacePassFace face : result) {
                    boolean mirror = cameraFacingFront; /* 前摄像头时mirror为true */
                    StringBuilder faceIdString = new StringBuilder();
                    faceIdString.append("ID = ").append(face.trackId);
                   // SpannableString faceViewString = new SpannableString(faceIdString);
                   // faceViewString.setSpan(new TypefaceSpan("fonts/kai"), 0, faceViewString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    StringBuilder faceRollString = new StringBuilder();
                    faceRollString.append("旋转: ").append((int) face.pose.roll).append("°");
                    StringBuilder facePitchString = new StringBuilder();
                    facePitchString.append("上下: ").append((int) face.pose.pitch).append("°");
                    StringBuilder faceYawString = new StringBuilder();
                    faceYawString.append("左右: ").append((int) face.pose.yaw).append("°");
                    StringBuilder faceBlurString = new StringBuilder();
                    faceBlurString.append("模糊: ").append(String.format("%.2f", face.blur));
                    StringBuilder faceAgeString = new StringBuilder();
                    faceAgeString.append("年龄: ").append(face.age);
                    StringBuilder faceGenderString = new StringBuilder();
                    switch (face.gender) {
                        case 0:
                            faceGenderString.append("性别: 男");
                            break;
                        case 1:
                            faceGenderString.append("性别: 女");
                            break;
                        default:
                            faceGenderString.append("性别: ?");
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
                    faceView.addId(faceIdString.toString());
                    faceView.addRoll(faceRollString.toString());
                    Log.d("YanShiActivity", "facePitchString:"+facePitchString.toString()+"    "+animator.getAnimatedValue());
                    faceView.addPitch(facePitchString.toString());
                    faceView.addYaw(faceYawString.toString());
                    faceView.addBlur(faceBlurString.toString());
                    faceView.addAge(faceAgeString.toString());
                    faceView.addGenders(faceGenderString.toString());
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
