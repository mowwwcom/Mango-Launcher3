package com.android.launcher3.ui.user;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.launcher3.R;
import com.android.launcher3.uioverrides.progress.CircularMusicProgressBar;
import com.mango.launcher3.oauth.wechat.Wechat;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * @author tic
 * created on 18-11-3
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private CircularMusicProgressBar mAvatar;
    private View mRegister;
    private View mLogin;
    /**
     * check again when receive data
     */
    private String csrf;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_login, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAvatar = view.findViewById(R.id.iv_avatar);
        mRegister = view.findViewById(R.id.btn_register);
        mLogin = view.findViewById(R.id.btn_login);

        mRegister.setOnClickListener(this);
        mLogin.setOnClickListener(this);

        view.findViewById(R.id.login_facebook).setOnClickListener(this);
        view.findViewById(R.id.login_google).setOnClickListener(this);
        view.findViewById(R.id.login_twitter).setOnClickListener(this);
        view.findViewById(R.id.login_wechat).setOnClickListener(this);
        view.findViewById(R.id.login_weibo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_login:
                mAvatar.autoProgress();
                new Handler().postDelayed(() -> {
                    mAvatar.clearAnimation();
                    UserInfoFragment f = new UserInfoFragment();
                    setTransition(f);
                    replace(f, mLogin, UserInfoFragment.SHARED_ELEMENT, false);
                }, 3000);
                break;
            case R.id.btn_register:
                RegisterFragment fragment = new RegisterFragment();
                setTransition(fragment);
                replace(fragment, mRegister, RegisterFragment.SHARED_ELEMENT, true);
                break;
            case R.id.login_facebook:
                Toast.makeText(activity, "not support right now", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_google:
                Toast.makeText(activity, "not support right now", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_twitter:
                Toast.makeText(activity, "not support right now", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_wechat:
                IWXAPI api = Wechat.getApi(activity);
                csrf = Wechat.createCSRF();
                Wechat.sendAuthRequest(api, csrf);
                break;
            case R.id.login_weibo:
                Toast.makeText(activity, "not support right now", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    private void setTransition(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailTransition());
            fragment.setEnterTransition(new Fade());
            setExitTransition(new Fade());
            fragment.setSharedElementReturnTransition(new DetailTransition());
        }
    }

    private void replace(Fragment fragment, View sharedElement, String name, boolean toBackStack) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentTransaction transaction = activity.getSupportFragmentManager()
                    .beginTransaction()
                    .addSharedElement(sharedElement, name)
                    .replace(R.id.main_content, fragment);
            if (toBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        }
    }

}
