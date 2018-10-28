package com.android.launcher3.util.system;

import android.content.Context;
import android.content.Intent;

/**
 * @author tic
 *         created on 18/10/28.
 */

public class Activities {
    private static final String TAG = "Activities";

    public static void goTo(Context context, String action) {
        Intent intent = new Intent(action);
        context.startActivity(intent);
    }
}
