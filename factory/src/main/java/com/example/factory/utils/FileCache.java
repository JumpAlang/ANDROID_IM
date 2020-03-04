package com.example.factory.utils;

import com.example.common.common.app.Application;
import com.example.common.utils.HashUtil;
import com.example.factory.net.Network;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 简单的一个文件缓存，实现文件的下载操作
 * 下载成功后回调相应方法
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class FileCache {
    private File baseDir;
    private String ext;
    // 最后一次的目标


    public FileCache(String baseDir, String ext) {
        this.baseDir = new File(Application.getCacheDirFile(), baseDir);
        this.ext = ext;
    }

    // 构建一个缓存文件，同一个网络路径对应一本地的文件
    private File buildCacheFile(String path) {
        String key = HashUtil.getMD5String(path);
        return new File(baseDir, key + "." + ext);
    }

    public String download(String path) {
        // 如果路径就是本地缓存路径，那么不需要下载
        if (path.startsWith(Application.getCacheDirFile().getAbsolutePath())) {
            return path;
        }
        // 构建缓存文件
        final File cacheFile = buildCacheFile(path);

        if (cacheFile.exists() && cacheFile.length() > 0) {
            // 如果文件存在，无须重新下载
            return cacheFile.getPath();
        }

        OkHttpClient client = Network.getOkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();

        // 发起请求
        Call call = client.newCall(request);
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile.getPath();
    }
}
