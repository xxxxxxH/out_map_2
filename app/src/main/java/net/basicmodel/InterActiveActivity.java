package net.basicmodel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import net.adapter.PlaceAdapter;
import net.entity.DataEntity;
import net.http.NetUtils;
import net.http.RequestService;
import net.http.RetrofitUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Copyright (C) 2021,2021/9/10, a Tencent company. All rights reserved.
 * <p>
 * User : v_xhangxie
 * <p>
 * Desc :
 */
public class InterActiveActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    Retrofit retrofit;
    ArrayList<DataEntity> data = null;
    PlaceAdapter placeAdapter;
    private StreetViewPanorama mStreetViewPanorama;
    Timer timer = null;
    DataEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_inter);
        ButterKnife.bind(this);
        initData();
//        initView();
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        SupportStreetViewPanoramaFragment fragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        fragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(@NonNull @NotNull StreetViewPanorama streetViewPanorama) {
                mStreetViewPanorama = streetViewPanorama;
                mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                    @Override
                    public void onStreetViewPanoramaChange(@NonNull @NotNull StreetViewPanoramaLocation streetViewPanoramaLocation) {

                    }
                });
                if (savedInstanceState == null) {
                    mStreetViewPanorama.setPosition(entity.getPanoid());
                }
                routeMap();
                mStreetViewPanorama.setOnStreetViewPanoramaClickListener(new StreetViewPanorama.OnStreetViewPanoramaClickListener() {
                    @Override
                    public void onStreetViewPanoramaClick(@NonNull @NotNull StreetViewPanoramaOrientation streetViewPanoramaOrientation) {
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                });
            }
        });
    }

    private void routeMap() {
        if (timer != null) {
            timer.cancel();
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StreetViewPanoramaCamera camera = StreetViewPanoramaCamera.builder()
                                .zoom(mStreetViewPanorama.getPanoramaCamera().zoom)
                                .tilt(mStreetViewPanorama.getPanoramaCamera().tilt)
                                .bearing(mStreetViewPanorama.getPanoramaCamera().bearing + 60)
                                .build();
                        mStreetViewPanorama.animateTo(camera, 5000);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 5000);
    }

    private void initData() {
        Intent intent = getIntent();
        entity = (DataEntity) intent.getSerializableExtra("data");
        retrofit = RetrofitUtils.Companion.get().retrofit();
        retrofit.create(RequestService.class).getSmallData(entity.getKey()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("xxxxxxH", "response = " + response);
                try {
                    String result = response.body().string();
                    data = NetUtils.getSmall(entity, result);
                    initView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("xxxxxxH", "onFailure = " + t.toString());
            }
        });
    }

    private void initView() {
        placeAdapter = new PlaceAdapter(this, "small", data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(placeAdapter);
        placeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                if (TextUtils.equals(placeAdapter.getType(), "small")) {
                    DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
                    if (mStreetViewPanorama == null) {
                        Toast.makeText(InterActiveActivity.this, "Street view init failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (entity.getFife()) {
                        mStreetViewPanorama.setPosition("F:" + dataEntity.getPannoId());
                    } else {
                        mStreetViewPanorama.setPosition(dataEntity.getPannoId());
                    }
                }
            }
        });
    }
}
