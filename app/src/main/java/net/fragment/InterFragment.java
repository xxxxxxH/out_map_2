package net.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import net.adapter.PlaceAdapter;
import net.basicmodel.InterActiveActivity;
import net.basicmodel.R;
import net.entity.DataEntity;
import net.http.NetUtils;
import net.http.RequestService;
import net.http.RetrofitUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InterFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    Retrofit retrofit;
    ArrayList<DataEntity> data = null;
    PlaceAdapter placeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return
                inflater.inflate(R.layout.layout_fragment_interactive, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initData() {
        retrofit = RetrofitUtils.Companion.get().retrofit();
        retrofit.create(RequestService.class).getData().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    data = NetUtils.get(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void initView() {
        placeAdapter = new PlaceAdapter(getActivity(), "big", data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(placeAdapter);
        placeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                if (placeAdapter.getType().equals("big")) {
                    DataEntity entity = (DataEntity) adapter.getData().get(position);
                    Intent i = new Intent(getActivity(), InterActiveActivity.class);
                    i.putExtra("data", entity);
                    getActivity().startActivity(i);
                }
            }
        });
    }
}
