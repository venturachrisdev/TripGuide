package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements  ApiUtils.AuthFragment {
    @Inject
    SharedPreferences sharedPreferences;

    AccountFragment accountFragment;
    SignInFragment signInFragment;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication)getActivity().getApplication()).getNetComponent())
                .build()
                .inject(this);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.account_layout, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sharedPreferences.contains(Constants.USER_LOGGED_API_TOKEN_SP)) {
            if (accountFragment == null || !accountFragment.isAdded()) {
                showAccountFragment();
            }
        } else {
            if (signInFragment == null || !signInFragment.isAdded()) {
                showSignInFragment();
            }
        }
    }

    private void showAccountFragment() {
        FragmentTransaction transaction = getChildFragmentManager()
                        .beginTransaction();
        if (accountFragment == null) {
            accountFragment = new AccountFragment();
        }
        if (signInFragment != null) {
            transaction.remove(signInFragment);
        }
        transaction.replace(R.id.content, accountFragment);
        transaction.commit();
    }

    private void showSignInFragment() {
        signInFragment = new SignInFragment();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.content, signInFragment)
                .commit();
    }


    @Override
    public void handleSignInResult(final GoogleSignInResult result) {
        showAccountFragment();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                accountFragment.handleSignInResult(result);
            }
        }, 600);
    }

    @Override
    public void handleSignOut(Status status) {
        accountFragment.logout();
        showSignInFragment();
    }

    @Override
    public boolean isUserSaved() {
        return sharedPreferences.contains(Constants.USER_LOGGED_API_TOKEN_SP) &&
                sharedPreferences.contains(Constants.USER_LOGGED_ID_SP);
    }

    public interface AuthListener {
        void signInWithGoogle();
    }
}
