package com.android.launcher3.util;

import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pair;

import com.android.launcher3.InvariantDeviceProfile;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.LauncherAppState;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author tic
 * created on 18-10-18
 */
public class SpaceHelper {

    private static final String TAG = "SpaceHelper";
    private final LauncherAppState mApp;

    public SpaceHelper(LauncherAppState app) {
        this.mApp = app;
    }

    public Pair<Long, int[]> findSpaceForItem(LongSparseArray<ArrayList<ItemInfo>> screenItems,
                                              ArrayList<Long> workspaceScreens,
                                              ArrayList<Long> addedWorkspaceScreensFinal) {
        // Find appropriate space for the item.
        long screenId = 0;
        int[] cordinates = new int[2];
        boolean found = false;

        int screenCount = workspaceScreens.size();
        Log.e(TAG, "screens:" + Arrays.toString(workspaceScreens.toArray()));
        // start with the second screen
        final int second = 1;
        int preferredScreenIndex = screenCount <= second ? screenCount : second;
        if (preferredScreenIndex < screenCount) {
            screenId = workspaceScreens.get(preferredScreenIndex);
            found = findNextAvailableIconSpaceInScreen(
                    screenItems.get(screenId), cordinates, 1, 1);
        }
        if (!found) {
            // Search on any of the screens starting from the second screen
            for (int screen = second; screen < screenCount; screen++) {
                screenId = workspaceScreens.get(screen);
                if (screenId < 0) {
                    break;
                }
                if (findNextAvailableIconSpaceInScreen(
                        screenItems.get(screenId), cordinates, 1, 1)) {
                    // We found a space for it
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            // Still no position found. Add a new screen to the end.
            long maxScreenId = workspaceScreens.get(workspaceScreens.size() - 1);
            screenId = maxScreenId + 1;

            // Save the screen id for binding in the workspace
            workspaceScreens.add(screenId);
            addedWorkspaceScreensFinal.add(screenId);

            // If we still can't find an empty space, then God help us all!!!
            if (!findNextAvailableIconSpaceInScreen(
                    screenItems.get(screenId), cordinates, 1, 1)) {
                throw new RuntimeException("Can't find space to add the item");
            }
        }
        return Pair.create(screenId, cordinates);
    }

    private boolean findNextAvailableIconSpaceInScreen(ArrayList<ItemInfo> occupiedPos,
                                                       int[] xy, int spanX, int spanY) {
        InvariantDeviceProfile profile = mApp.getInvariantDeviceProfile();

        final int xCount = profile.numColumns;
        final int yCount = profile.numRows;
        boolean[][] occupied = new boolean[xCount][yCount];
        if (occupiedPos != null) {
            for (ItemInfo r : occupiedPos) {
                int right = r.cellX + r.spanX;
                int bottom = r.cellY + r.spanY;
                for (int x = r.cellX; 0 <= x && x < right && x < xCount; x++) {
                    for (int y = r.cellY; 0 <= y && y < bottom && y < yCount; y++) {
                        occupied[x][y] = true;
                    }
                }
            }
        }
        return findVacantCell(xy, spanX, spanY, xCount, yCount, occupied);
    }

    private boolean findVacantCell(int[] vacant, int spanX, int spanY,
                                   int xCount, int yCount, boolean[][] occupied) {
        for (int y = 0; (y + spanY) <= yCount; y++) {
            for (int x = 0; (x + spanX) <= xCount; x++) {
                boolean available = !occupied[x][y];
                out:
                for (int i = x; i < x + spanX; i++) {
                    for (int j = y; j < y + spanY; j++) {
                        available = available && !occupied[i][j];
                        if (!available) {
                            break out;
                        }
                    }
                }
                if (available) {
                    vacant[0] = x;
                    vacant[1] = y;
                    return true;
                }
            }
        }
        return false;
    }

}
