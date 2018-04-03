package com.example.administrator.three;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/4/3.
 */
//图片加载的工具类
public class MyUtils {
    private DisCacheUtil disCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;
    private NetCacheUtils netCacheUtils;

    public MyUtils(){
        disCacheUtil =new DisCacheUtil();
        memoryCacheUtil =new MemoryCacheUtil();
        netCacheUtils =new NetCacheUtils(disCacheUtil,memoryCacheUtil);

    }

    public void display(ImageView imageView,String url){
        //从内存中读取
       Bitmap fromMemory =memoryCacheUtil.getBitMapFromMemory(url);
       //如果内存中有的话就直接返回
        if (fromMemory != null) {
            imageView.setImageBitmap(fromMemory);

            return;
        }
       //从磁盘中读取
        Bitmap fromDis = disCacheUtil.getBitmapFromLocal(url);
        if (fromDis != null) {
            imageView.setImageBitmap(fromDis);

            disCacheUtil.setBitmapToLocal(url,fromDis);

            return;
        }
        //从网络中读取
       netCacheUtils.getBitmapFromNet(imageView, url);

    }


 }

