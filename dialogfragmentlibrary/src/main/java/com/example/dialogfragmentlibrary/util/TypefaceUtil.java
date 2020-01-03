package com.example.dialogfragmentlibrary.util;

import android.content.Context;
import android.graphics.Typeface;

import androidx.collection.SimpleArrayMap;

import com.example.dialogfragmentlibrary.R;

import kotlin.text.Regex;

public class TypefaceUtil {

    private static TypefaceUtil mTypefaceUtil;
    private static SimpleArrayMap<String, Typeface> mCache = new SimpleArrayMap<>();

    private TypefaceUtil() {
        // cannot be instantiated
    }

    public static synchronized TypefaceUtil getInstance() {
        if (mTypefaceUtil == null) {
            mTypefaceUtil = new TypefaceUtil();
        }
        return mTypefaceUtil;
    }

    public static void releaseInstance() {
        if (mTypefaceUtil != null) {
            mTypefaceUtil = null;
        }
        if (mCache != null) {
            mCache.clear();
            mCache = null;
        }
    }


    public synchronized Typeface get(Context ctx, String name) {
        try {
            if (!mCache.containsKey(name)) {
                Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), String.format(ctx.getResources().getString(R.string.file_ttf), name));
                mCache.put(name, typeface);
                return typeface;
            }
        } catch (Exception e) {

        }
        return mCache.get(name);
    }
}
