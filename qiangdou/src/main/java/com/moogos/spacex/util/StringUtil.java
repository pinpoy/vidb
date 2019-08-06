package com.moogos.spacex.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaokewang on 2017/12/5.
 */

public class StringUtil {
    /**
     * 检查String是否为空
     *
     * @param s
     * @return
     */
    public static boolean checkEmpty(String s) {
        boolean isEmpty = true;

        if (null != s && !"".equals(s) && !"null".equals(s)) {
            isEmpty = false;
        }
        return isEmpty;
    }

    /**
     * 验证手机号
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
//        p = Pattern.compile("^[1][0-9]{10}$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * @param str
     * @return String
     * @Description:格式化字符串，如果为null,转成""
     * @exception:
     */
    public static String formatString(String str) {

        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }
}
