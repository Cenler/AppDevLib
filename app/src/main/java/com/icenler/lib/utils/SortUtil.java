package com.icenler.lib.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by iCenler - 2015/7/13.
 * Description： 排序相关工具类
 */
public class SortUtil {

    public static final int COMPARE_TYPE_STRING = 0;
    public static final int COMPARE_TYPE_INT = 1;
    public static final int COMPARE_TYPE_LONG = 2;

    private static <T> int doCompare(final String method, final Class[] methodArgsClass,
                                     final Object[] methodArgs, final String order, Object object1, Object object2,
                                     int compareType, boolean ignoreCharType) {
        int result = 0;
        try {
            Method compareMethod1 = object1.getClass().getMethod(method, methodArgsClass);
            Method compareMethod2 = object2.getClass().getMethod(method, methodArgsClass);

            if (null == compareMethod1.invoke(object1, methodArgs) || null == compareMethod2.invoke(object2, methodArgs)) {
                return result;
            }

            if (compareType == COMPARE_TYPE_INT) {
                //按int类型比较
                int value1 = (Integer) compareMethod1.invoke(object1, methodArgs);
                int value2 = (Integer) compareMethod1.invoke(object2, methodArgs);
                if (value1 == value2) {
                    return result = 0;
                }
                if (order != null && "DESC".equals(order)) {
                    result = value2 > value1 ? 1 : -1;
                } else {
                    result = value1 > value2 ? 1 : -1;
                }

            } else if (compareType == COMPARE_TYPE_LONG) {
                //按long类型比较
                long value1 = (Long) compareMethod1.invoke(object1, methodArgs);
                long value2 = (Long) compareMethod1.invoke(object2, methodArgs);
                if (value1 == value2) {
                    return result = 0;
                }
                if (order != null && "DESC".equals(order)) {
                    result = value2 > value1 ? 1 : -1;
                } else {
                    result = value1 > value2 ? 1 : -1;
                }
            } else if (compareType == COMPARE_TYPE_STRING) {
                //按long类型比较
                String value1 = (String) compareMethod1.invoke(object1, methodArgs);
                String value2 = (String) compareMethod1.invoke(object2, methodArgs);
                if (value1 != null && value2 != null) {
                    if (!ignoreCharType) {
                        return compareString(value1, value2);
                    } else {
                        return value1.compareTo(value2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private static int compareString(String oneString, String anotherString) {
        int len1 = oneString.length();
        int len2 = anotherString.length();
        int lim = Math.min(len1, len2);
        char v1[] = oneString.toCharArray();
        char v2[] = anotherString.toCharArray();

        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];

            //如果是英文，则排在前面
            if (isLetter(c1) && !isLetter(c2)) {
                return -1;
            }

            //如果是数字，则排在前面
            if (isNumeric(c1) && !isNumeric(c2)) {
                //第二个是英文，英文排在前面
                if (isLetter(c2)) {
                    return 1;
                } else {//其他情况，数字排在前面
                    return -1;
                }
            }

            //如果是中文（非数字），则排在前面
            if (isChinese(c1) && !isChinese(c2)) {
                if (isLetter(c2) || isNumeric(c2)) {
                    return 1;
                } else {
                    return -1;
                }
            }

            if (c1 != c2) {
                return c1 - c2;
            }

            k++;
        }
        return len1 - len2;
    }


    private static <T> int doCompare(final String method, final Class[] methodArgsClass,
                                     final Object[] methodArgs, final String order, Object object1, Object object2,
                                     int compareType) {
        return doCompare(method, methodArgsClass, methodArgs, order, object1, object2, compareType, false);
    }

    // 是否是英文
    private static boolean isLetter(char c) {
        return Character.isUpperCase(c) || Character.isLowerCase(c);
    }

    // 是否是数字
    private static boolean isNumeric(char c) {
        return Character.isDigit(c);
    }

    // 是否是中文
    private static boolean isChinese(char key) {
        return (key >= 0x4e00 && key <= 0x9fa5) ? true : false;
    }

    public static <T> void sortByString(List<T> list, final String method, final Class[] methodArgsClass, final Object[] methodArgs, final String order) {
        try {
            Collections.sort(list, new Comparator() {

                @Override
                public int compare(Object object1, Object object2) {
                    return doCompare(method, methodArgsClass, methodArgs, order, object1, object2, COMPARE_TYPE_STRING, true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    public static <T> void sortByStringCritical(List<T> list, final String method, final Class[] methodArgsClass, final Object[] methodArgs, final String order) {
        try {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object object1, Object object2) {
                    return doCompare(method, methodArgsClass, methodArgs, order, object1, object2, COMPARE_TYPE_STRING, false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> void sortByInteger(List<T> list, final String method, final Class[] methodArgsClass, final Object[] methodArgs, final String order) {
        // 宽松汉字拼音排序
        try {
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object object1, Object object2) {
                    return doCompare(method, methodArgsClass, methodArgs, order, object1, object2, COMPARE_TYPE_INT);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

}
