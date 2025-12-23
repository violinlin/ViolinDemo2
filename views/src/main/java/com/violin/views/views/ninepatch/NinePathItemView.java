package com.violin.views.views.ninepatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.ByteBufferWebpDecoder;
import com.bumptech.glide.integration.webp.ninepath.NinePatchUtil;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.violin.views.R;

public class NinePathItemView extends FrameLayout {
    private ImageView iv;
    private TextView tv;

    public NinePathItemView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.view_ninepath_item_view, this);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        iv = findViewById(R.id.iv_img);
        tv = findViewById(R.id.tv_text);
    }

    public void setData(NinePathAdapter.NinePathBean bean) {
        tv.setText(bean.getText());
        Glide.with(iv.getContext())
                .load(bean.getUrl())
                .set(ByteBufferWebpDecoder.USE_NINE_PATH_DRAWABLE, true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        Log.d("NinePathWebDrawable", "onLoadFailed:${}" + e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        Log.d("NinePathWebDrawable", "onResourceReady:${}" + dataSource.name());
                        tv.setTextColor(Color.WHITE);
                        if (resource instanceof BitmapDrawable) {
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            iv.setImageDrawable(new NinePatchDrawable(iv.getResources(), bitmap,
                                    NinePatchUtil.getNinePatchChunk(bitmap), new Rect(), null));
                            return true;
                        }
                        return false;
                    }
                })
                .into(iv);
    }

    public void onViewRecycled() {
        Glide.with(iv.getContext()).clear(iv);
        tv.setTextColor(Color.BLACK);
        Log.d("NinePathWebDrawable", "onViewRecycled:${}" + tv.getText());
    }
}
