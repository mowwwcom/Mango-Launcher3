package com.android.launcher3.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.launcher3.R;
import com.android.launcher3.ui.BaseCompatActivity;

/**
 * User entry for login & register & user Info
 *
 * @author tic
 * created on 18-11-3
 */
public class UserEntryActivity extends BaseCompatActivity {

    private int index;
    private static final int INDEX_LOGIN = 1;
    private static final int INDEX_REGISTER = 2;
    private static final int INDEX_USER_INFO = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.login);
        index = INDEX_LOGIN;
        replace(getFragment(index));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public Fragment getFragment(int index) {
        Fragment f;
        switch (index) {
            case INDEX_LOGIN:
                f = getLoginFragment();
                break;
            case INDEX_REGISTER:
                f = getRegisterFragment();
                break;
            case INDEX_USER_INFO:
                f = getUserInfoFragment();
                break;
            default:
                f = null;
                break;
        }
        return f;
    }

    public Fragment getLoginFragment() {
        return new LoginFragment();
    }

    public Fragment getRegisterFragment() {
        return new LoginFragment();
    }

    public Fragment getUserInfoFragment() {
        return new LoginFragment();
    }

    @Override
    protected int layout() {
        return R.layout.activity_user_entry;
    }
}
