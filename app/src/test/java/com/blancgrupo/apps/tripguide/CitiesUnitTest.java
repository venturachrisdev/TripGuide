package com.blancgrupo.apps.tripguide;

import com.blancgrupo.apps.tripguide.data.ApiCityRepository;
import com.blancgrupo.apps.tripguide.data.ApiKeyInterceptor;
import com.blancgrupo.apps.tripguide.data.entity.api.CitiesWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.City;
import com.blancgrupo.apps.tripguide.data.entity.api.CityWrapper;
import com.blancgrupo.apps.tripguide.data.entity.api.Topic;
import com.blancgrupo.apps.tripguide.data.service.ApiPlaceService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CitiesUnitTest {

    ApiCityRepository apiCityRepository;

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

        apiCityRepository = new ApiCityRepository(apiPlaceService);


    }

    @Test
    public void fetchingCitiesFromApi() throws Exception {
        apiCityRepository.getCities().subscribe(new Observer<CitiesWrapper>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull CitiesWrapper cities) {
                assertNotEquals(cities.getCities().size(), 0);
                for (City city : cities.getCities()) {
                    assertNotNull(city);
                    System.out.println(city.getName() + " -> " + city.getId());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Test
    public void fetchingSingleCityFromApi() throws Exception {
        apiCityRepository.getSingleCity("5995afae263c0c40c7afb8dc")
            .subscribe(new Observer<CityWrapper>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull CityWrapper cityWrapper) {
                    City city = cityWrapper.getCity();
                    assertNotNull(city.getTopics());
                    assertNotNull(city.getLocation());
                    System.out.println(city.getName());
                    for (Topic topic : city.getTopics()) {
                        System.out.println(topic.get_id() + " -> " + topic.getPlaces().size());
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

    }
}