package com.example.xiaoyee.clipview.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by xiaoyee on 16/7/20.
 * 日志打印类
 */
public class Log {
    private static boolean isInited = false;
    
    public static void d(String tag, String info){
        if (!isInited) {
            Logger.init(tag).methodCount(3).methodOffset(2).hideThreadInfo();
            isInited = true;
        }
        Logger.d(info);
    }
    
    public static void i(String tag, String info) {
        Logger.t(tag).i(info);
    }
}
