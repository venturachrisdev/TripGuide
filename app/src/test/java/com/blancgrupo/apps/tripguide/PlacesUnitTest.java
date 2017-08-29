package com.blancgrupo.apps.tripguide;

import com.blancgrupo.apps.tripguide.data.ApiCityRepository;
import com.blancgrupo.apps.tripguide.data.ApiPlaceRepository;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.PlacesWrapper;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 8/29/17.
 */

public class PlacesUnitTest {
    ApiPlaceRepository apiPlaceRepository;

    @Before
    public void setupRetrofit() throws Exception {

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(
                        FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .baseUrl("http://138.197.43.190/api/v1/")
                .build();

        ApiPlaceService apiPlaceService = retrofit.create(ApiPlaceService.class);

        apiPlaceRepository = new ApiPlaceRepository(apiPlaceService);


    }
    @Test
    public void fetchPlacesFromApi() throws Exception {
        apiPlaceRepository.getTours()
                .subscribe(new Observer<PlacesWrapper>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull PlacesWrapper placesWrapper) {
                        City city = placesWrapper.getPlaces().get(0).getCity();
                        System.out.println(city.getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
