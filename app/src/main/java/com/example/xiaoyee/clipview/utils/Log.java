package com.example.xiaoyee.clipview.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by xiaoyee on 16/7/20.
 * 日志打印类
 */
public class Log {
    public static void d(String tag, String info){
        Logger.t(tag).d(info);
    }
    
    public static void i(String tag, String info) {
        Logger.t(tag).i(info);
    }
}
