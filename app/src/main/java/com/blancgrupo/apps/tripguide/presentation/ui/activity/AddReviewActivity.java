package com.blancgrupo.apps.tripguide.presentation.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blancgrupo.apps.tripguide.MyApplication;
import com.blancgrupo.apps.tripguide.R;
import com.blancgrupo.apps.tripguide.data.entity.api.Review;
import com.blancgrupo.apps.tripguide.data.entity.api.ReviewResponseWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.UploadPhotoWrapper;
import com.blancgrupo.apps.tripguide.data.persistence.repository.PlaceDBRepository;
import com.blancgrupo.apps.tripguide.data.persistence.repository.ReviewDBRepository;
import com.blancgrupo.apps.tripguide.domain.model.ReviewModel;
import com.blancgrupo.apps.tripguide.domain.model.mapper.PlaceModelMapper;
import com.blancgrupo.apps.tripguide.domain.repository.ProfileRepository;
import com.blancgrupo.apps.tripguide.presentation.di.component.DaggerActivityComponent;
import com.blancgrupo.apps.tripguide.presentation.di.module.ActivityModule;
import com.blancgrupo.apps.tripguide.utils.ApiUtils;
import com.blancgrupo.apps.tripguide.utils.Constants;
import com.bumptech.glide.Glide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AddReviewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rating_bar)
    SimpleRatingBar ratingBar;
    @BindView(R.id.review_message)
    TextInputEditText reviewMessageEditText;
    @BindView(R.id.btn)
    Button sendReviewBtn;
    @BindView(R.id.addphoto_btn)
    Button addPhotoBtn;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.review_place_name)
    TextView placeName;
    @BindView(R.id.close)
            ImageView closeImage;
    Disposable disposable;
    String placeId;
    String filePath;
    ProgressDialog dialog;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    ProfileRepository profileRepository;

    @Inject
    ReviewDBRepository reviewDBRepository;
    @Inject
    PlaceDBRepository placeDBRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_review);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .netComponent(((MyApplication) getApplication()).getNetComponent())
                .build()
                .inject(this);

        Bundle data = getIntent().getExtras();
        float rating = data.getFloat(Constants.EXTRA_PROGRESS);
        String place = data.getString(Constants.EXTRA_PLACE_NAME);
        ratingBar.setRating(rating);
        placeId = data.getString(Constants.EXTRA_PLACE_ID);
        placeName.setText(place);

        sendReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoIntent();
            }
        });


    }

    private void photoIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 765 && grantResults[0] == PERMISSION_GRANTED) {
            photoIntent();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9898 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                try {
                    String wholeId = DocumentsContract.getDocumentId(uri);
                    String id = wholeId.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = getContentResolver().query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[] {id}, null
                    );
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndex(column[0]);
                        if (cursor.moveToFirst()) {
                            filePath = cursor.getString(columnIndex);
                        } else {
                            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
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
                        c = getContentResolver().query(
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
            Glide.with(this)
                    .load(file)
                    .centerCrop()
                    .crossFade()
                    .into(photo);
            photo.setVisibility(View.VISIBLE);
            addPhotoBtn.setVisibility(View.GONE);
            closeImage.setVisibility(View.VISIBLE);
            closeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filePath = null;
                    photo.setVisibility(View.GONE);
                    addPhotoBtn.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    private void uploadImage() {
        dialog = new ProgressDialog(this);
        if (filePath != null && filePath.length() > 1) {
            String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
            String id = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
            File file = ApiUtils.resizeImage(id, new File(filePath), 75, 100);
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
                            sendReview(uploadPhotoWrapper.getPhotoUrl());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Toast.makeText(AddReviewActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    });
        } else {
            sendReview(null);
        }
    }

    private void
    sendReview(String photoUrl) {
        dialog.cancel();
        String profileId = sharedPreferences.getString(Constants.USER_LOGGED_ID_SP, null);
        String apiToken = sharedPreferences.getString(Constants.USER_LOGGED_API_TOKEN_SP, null);
        if (profileId != null) {
            float rating = ratingBar.getRating();
            String message = reviewMessageEditText.getText().toString();
            if (message.length() > 0) {
                message = message.trim();
            }
            Review review = new Review();
            review.setRating(rating);
            review.setMessage(message);
            Review.ReviewProfile profile = new Review.ReviewProfile();
            profile.set_id(profileId);
            review.setProfile(profile);
            Review.ReviewPlace place = new Review.ReviewPlace();
            place.set_id(placeId);
            review.setPlace(place);
            review.setPhoto(photoUrl);
            // send review
            dialog.setMessage(getString(R.string.sending_review));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            disposable = profileRepository.addReview(review, apiToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ReviewResponseWrapper>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull ReviewResponseWrapper reviewResponseWrapper) throws Exception {
                            ReviewModel reviewModel = PlaceModelMapper.transformReview(reviewResponseWrapper.getReview());
                            saveReviewToDB(reviewModel);
                            updatePlace(placeId, true);
                            dialog.hide();
                            Toast.makeText(AddReviewActivity.this, R.string.thanks_for_your_review
                                    , Toast.LENGTH_SHORT).show();
                            Intent result = new Intent(Intent.ACTION_DEFAULT);
                            result.putExtra(Constants.EXTRA_PLACE_ID, placeId);
                            setResult(RESULT_OK, result);
                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            Toast.makeText(AddReviewActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.hide();
                        }
                    });

        } else {
            Toast.makeText(this, R.string.you_are_not_logged_in, Toast.LENGTH_SHORT).show();
        }
    }

    void saveReviewToDB(ReviewModel review) {
        reviewDBRepository.insertReview(review);
    }

    void updatePlace(String placeId, boolean isReviewed) {
        placeDBRepository.setReviewed(placeId, isReviewed);
    }

    @Override
    protected void onStop() {
        super.onStop();
        reviewDBRepository.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        if (reviewMessageEditText.getText().toString().length() > 0) {
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .content(R.string.are_you_sure)
                    .positiveText(R.string.discard)
                    .negativeText(R.string.dont_discard)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            AddReviewActivity.super.onBackPressed();
                        }
                    })
                    .build();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
