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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Profile;
import com.blancgrupo.apps.tripguide.data.entity.api.ProfileWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.UploadPhotoWrapper;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ProfileDBRepository;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ReviewDBRepository;
import com.blancgrupo.apps.tripguide.domain.model.ProfileModel;
import com.blancgrupo.apps.tripguide.domain.model.ProfileWithReviews;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;
import com.blancgrupo.apps.tripguide.domain.model.mapper.ProfileModelMapper;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.presentation.ui.adapter.ReviewAdapter;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileVMFactory;
import com.blancgrupo.apps.tripguide.presentation.ui.viewmodel.ProfileViewModel;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.ConnectivityUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends LifecycleFragment implements ReviewAdapter.ReviewMenuListener {
    @BindView(R.id.profile_rv)
    ShimmerRecyclerView recyclerView;
    @BindView(R.id.user_profile_name)
    TextView profileName;
    @BindView(R.id.user_profile_email)
    TextView profileEmail;
    @BindView(R.id.user_profile_image)
    CircleImageView profileImage;
    @BindView(R.id.review_number)
    TextView reviewNumber;
    @BindView(R.id.review_number2)
    TextView reviewNumber2;
    @BindView(R.id.xp_progress)
    MaterialProgressBar XP_Progress;
    @BindView(R.id.review_title)
    TextView reviewTitleText;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    StatesRecyclerViewAdapter statesRecyclerViewAdapter;
    ReviewAdapter.ReviewProfileListener profileListener;
    String filePath;
    ProgressDialog dialog;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ProfileVMFactory profileVMFactory;
    @Inject
    ProfileRepository profileRepository;
    @Inject
    ProfileDBRepository profileDBRepository;
    @Inject
    ReviewDBRepository reviewDBRepository;

    ProfileViewModel profileViewModel;
    Disposable disposable;
    ReviewAdapter reviewAdapter;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReviewAdapter.ReviewProfileListener) {
            profileListener = (ReviewAdapter.ReviewProfileListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ReviewProfileListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication)getActivity().getApplication()).getNetComponent())
                .build()
                .inject(this);
        profileViewModel = ViewModelProviders.of(this, profileVMFactory)
                .get(ProfileViewModel.class);
        reviewAdapter = new ReviewAdapter(ReviewAdapter.REVIEW_PROFILE_TYPE, profileListener, this);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_profile_layout,
                recyclerView, false);
        statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(
                reviewAdapter,
                null,
                emptyView,
                null
        );
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(statesRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recoverSession();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (profileName.getText() == null || profileName.getText().length() <= 0) {
            recoverSession();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeProfileLayout(ProfileWithReviews profileWithReviews, String token) {
        ProfileModel profile = profileWithReviews.getProfile();
        List<ReviewModel> reviews = profileWithReviews.getReviews();
        if (profile != null && token != null) {
            swipeRefreshLayout.setRefreshing(false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_LOGGED_ID_SP, profile.get_id());
            editor.putString(Constants.USER_LOGGED_SP, profile.getTokenId());
            editor.putString(Constants.USER_LOGGED_TYPE_SP, profile.getType());
            editor.putString(Constants.USER_LOGGED_API_TOKEN_SP, token);
            editor.apply();
            bindProfile(profile);

            if (reviews !=  null && reviews.size() > 0) {
                reviewAdapter.updateData(reviews);
                statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
            } else {
                statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
            }
        }
    }


    public void recoverSession() {
        if (sharedPreferences.contains(Constants.USER_LOGGED_API_TOKEN_SP) &&
                sharedPreferences.contains(Constants.USER_LOGGED_SP)) {
            if (ConnectivityUtils.isConnected(getContext())) {
                fetchProfileFromApi(sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null));
            } else {
                getProfileFromDB();
            }
        }
    }

    private void fetchProfileFromApi(final String apiToken) {
        profileViewModel.getLoggedProfile(apiToken).observe(this, new Observer<ProfileWrapper>() {
            @Override
            public void onChanged(@Nullable ProfileWrapper profileWrapper) {
                if (profileWrapper != null) {
                    if (profileWrapper.getProfile() != null) {
                        ProfileWithReviews profileWR = ProfileModelMapper
                                .transform(profileWrapper.getProfile());
                        initializeProfileLayout(profileWR,
                                apiToken);
                        saveProfileToDB(profileWR);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        profileDBRepository.onStop();
        reviewDBRepository.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
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

    private void photoIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
        if (requestCode == 765 && grantResults[0] == PERMISSION_GRANTED && grantResults[1] == PERMISSION_GRANTED) {
            photoIntent();
        }
    }

    private void uploadImage() {
        dialog = new ProgressDialog(getContext());
        if (filePath != null && filePath.length() > 1) {
            String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
            String id = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
            File file = ApiUtils.resizeImage(id, new File(filePath), 50, 50);
            Glide.with(getContext())
                    .load(file)
                    .centerCrop()
                    .crossFade()
                    .into(profileImage);
            MultipartBody.Part image = MultipartBody.Part .createFormData("photo", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
            dialog.setMessage(getString(R.string.uploading_image));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
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
                            Glide.with(getContext())
                                    .load(Constants.API_UPLOAD_URL + uploadPhotoWrapper.getPhotoUrl())
                                    .centerCrop()
                                    .crossFade()
                                    .into(profileImage);
                            updateProfile(uploadPhotoWrapper.getPhotoUrl());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
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
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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
                        dialog.hide();
                    }
                });
    }

    private void bindProfile(ProfileModel profile) {
        profileName.setText(profile.getName());
        profileEmail.setText(profile.getEmail());
        if (profile.getPhotoUrl() != null) {
            Glide.with(getContext())
                    .load(Constants.API_UPLOAD_URL + profile.getPhotoUrl())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.mipmap.profile)
                    .into(profileImage);
        }
        int progress = calculateXP_progress(profile.getExperience());
        XP_Progress.setProgress(progress);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoIntent();
            }
        });
    }

    public int calculateXP_progress(int experience) {
        float progress;
        reviewNumber.setText(String.format(getString(R.string.xp_count), experience));
        if (experience < Constants.XP_FIRST_LEVEL_TOP) {
            float rat = (Float.valueOf(experience) / Float.valueOf(Constants.XP_FIRST_LEVEL_TOP));
            reviewNumber2.setText(String.format("/ " + getString(R.string.xp_count), Constants.XP_FIRST_LEVEL_TOP));
            reviewTitleText.setText(R.string.visitor);
            progress = 100 * rat;
        } else if (experience < Constants.XP_SECOND_LEVEL_TOP) {
            reviewNumber2.setText(String.format("/ " + getString(R.string.xp_count), Constants.XP_SECOND_LEVEL_TOP));
            reviewTitleText.setText(R.string.traveler);
            progress = 100 * (Float.valueOf(experience)/ Float.valueOf(Constants.XP_SECOND_LEVEL_TOP));
        } else {
            reviewNumber2.setText(String.format("/ " + getString(R.string.xp_count), Constants.XP_THIRD_LEVEL_TOP));
            reviewTitleText.setText(R.string.scout);
            progress = 100 * (Float.valueOf(experience) / Float.valueOf(Constants.XP_THIRD_LEVEL_TOP));
        }
        return (int) progress;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (profileViewModel == null) {
            profileViewModel = ViewModelProviders.of(this, profileVMFactory)
                    .get(ProfileViewModel.class);
        }
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
            if (profileViewModel == null) {
                profileViewModel = ViewModelProviders.of(this, profileVMFactory)
                        .get(ProfileViewModel.class);
            }
            profileViewModel.signInOrRegister(profile).observe(this, new Observer<ProfileWrapper>() {
                @Override
                public void onChanged(@Nullable ProfileWrapper profileWrapper) {
                    if (profileWrapper != null) {
                        if (profileWrapper.getProfile() != null && profileWrapper.getStatus().equals("OK")) {
                            ProfileWithReviews profileWR = ProfileModelMapper.
                                    transform(profileWrapper.getProfile());
                            initializeProfileLayout(profileWR,
                                    profileWrapper.getToken());
                            saveProfileToDB(profileWR);
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

    private void getProfileFromDB() {
        final String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
        String userId = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
        profileDBRepository.getProfile(userId).observe(this, new Observer<ProfileWithReviews>() {
            @Override
            public void onChanged(@Nullable ProfileWithReviews profileWithReviews) {
                if (apiToken != null && profileWithReviews != null && profileWithReviews.getProfile() != null) {
                    // saved in DB
                    initializeProfileLayout(profileWithReviews, apiToken);
                } else {
                    // fetch from API
                    fetchProfileFromApi(apiToken);
                }
            }
        });
    }

    private void saveProfileToDB(ProfileWithReviews profileWR) {
        profileDBRepository.insertProfile(profileWR.getProfile());
        List<ReviewModel> reviewModels = profileWR.getReviews();
        if (reviewModels != null) {
            reviewDBRepository.insertReview(profileWR.getReviews());
        }
    }

    private void logoutFromDB() {
        profileDBRepository.logoutProfile();
    }

    private void deleteReviewFromDB(ReviewModel review) {
        reviewDBRepository.deleteReview(review);
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        logoutFromDB();
        editor.remove(Constants.USER_LOGGED_SP);
        editor.remove(Constants.USER_LOGGED_ID_SP);
        editor.remove(Constants.USER_LOGGED_TYPE_SP);
        editor.remove(Constants.USER_LOGGED_API_TOKEN_SP);
        editor.apply();
    }


    @Override
    public void onReviewMenuItemClick(MenuItem item, ReviewModel review) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteReview(review);
                break;
        }
    }

    private void deleteReview(ReviewModel review) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.please_wait));

        final String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
        deleteReviewFromDB(review);
        Review dummyReview = new Review();
        dummyReview.set_id(review.get_id());
        disposable =  profileRepository.removeReview(dummyReview, apiToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                        dialog.hide();
                        dialog.cancel();
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        profileViewModel.loadLoggedProfile(apiToken);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        dialog.hide();
                        dialog.cancel();
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void shareProfile() {
        String id = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
        profileDBRepository.getProfile(id)
                .observe(this, new Observer<ProfileWithReviews>() {
                    @Override
                    public void onChanged(@Nullable ProfileWithReviews profileWithReviews) {
                        if (profileWithReviews != null && profileWithReviews.getProfile() != null) {
                            ProfileModel profile = profileWithReviews.getProfile();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            String title = String.format(getString(R.string.find_me_at_app), getString(R.string.app_name));
                            String text = title + ": " + profile.getName()  + " -> "
                                    + "http://" + getString(R.string.app_name) + ".com/profile/" + profile.get_id();
                            intent.putExtra(Intent.EXTRA_SUBJECT, title);
                            intent.putExtra(Intent.EXTRA_TEXT, text);
                            Intent chooser = Intent.createChooser(intent, getContext().getString(R.string.share_profile));
                            startActivity(chooser);

                        }
                    }
                });
    }
}
