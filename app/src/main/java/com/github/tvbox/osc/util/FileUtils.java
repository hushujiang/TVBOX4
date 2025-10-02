package com.github.tvbox.osc.util;

import android.os.Environment;
import android.text.TextUtils;
import com.github.tvbox.osc.base.App;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static Map<String, byte[]> cacheMap = new HashMap<>();

    // 新增方法：读取模块
    public static String readModule(String moduleName) {
        try {
            if (cacheMap.containsKey(moduleName)) {
                return new String(cacheMap.get(moduleName));
            }
            return getAssetFile(moduleName);
        } catch (Exception e) {
            return "";
        }
    }

    // 新增方法：写入缓存
    public static void writeCache(String key, byte[] data) {
        cacheMap.put(key, data);
    }

    // 新增方法：读取缓存字节
    public static byte[] readCache(String key) {
        return cacheMap.getOrDefault(key, null);
    }

    // ... [保持原有方法不变] ...
    
    public static String getAssetFile(String assetName) throws IOException {
        InputStream is = App.getInstance().getAssets().open(assetName);
        byte[] data = new byte[is.available()];
        is.read(data);
        return new String(data, "UTF-8");
    }

    // ... [保持其余代码不变] ...
}
    public static String getAssetFile(String assetName) throws IOException {
        InputStream is = App.getInstance().getAssets().open(assetName);
        byte[] data = new byte[is.available()];
        is.read(data);
        return new String(data, "UTF-8");
    }

    public static boolean isAssetFile(String name, String path) {
        try {
            for(String one : App.getInstance().getAssets().list(path)) {
                if (one.equals(name)) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static File getLocal(String path) {
        return new File(path.replace("file:/", getRootPath()));
    }

    public static File getCacheDir() {
        return App.getInstance().getCacheDir();
    }

    public static String getCachePath() {
        return getCacheDir().getAbsolutePath();
    }

    public static void cleanDirectory(File dir) {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) return;
        for(File one : files) {
            try {
                deleteFile(one);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(File file) {
        if (!file.exists()) return;
        if (file.isFile()) {
            if (file.canWrite()) file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                if (file.canWrite()) file.delete();
                return;
            }
            for(File one : files) {
                deleteFile(one);
            }
        }
        return;
    }

    public static void cleanPlayerCache() {
        String ijkCachePath = getCachePath() + "/ijkcaches/";
        String thunderCachePath = getCachePath() + "/thunder/";
        File ijkCacheDir = new File(ijkCachePath);
        File thunderCacheDir = new File(thunderCachePath);
        try {
            if (ijkCacheDir.exists()) cleanDirectory(ijkCacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (thunderCacheDir.exists()) cleanDirectory(thunderCacheDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String read(String path) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getLocal(path))));
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) sb.append(text).append("\n");
            br.close();
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getFileName(String filePath){
        if(TextUtils.isEmpty(filePath)) return "";
        String fileName = filePath;
        int p = fileName.lastIndexOf(File.separatorChar);
        if(p != -1){
            fileName = fileName.substring(p + 1);
        }
        return fileName;
    }

    public static String getFileNameWithoutExt(String filePath){
        if(TextUtils.isEmpty(filePath)) return "";
        String fileName = filePath;
        int p = fileName.lastIndexOf(File.separatorChar);
        if(p != -1){
            fileName = fileName.substring(p + 1);
        }
        p = fileName.indexOf('.');
        if(p != -1){
            fileName = fileName.substring(0, p);
        }
        return fileName;
    }

    public static String getFileExt(String fileName){
        if(TextUtils.isEmpty(fileName)) return "";
        int p = fileName.lastIndexOf('.');
        if(p != -1) {
            return fileName.substring(p).toLowerCase();
        }
        return "";
    }

    public static boolean hasExtension(String path) {
        int lastDotIndex = path.lastIndexOf(".");
        int lastSlashIndex = Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"));
        // 如果路径中有点号，并且点号在最后一个斜杠之后，认为有后缀
        return lastDotIndex > lastSlashIndex && lastDotIndex < path.length() - 1;
    }
}
