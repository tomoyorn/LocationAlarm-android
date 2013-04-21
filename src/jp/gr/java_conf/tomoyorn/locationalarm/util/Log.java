package jp.gr.java_conf.tomoyorn.locationalarm.util;

import android.os.Debug;

import jp.gr.java_conf.tomoyorn.locationalarm.BuildConfig;

/**
 * リリースビルド時にはログ出力をしないように{@link android.util.Log}をラップ
 * したクラスです。
 *
 * @author tomoyorn
 */
public class Log {

    private static final String TAG = "Location Alarm";

    public static final void d(String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(TAG, msg);
        }
    }

    public static final void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static final void e(String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(TAG, msg);
        }
    }

    public static final void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, msg);
        }
    }

    public static final void i(String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static final void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(tag, msg);
        }
    }

    public static final void v(String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static final void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, msg);
        }
    }

    public static final void w(String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(TAG, msg);
        }
    }

    public static final void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(tag, msg);
        }
    }

    public static final void heap() {
        heap(TAG);
    }

    public static final void heap(String tag) {
        if (BuildConfig.DEBUG) {
            String msg = "heap: Free="
                    + Long.valueOf(Debug.getNativeHeapFreeSize() / 1024)
                    + "kb" + ", Allocated="
                    + Long.valueOf(Debug.getNativeHeapAllocatedSize() / 1024)
                    + "kb" + ", Size="
                    + Long.valueOf(Debug.getNativeHeapSize() / 1024) + "kb";
            Log.v(tag, msg);
        }
    }
}
