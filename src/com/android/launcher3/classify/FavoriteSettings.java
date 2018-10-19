package com.android.launcher3.classify;

/**
 * 应用，文件夹配置
 *
 * @author tic
 * created on 18-10-19
 */
public class FavoriteSettings {

    public static class Classify {
        /** 应用推荐 */
        public static final int TYPE_RECOMMEND = -216;

        public static final int TYPE_SYSTEM = 1;
        public static final int TYPE_TOOLS = 2;
        public static final int TYPE_SHOPPING = 3;
        public static final int TYPE_SOCIAL = 4;
        public static final int TYPE_EFFICIENCY = 5;
        public static final int TYPE_GAME = 6;

        public static boolean isNoType(int classify) {
            return classify == 0;
        }

        public static String classifyToString(int type) {
            switch (type) {
                case TYPE_SYSTEM:
                    return "系统";
                case TYPE_TOOLS:
                    return "工具";
                case TYPE_SHOPPING:
                    return "购物";
                case TYPE_SOCIAL:
                    return "社交";
                case TYPE_EFFICIENCY:
                    return "效率";
                case TYPE_GAME:
                    return "游戏";
                default:
                    return String.valueOf(type);
            }
        }
    }
}
