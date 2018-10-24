package com.android.launcher3.classify;

import android.util.ArrayMap;

import com.google.common.base.Preconditions;

/**
 * @author tic
 * created on 18-10-22
 */
public class ClassifyCache {

    private final ArrayMap<String, Integer> mCache = new ArrayMap<>();

    public void init() {
        initShopping();
        initTools();
    }

    private void initGames() {
//        int type = FavoriteSettings.Classify.TYPE_GAME;
//        mCache.put("", type);
    }

    private void initSocial() {
        int type = FavoriteSettings.Classify.TYPE_SOCIAL;
        mCache.put("com.qzone", type);
        mCache.put("com.tencent.mm", type);
        mCache.put("com.tencent.mobileqq", type);
        mCache.put("com.p1.mobile.putong", type);   // 探探
        mCache.put("com.immomo.momo", type);
    }

    private void initTools() {
        int type = FavoriteSettings.Classify.TYPE_TOOLS;
        mCache.put("com.android.documentsui", type);
        mCache.put("com.android.soundrecorder", type);
    }

    private void initShopping() {
        int type = FavoriteSettings.Classify.TYPE_SHOPPING;
        mCache.put("com.taobao.taobao", type);
        mCache.put("com.taobao.idlefish", type);
        mCache.put("com.jingdong.app.mall", type);
        mCache.put("com.xunmeng.pinduoduo", type);
        mCache.put("com.sankuai.meituan", type);
        mCache.put("com.suning.mobile.ebuy", type);
        mCache.put("cn.missfresh.application", type);
    }

    public void cacheSystem(String packageName) {
        Preconditions.checkNotNull(packageName);
        mCache.put(packageName, FavoriteSettings.Classify.TYPE_SYSTEM);
    }

    public ArrayMap<String, Integer> getCache() {
        return mCache;
    }
}