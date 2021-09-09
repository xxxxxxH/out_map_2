package net.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import net.adapter.NearByAdapter;
import net.basicmodel.R;
import net.entity.ResourceEntity;
import net.utils.PackageUtils;
import net.utils.ResourceManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearByFragment extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    ArrayList<ResourceEntity> data;
    NearByAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_nearby, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initData() {
        data = ResourceManager.getInstance().getAllResource(getActivity());
    }

    private void initView() {
        adapter = new NearByAdapter(getActivity(), getActivity(), data);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter<?, ?> adapter, View view, int position) {
                if (!PackageUtils.Companion.get().checkAppInstalled(getActivity(), "com.google.android.apps.maps")) {
                    Toast.makeText(getActivity(), "not found google map", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResourceEntity entity = (ResourceEntity) adapter.getData().get(position);
                String url = "http://maps.google.com/maps?q=" + entity.getName() + "&hl=en";
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                i.setPackage("com.google.android.apps.maps");
                getActivity().startActivity(i);
            }
        });
    }
}
