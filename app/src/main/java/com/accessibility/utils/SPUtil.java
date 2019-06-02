package com.accessibility.utils;

/**
 * 简化SharedPreferences的帮助类.
 * 支持自定义名称
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SPUtil {

    /**
     * SharedPreferences存储在sd卡中的文件名字
     */
    private static String getSpName(Context context, String name) {
        return context.getPackageName() + name + "_preferences";
    }

    private static String getSpName(Context context) {
        return getSpName(context, "");
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void putAndApply(Context context, String key, Object object, String name) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, "" + object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public static void putAndApply(Context context, String key, Object object) {
        putAndApply(context, key, object, "");
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(Context context, String key, Object defaultObject, String name) {
        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return null;
        }
    }

    public static Object get(Context context, String key, Object defaultObject) {
        return get(context, key, defaultObject, "");
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key, String name) {
        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    public static void remove(Context context, String key) {
        remove(context, key, "");
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    public static void clear(Context context) {
        clear(context, "");
    }


    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key, String name) {
        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    public static boolean contains(Context context, String key) {
        return contains(context, key, "");
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static Map<String, ?> getAll(Context context) {
        return getAll(context, "");
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {

        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException expected) {
            } catch (IllegalAccessException expected) {
            } catch (InvocationTargetException expected) {
            }
            editor.commit();
        }
    }


    /**
     * 将对象储存到sharepreference
     *
     * @param key
     * @param device
     * @param <T>
     */
    public static <T> boolean saveDeviceData(Context context, String key, T device, String name) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(device);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            sp.edit().putString(key, oAuth_Base64).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将对象从shareprerence中取出来
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getDeviceData(Context context, String key, String name) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);

        T device = null;
        String productBase64 = sp.getString(key, null);

        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);

            // 读取对象
            device = (T) bis.readObject();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return device;
    }


    /**
     * 将对象储存到sharepreference
     *
     * @param key
     * @param parcelable
     * @param <T>
     */
    public static <T> boolean saveParcelableData(Context context, String key, Parcelable parcelable, String name) {
        try {
            byte[] bytes = marshall(parcelable);
            SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sp.edit();
            spEditor.putString(key, Base64.encodeToString(bytes, 0));
            spEditor.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将对象从shareprerence中取出来
     *
     * @param key
     * @param name
     * @param <T>
     */
    public static Object getParcelableData(Context context, Parcelable.Creator creator, String key, String name) {
        SharedPreferences sp = context.getSharedPreferences(getSpName(context, name), Context.MODE_PRIVATE);
        Parcel parcel = unmarshall(Base64.decode(sp.getString(key, "default").getBytes(), 0));
        return creator.createFromParcel(parcel);
    }

    public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parcel.setDataPosition(0);
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        return parcel;
    }
}
