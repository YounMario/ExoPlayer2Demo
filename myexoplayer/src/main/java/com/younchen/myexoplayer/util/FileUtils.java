package com.younchen.myexoplayer.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.younchen.myexoplayer.MyExoPlayerEnv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {


    private static File getExternalCacheDir(Context context, String type) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "files");
        File cacheDir = new File(appCacheDir, type);
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(cacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return cacheDir;
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath, boolean copyDirectory) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {

                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else{
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + File.separator + temp.getName());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();

                } else if (copyDirectory && temp.isDirectory()) {
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i], copyDirectory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------for video player--------------------------------

    public static String convertUrlToLocalPath(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }

        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        File file = new File(MyExoPlayerEnv.getContext().getFilesDir(), fileName);
        return file.getAbsolutePath();
    }

    public static void moveCompleteFilmToFinalPath(String tmpFilePath, String md5) {
        File tmpFile = new File(tmpFilePath);
        String tmpMd5 = Md5Util.getFileMD5(tmpFile);
        if (TextUtils.isEmpty(tmpMd5) || !tmpMd5.equalsIgnoreCase(md5)) {
            return;
        }
        String finalPath = tmpFilePath.replace("complete_", "");
        new File(tmpFilePath).renameTo(new File(finalPath));
        removeFiles(md5);
    }

    private static void removeFiles(String md5) {
        File file = new File(MyExoPlayerEnv.getContext().getFilesDir().getAbsolutePath());
        File[] files = null;
        if (file.isDirectory()) {
            files = file.listFiles();
        }

        if (files == null) return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().startsWith(md5 + "_glue_")) {
                files[i].delete();
            }
        }
    }
}
