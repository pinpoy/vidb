package com.moogos.spacex.constants;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * ProjectName: ClipboardHelper
 * Description: 剪切板工具
 *
 * review by chenpan, wangkang, wangdong 2017/11/13
 * edit by JeyZheng 2017/11/13
 * author: JeyZheng
 * version: 4.8.0
 * created at: 2017/11/13 10:00
 */
public class ClipboardHelper {

    public static void clipText(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("clipData", text);
        cm.setPrimaryClip(clipData);
    }
}
