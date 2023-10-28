package com.dxxy.note.util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * 吐司工具类
 */
public class ToastUtil {

    public static void showToast(Context context, String toast) {
        try {
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Looper.prepare();
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }
}
