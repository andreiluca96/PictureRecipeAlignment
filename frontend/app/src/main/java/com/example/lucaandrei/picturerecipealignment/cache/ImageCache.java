package com.example.lucaandrei.picturerecipealignment.cache;

import android.util.LruCache;

public class ImageCache {
    private final static LruCache<String, byte[]> mMemoryCache;
    static {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, byte[]>(cacheSize) {
            @Override
            protected int sizeOf(String key, byte[] bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.length / 1024;
            }
        };
    }

    public static void addBitmapToMemoryCache(String key, byte[] bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static byte[] getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void clearMemCache() {
        mMemoryCache.evictAll();
    }



}
