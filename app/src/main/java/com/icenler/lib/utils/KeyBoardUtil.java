package com.icenler.lib.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by iCenler - 2015/7/28：
 * Description：软键盘控制工具类
 */
public class KeyBoardUtil {

    private KeyBoardUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 呼出软键盘
     */
    public static void openKeybord(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

}
