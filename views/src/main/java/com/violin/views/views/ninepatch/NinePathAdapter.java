package com.violin.views.views.ninepatch;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NinePathAdapter extends RecyclerView.Adapter<NinePathAdapter.Holder> {

    public ArrayList<NinePathBean> list = new ArrayList<>();

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(new NinePathItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bindData(list.get(position));

    }

    @Override
    public void onViewRecycled(@NonNull Holder holder) {
        holder.onViewRecycled();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }

        public void bindData(NinePathBean bean) {
            if (itemView instanceof NinePathItemView) {
                ((NinePathItemView) itemView).setData(bean);
            }

        }
        public void onViewRecycled() {
            if (itemView instanceof NinePathItemView) {
                ((NinePathItemView) itemView).onViewRecycled();
            }
        }

    }

    public static class NinePathBean {
        private String url;
        private String text;

        public NinePathBean(String url, String text) {
            this.url = url;
            this.text = text;

        }

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }
}
