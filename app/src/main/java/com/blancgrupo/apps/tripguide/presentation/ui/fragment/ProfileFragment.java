package com.blancgrupo.apps.tripguide.presentation.ui.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.blancgrupo.apps.tripguide.data.entity.api.UploadPhotoWrapper;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Status;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends LifecycleFragment implements
        ApiUtils.AuthFragment {

    ShimmerRecyclerView recyclerView;
    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    ReviewAdapter.ReviewProfileListener profileListener;

    ProgressDialog dialog;

    boolean logged = false;
    String filePath;
    SignInButton googleSignInBtn;
    AuthListener authListener;

    @BindView(R.id.content)
    FrameLayout frameLayout;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ProfileVMFactory profileVMFactory;
    @Inject
    ProfileRepository profileRepository;

    ProfileViewModel profileViewModel;
    CircleImageView profileImage;

    Disposable disposable;
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
    public void onResume() {
        super.onResume();
        recoverSession();
    }

    public void recoverSession() {
        if (sharedPreferences.contains(Constants.USER_LOGGED_API_TOKEN_SP) &&
                sharedPreferences.contains(Constants.USER_LOGGED_SP)) {
            Profile profile = new Profile();
            profile.setType(sharedPreferences.getString(Constants.USER_LOGGED_TYPE_SP, "email"));
            profile.setTokenId(sharedPreferences.getString(Constants.USER_LOGGED_SP, null));
            profileViewModel.signInOrRegister(profile).observe(this, new Observer<ProfileWrapper>() {
                @Override
                public void onChanged(@Nullable ProfileWrapper profileWrapper) {
                    if (profileWrapper != null) {
                        if (profileWrapper.getProfile() != null && profileWrapper.getStatus().equals("OK")) {
                            initializeProfileLayout(profileWrapper.getProfile(),
                                    profileWrapper.getToken());
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
    public void initializeProfileLayout(Profile profile, String token) {
        frameLayout.removeAllViews();
        frameLayout.removeAllViewsInLayout();
        View profileView = getLayoutInflater().inflate(R.layout.fragment_profile, frameLayout, false);
        frameLayout.addView(profileView);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_LOGGED_ID_SP, profile.get_id());
        editor.putString(Constants.USER_LOGGED_SP, profile.getTokenId());
        editor.putString(Constants.USER_LOGGED_TYPE_SP, profile.getType());
        editor.putString(Constants.USER_LOGGED_API_TOKEN_SP, token);
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

    private void photoIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, 765);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 9898);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 765 && grantResults[0] == PERMISSION_GRANTED) {
            photoIntent();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9898 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                try {
                    String wholeId = DocumentsContract.getDocumentId(uri);
                    String id = wholeId.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = getContext().getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[] {id}, null
                    );
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndex(column[0]);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        } else {
                            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cursor.close();
                    }
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    Cursor c = null;
                    String column = "_data";
                    String[] projection = {column};
                    try {
                        c = getContext().getContentResolver().query(
                                uri, projection, null, null, null
                        );
                        if (c != null && c.moveToFirst()) {
                            int index = c.getColumnIndexOrThrow(column);
                            filePath = c.getString(index);
                        }

                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            }
            File file = new File(filePath);
            Glide.with(getContext())
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(profileImage);
            uploadImage();
        }
    }

    private void uploadImage() {
        dialog = new ProgressDialog(getContext());
        if (filePath != null && filePath.length() > 1) {
            String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
            File file = new File(filePath);
            MultipartBody.Part image = MultipartBody.Part .createFormData("photo", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
            dialog.setMessage(getString(R.string.uploading_image));
            dialog.setIndeterminate(true);
            dialog.show();
            disposable = profileRepository.uploadPhoto(image, apiToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UploadPhotoWrapper>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull
                                                   UploadPhotoWrapper uploadPhotoWrapper)
                                throws Exception {
                            dialog.hide();
                            dialog.cancel();
                            updateProfile(uploadPhotoWrapper.getPhotoUrl());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    });
        }
    }

    private void updateProfile(String photoUrl) {
        String id = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
        String tokenId = sharedPreferences.getString(Constants.USER_LOGGED_SP, null);
        String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);

        Profile profile = new Profile();
        profile.setPhotoUrl(photoUrl);
        profile.set_id(id);
        profile.setTokenId(tokenId);

        dialog.setMessage(getString(R.string.updating_profile));
        dialog.show();
        disposable = profileRepository.changeProfilePhoto(profile, apiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        dialog.hide();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }
                });

    }

    private void bindProfile(View profileView, Profile profile) {
        TextView reviewNumber = profileView.findViewById(R.id.review_number);
        reviewNumber.setText(String.format(getString(R.string.reviews_number), profile.getReviews().size()));
        profileImage = profileView.findViewById(R.id.user_profile_image);
        if (profile.getPhotoUrl() != null && profile.getPhotoUrl().length() > 0) {
            Glide.with(getContext())
                    .load(Constants.API_UPLOAD_URL + profile.getPhotoUrl())
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.mipmap.profile_placeholder)
                    .into(profileImage);
        }
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoIntent();
            }
        });
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
                            initializeProfileLayout(profileWrapper.getProfile(),
                                    profileWrapper.getToken());
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
            Toast.makeText(getContext(), R.string.sign_in_aborted, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleSignOut(Status status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.USER_LOGGED_SP);
        editor.remove(Constants.USER_LOGGED_ID_SP);
        editor.remove(Constants.USER_LOGGED_TYPE_SP);
        editor.remove(Constants.USER_LOGGED_API_TOKEN_SP);
        editor.apply();
        initializeSignInLayout();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
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
