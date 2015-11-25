package com.icenler.lib.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by iCenler - 2015/7/15.
 * Description：Log 工具
 * 1、TAG 自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * 2、customTagPrefix 为空时只输出：className.methodName(L:lineNumber)
 * 3、通过设置日志等级标记位控制是否输出
 */
public class LogUtil {


    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String customTagPrefix = "iCenler";

    public static boolean CONTROLSWITCH = true;

    // 日志等级控制是否输出
    public static boolean allowD = true;// Debug
    public static boolean allowE = true;// Error
    public static boolean allowI = true;// Info
    public static boolean allowV = true;// Verbose
    public static boolean allowW = true;// Warn
    public static boolean allowWtf = true;// what a terrible failure

    private static final int JSON_INDENT = 4;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    static {
        if (!CONTROLSWITCH) {
            allowD = allowE = allowI = allowV = allowW = allowWtf = false;
        }
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;

//        可跳转日志输出格式
//        String className = caller.getFileName();
//        String methodName = caller.getMethodName();
//        int lineNumber = caller.getLineNumber();
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("(").append(className).append(":").append(lineNumber).append(")#").append(methodName).append("");

        return tag;
    }

    /**
     * Log 日志输出扩展，可实现 CustomLogger 接口进行自定义
     */
    public static CustomLogger customLogger;

    public static void setCustomLogger(CustomLogger logger) {
        customLogger = logger;
    }

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    public static void d(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
            Log.e(tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
            Log.i(tag, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
            Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
            Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
            Log.w(tag, tr);
        }
    }

    public static void wtf(String content) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content);
        } else {
            Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
            Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, tr);
        } else {
            Log.wtf(tag, tr);
        }
    }

    /**
     * 格式化 Json 输出
     *
     * @param content
     */
    public static void json(String content) {
        if (TextUtils.isEmpty(content)) {
            d("Empty or Null json content");
            return;
        }

        String message = null;
        try {
            if (content.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(content);
                message = jsonObject.toString(JSON_INDENT);
            } else if (content.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(content);
                message = jsonArray.toString(JSON_INDENT);
            }
        } catch (JSONException e) {
            e(e.getCause().getMessage() + "\n" + content);
            return;
        }

        String tag = generateTag(getCallerStackTraceElement());
        Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        String[] lines = message.split(LINE_SEPARATOR);
        StringBuilder jsonContent = new StringBuilder();
        for (String line : lines) {
            jsonContent.append("║ ").append(line).append(LINE_SEPARATOR);
        }
        Log.d(tag, jsonContent.toString());
        Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }

    /**
     * StackTraceElement >>>
     * String getClassName() - 返回类的完全限定名，该类包含由该堆栈跟踪元素所表示的执行点。
     * String getFileName()  - 返回源文件名，该文件包含由该堆栈跟踪元素所表示的执行点。
     * int getLineNumber()   - 返回源行的行号，该行包含由该堆栈该跟踪元素所表示的执行点。
     * String getMethodName()- 返回方法名，此方法包含由该堆栈跟踪元素所表示的执行点。
     * String toString()     - 返回表示该堆栈跟踪元素的字符串。
     *
     * @return 线程堆栈转储的堆栈跟踪元素数组（Ps：index 可通过断点调试查看）
     */
    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

}
