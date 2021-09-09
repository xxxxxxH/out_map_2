package net.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import net.basicmodel.R;
import net.entity.ResourceEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourceManager {
    private static ResourceManager instance = null;

    private ResourceManager() {
    }

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public String res2String(Context context, int id) {
        Resources r = context.getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(id) + "/"
                + r.getResourceTypeName(id) + "/"
                + r.getResourceEntryName(id)
        );
        return uri.toString();
    }

    public int getResource(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
    }

    public ArrayList<ResourceEntity> getAllResource(Context context) {
        ArrayList<ResourceEntity> result = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Class clazz = R.mipmap.class;
                for (Field field : clazz.getFields()) {
                    String name = field.getName();
                    if (name.startsWith("ic")){
                        continue;
                    }
                    int id = context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
                    ResourceEntity entity = new ResourceEntity();
                    entity.setName(name);
                    entity.setId(id);
                    result.add(entity);
                }
            }
        }).start();
        return result;
    }
}
