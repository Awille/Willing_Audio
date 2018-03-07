package com.example.wille.willing_audio;

/**
 * Created by Administrator on 2017\12\29 0029.
 */

public class ClickFilter
{
    public static final long INTERVAL = 00L; //防止连续点击的时间间隔
    private static long lastClickTime = 0L; //上一次点击的时间

    public static boolean filter()
    {
        long time = System.currentTimeMillis();
        if ( ( time - lastClickTime ) > INTERVAL )
        {
            lastClickTime = time;
            return false;
        }
        lastClickTime = time;
        return true;
    }
}