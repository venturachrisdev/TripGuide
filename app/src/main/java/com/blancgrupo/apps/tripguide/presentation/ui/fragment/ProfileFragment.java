package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.PlaceAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Status;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends LifecycleFragment implements
        ApiUtils.AuthFragment {

    ShimmerRecyclerView recyclerView;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    ReviewAdapter.ReviewProfileListener profileListener;

    boolean logged = false;

    SignInButton googleSignInBtn;
    AuthListener authListener;

    @BindView(R.id.content)
    FrameLayout frameLayout;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ProfileVMFactory profileVMFactory;
    ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AuthListener && context instanceof ReviewAdapter.ReviewProfileListener) {
            authListener = (AuthListener) context;
            profileListener = (ReviewAdapter.ReviewProfileListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AuthListener and ReviewProfileListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication)getActivity().getApplication()).getNetComponent())
                .build()
                .inject(this);
        View v = inflater.inflate(R.layout.account_layout, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        profileViewModel = ViewModelProviders.of(this, profileVMFactory)
                .get(ProfileViewModel.class);
        initializeSignInLayout();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sharedPreferences.contains(Constants.USER_LOGGED_SP)) {
            Profile profile = new Profile();
            profile.setType(sharedPreferences.getString(Constants.USER_LOGGED_TYPE_SP, "email"));
            profile.setTokenId(sharedPreferences.getString(Constants.USER_LOGGED_SP, null));
            profileViewModel.signInOrRegister(profile).observe(this, new Observer<ProfileWrapper>() {
                @Override
                public void onChanged(@Nullable ProfileWrapper profileWrapper) {
                    if (profileWrapper != null) {
                        if (profileWrapper.getProfile() != null && profileWrapper.getStatus().equals("OK")) {
                            initializeProfileLayout(profileWrapper.getProfile());
                        } else {
                            Toast.makeText(getContext(), profileWrapper.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
        authListener.loadAccount();
    }

    @Override
    public void initializeProfileLayout(Profile profile) {
        frameLayout.removeAllViews();
        frameLayout.removeAllViewsInLayout();
        View profileView = getLayoutInflater().inflate(R.layout.fragment_profile, frameLayout, false);
        frameLayout.addView(profileView);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_LOGGED_ID_SP, profile.get_id());
        editor.putString(Constants.USER_LOGGED_SP, profile.getTokenId());
        editor.putString(Constants.USER_LOGGED_TYPE_SP, profile.getType());
        editor.apply();
        logged = true;

        bindProfile(profileView, profile);

        recyclerView =  profileView.findViewById(R.id.profile_rv);
        ReviewAdapter reviewAdapter = new ReviewAdapter(ReviewAdapter.REVIEW_PROFILE_TYPE, profileListener);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_profile_layout,
                recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(
                reviewAdapter,
                null,
                emptyView,
                null
        );
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        List<Review> profileReviews = profile.getReviews();
        if (profileReviews !=  null && profileReviews.size() > 0) {
            reviewAdapter.updateData(profile.getReviews());
        } else {
            statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
        }
    }

    private void bindProfile(View profileView, Profile profile) {
        CircleImageView profileImage = profileView.findViewById(R.id.user_profile_image);
        Glide.with(getContext())
                .load(profile.getPhotoUrl())
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.profile_placeholder)
                .into(profileImage);
        TextView profileEmail = profileView.findViewById(R.id.user_profile_email);
        profileEmail.setText(profile.getEmail());
        TextView profileName = profileView.findViewById(R.id.user_profile_name);
        profileName.setText(profile.getName());
    }

    void initializeSignInLayout() {
        frameLayout.removeAllViews();
        frameLayout.removeAllViewsInLayout();
        View signInView = getLayoutInflater().inflate(R.layout.fragment_sign_in, frameLayout, false);
        frameLayout.addView(signInView);
        logged = false;
        googleSignInBtn = signInView.findViewById(R.id.sign_in_button);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                authListener.signInWithGoogle();
            }
        });
    }


    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess() && result.getSignInAccount() != null) {
            GoogleSignInAccount account = result.getSignInAccount();
            Profile profile = new Profile();
            profile.setTokenId(account.getId());
            profile.setName(account.getDisplayName());
            profile.setEmail(account.getEmail());
            if (account.getPhotoUrl() != null) {
                profile.setPhotoUrl(account.getPhotoUrl().toString());
            }
            profile.setType("google");
            profileViewModel.signInOrRegister(profile).observe(this, new Observer<ProfileWrapper>() {
                @Override
                public void onChanged(@Nullable ProfileWrapper profileWrapper) {
                    if (profileWrapper != null) {
                        if (profileWrapper.getProfile() != null && profileWrapper.getStatus().equals("OK")) {
                            initializeProfileLayout(profileWrapper.getProfile());
                        } else {
                            Toast.makeText(getContext(), profileWrapper.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "No inicio sesion", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleSignOut(Status status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.USER_LOGGED_SP);
        editor.remove(Constants.USER_LOGGED_ID_SP);
        editor.remove(Constants.USER_LOGGED_TYPE_SP);
        editor.apply();
        initializeSignInLayout();
    }

    @Override
    public boolean isUserSaved() {
        return sharedPreferences.contains(Constants.USER_LOGGED_SP);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile, menu);
    }

    public interface AuthListener {
        void signInWithGoogle();
        void loadAccount();
    }

}
