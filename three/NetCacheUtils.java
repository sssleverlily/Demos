package com.example.administrator.three;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018/4/3.
 */
//网络缓存
public class NetCacheUtils {
    private DisCacheUtil mDisCacheUtil;
    private MemoryCacheUtil mMemoryCacheUtils;

    public NetCacheUtils(DisCacheUtil disCacheUtil, MemoryCacheUtil memoryCacheUtils) {
        mDisCacheUtil = disCacheUtil;
        mMemoryCacheUtils = memoryCacheUtils;
    }

   //从网络上下载图片
    public void getBitmapFromNet(ImageView ivPic, String url) {
        new BitmapTask().execute(ivPic, url);//启动AsyncTask，url再根据实际情况改动即可

    }

   //AsyncTask就是对handler和线程池的封装，第一个泛型是参数类型，第二个泛型是更新进度的泛型，第三个泛型是onPostExecute的返回结果(百度hh)
    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView ivPic;
        private String url;

        @Override
        protected Bitmap doInBackground(Object[] params) {
            ivPic = (ImageView) params[0];
            url = (String) params[1];

            return downLoadBitmap(url);
        }


        @Override
        protected void onProgressUpdate(Void[] values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                ivPic.setImageBitmap(result);

                //从网络获取图片后,保存至本地缓存
                mDisCacheUtil.setBitmapToLocal(url, result);
                //保存至内存中
                mMemoryCacheUtils.setBitmapToMemory(url, result);

            }
        }
    }

   //网络下载图片
    private Bitmap downLoadBitmap(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=2;//宽高压缩为原来的1/2
                options.inPreferredConfig=Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(),null,options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return null;
    }

}
