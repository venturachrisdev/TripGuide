package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.google.android.gms.common.SignInButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    ProfileFragment.AuthListener authListener;
    Disposable disposable;

    @BindView(R.id.sign_in_button)
    SignInButton googleSignInBtn;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragment.AuthListener && context instanceof ReviewAdapter.ReviewProfileListener) {
            authListener = (ProfileFragment.AuthListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AuthListener and ReviewProfileListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication)getActivity().getApplication()).getNetComponent())
                .build()
                .inject(this);
        ButterKnife.bind(this, v);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                authListener.signInWithGoogle();
            }
        });
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
