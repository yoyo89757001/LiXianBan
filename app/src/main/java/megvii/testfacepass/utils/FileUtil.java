package megvii.testfacepass.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by xingchaolei on 2018/1/26.
 */

public class FileUtil {

    public static File[] getFiles(String path){
        File file=new File(path);
        return file.listFiles();
    }

    /**
     * 获取所有存储卡挂载路径
     * @return
     */
    public static List<String> getMountPathList() {
        List<String> pathList = new ArrayList<String>();
        final String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();//取得当前JVM的运行时环境
        try {
            Process p = run.exec(cmd);//执行命令
            BufferedInputStream inputStream = new BufferedInputStream(p.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
               // Log.d("",line);
                //输出信息内容：  /data/media /storage/emulated/0 sdcardfs rw,nosuid,nodev,relatime,uid=1023,gid=1023 0 0
                String[] temp = TextUtils.split(line, " ");
                //分析内容可看出第二个空格后面是路径
                String result = temp[1];
                File file = new File(result);
                //类型为目录、可读、可写，就算是一条挂载路径
                if (file.isDirectory() && file.canRead() && file.canWrite()) {
                   // Logger.d("add --> "+file.getAbsolutePath());
                    pathList.add(result);
                }

                // 检查命令是否执行失败
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    Log.d("","命令执行失败!");
                }
            }
            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            Log.d("",e.toString()+"f");
            //命令执行异常，就添加默认的路径
            pathList.add(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return pathList;
    }



    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     * @param
     */
    public static List<String> getAllFiles(String dirPath, List<String> fileList) {
        File f = new File(dirPath);
        Log.d("FileUtil", "f.exists():" + f.exists()+dirPath);
        if (!f.exists()) {//判断路径是否存在
            return null;
        }

        File[] files = f.listFiles();
        Log.d("FileUtil", "files.length:" + files.length);
        if(files==null){//判断权限
            return null;
        }

        Log.d("FileUtil", "文件夹个数" + files.length);

        for (File _file : files) {//遍历目录
            if(_file.isFile() && (_file.getName().endsWith("jpg")|| _file.getName().endsWith("png") )){
                String _name=_file.getName();
                String filePath = _file.getAbsolutePath();//获取文件路径
              //  String fileName = _file.getName().substring(0,_name.length()-4);//获取文件名
              //  Log.d("LOGCAT","fileName:"+fileName);
               // Log.d("LOGCAT","filePath:"+filePath);
                try {
                    fileList.add(filePath);

                }catch (Exception e){
                    Log.d("FileUtil", e.getMessage()+"获取usb文件异常");
                }
            } else if(_file.isDirectory()){//查询子目录
                getAllFiles(_file.getAbsolutePath(),fileList);
            }
        }
        Log.d("FileUtil", "返回的jsonArray:" + fileList.size());
        return fileList;
    }




    //兼容 sdk>=19 版本 图片选择获取路径问题
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                //   if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
                //     }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
