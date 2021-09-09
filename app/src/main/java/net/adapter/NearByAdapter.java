package net.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import net.basicmodel.R;
import net.entity.ResourceEntity;
import net.utils.ResourceManager;
import net.utils.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NearByAdapter extends BaseQuickAdapter<ResourceEntity, BaseViewHolder> {

    Context context;
    Activity activity;

    public NearByAdapter(Context context, Activity activity, List<ResourceEntity> data) {
        super(R.layout.layout_item_nearby, data);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ResourceEntity resourceEntity) {
        RelativeLayout root = baseViewHolder.getView(R.id.item_root);
        ViewGroup.LayoutParams params1 = root.getLayoutParams();
        params1.width = ScreenUtils.getScreenSize(activity)[1] / 3;
        root.setLayoutParams(params1);

        ImageView imageView = baseViewHolder.getView(R.id.item_bg);
        ViewGroup.LayoutParams params2 = imageView.getLayoutParams();
        params2.width = ScreenUtils.getScreenSize(activity)[1] / 5;
        params2.height = ScreenUtils.getScreenSize(activity)[1] / 5;
        imageView.setLayoutParams(params2);
        Glide.with(context).load(ResourceManager.getInstance().res2String(context, resourceEntity.getId())).into(imageView);

        baseViewHolder.setText(R.id.item_name, resourceEntity.getName());
    }
}
