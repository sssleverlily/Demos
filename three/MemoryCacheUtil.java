package com.example.administrator.three;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2018/4/3.
 */
//内存缓存
public class MemoryCacheUtil {
    private LruCache<String, Bitmap> mMemoryCache;
    //LruCache来解决内存不回收或提前回收的问题(来自百度hh)


    public  MemoryCacheUtil(){
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;// 模拟器默认是16M
        mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getRowBytes() * value.getHeight();// 获取图片占用内存大小
                return byteCount;
            }
        };
    }

    public Bitmap getBitMapFromMemory(String url) {
        return mMemoryCache.get(url);
    }

    public void setBitmapToMemory(String url, Bitmap bitmap){
        mMemoryCache.put(url, bitmap);
    }



}
