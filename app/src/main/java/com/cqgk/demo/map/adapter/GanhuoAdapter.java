package com.cqgk.demo.map.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import com.cqgk.demo.map.R;
import com.cqgk.demo.map.model.GankResults;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by wanglei on 2016/12/10.
 */

public class GanhuoAdapter extends SimpleRecAdapter<GankResults.Item, GanhuoAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public GanhuoAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_ganhuo;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GankResults.Item item = data.get(position);
        holder.tvItem.setText(item.getDesc());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_item)
        TextView tvItem;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }

}
