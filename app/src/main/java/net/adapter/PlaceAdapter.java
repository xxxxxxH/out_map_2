package net.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import net.basicmodel.R;
import net.entity.DataEntity;
import net.utils.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Copyright (C) 2021,2021/9/10, a Tencent company. All rights reserved.
 * <p>
 * User : v_xhangxie
 * <p>
 * Desc :
 */
public class PlaceAdapter extends BaseQuickAdapter<DataEntity, BaseViewHolder> {

    Activity activity;
    String type;

    public PlaceAdapter(Activity activity, String type, List<DataEntity> data) {
        super(R.layout.layout_item_interactive, data);
        this.activity = activity;
        this.type = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DataEntity dataEntity) {
        ImageView bg = baseViewHolder.getView(R.id.img);
        TextView title = baseViewHolder.getView(R.id.img_name);
        RelativeLayout root = baseViewHolder.getView(R.id.root);
        ViewGroup.LayoutParams p = root.getLayoutParams();
        if (type.equals("big")) {
            p.height = ScreenUtils.getScreenSize(activity)[0] / 3;
        } else {
            p.height = ScreenUtils.getScreenSize(activity)[0] / 4;
        }
        root.setLayoutParams(p);
        Glide.with(activity).load(dataEntity.getImageUrl()).into(bg);
        baseViewHolder.setText(R.id.img_name, dataEntity.getTitle());
    }

    public String getType() {
        return type;
    }
}
