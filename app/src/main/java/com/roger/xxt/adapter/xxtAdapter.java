package com.roger.xxt.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roger.xxt.R;
import com.roger.xxt.data.bean.Information;
import com.roger.xxt.uti.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
public class xxtAdapter extends RecyclerView.Adapter<xxtAdapter.ViewHolder> {

    List<Information> mList;

    OnItemClickListener mlistener;

    public xxtAdapter(List<Information> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xxt, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvContent.setText(mList.get(position).getContent());
        holder.tvTitle.setText(mList.get(position).getTilte());
        holder.tvTimestamp.setText(getTime(mList.get(position).getCreatedAt()));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener mlistener) {
        this.mlistener = mlistener;

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_timestamp)
        TextView tvTimestamp;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(v, getAdapterPosition());
                }
            });
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(date);
    }
}
