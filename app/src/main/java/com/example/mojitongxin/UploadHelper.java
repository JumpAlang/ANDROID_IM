package com.sunhaobin.imapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.Date;

public class UploadHelper {
    private static final String TAG="UploadHelper";
    private static final String AccessKeyId="LTAI4Fx7GQBzutchdMUMZTDF";
    private static final String AccessKeySecret="opWMoU74QBYjolLiqBWw7D2bMa3cku";
    private static final String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";
    private static final String BUCKET_NAME = "sunhaobin";

    private static final Object lock=new Object();

    private OSS ossClient;
    private static volatile UploadHelper mUploadHelper;

    public UploadHelper() {
        Log.d(TAG, "Constructor");
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                AccessKeyId, AccessKeySecret);
        MyApplication.getInstance().getApplicationContext();
//        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(AccessKeyId, AccessKeySecret, "<StsToken.SecurityToken>");
        ossClient = new OSSClient(MyApplication.getInstance(), ENDPOINT, credentialProvider);
    }

    public static UploadHelper getInstance() {
        if(mUploadHelper==null){
            synchronized(lock){
                if(mUploadHelper==null){
                    mUploadHelper=new UploadHelper();
                }
            }
        }
        return mUploadHelper;
    }


    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upLoad(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upLoad(key, path);
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upLoad(key, path);
    }


    public String upLoad(String objectKey,String path){
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectKey, path);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        String url = ossClient.presignPublicObjectURL(BUCKET_NAME, objectKey);
        return url;
    }
    /**
     * 分月存储，避免一个文件夹太多
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }
}
