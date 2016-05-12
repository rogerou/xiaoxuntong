package com.roger.xxt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roger.xxt.R;
import com.roger.xxt.activity.CommitCommentActivity;
import com.roger.xxt.data.bean.Comment;
import com.roger.xxt.data.bean.Information;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by YX201603-6 on 2016/5/12.
 */
public class HeaderViewCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    List<Comment> mList;
    Information mInformation;
    Context mContext;

    public HeaderViewCommentAdapter(List<Comment> list, Information information, Context context) {
        this.mContext = context;
        this.mList = list;
        this.mInformation = information;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_detail, parent, false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            Comment comment = getItem(position);
            ((CommentViewHolder) holder).tvComment.setText(comment.getComment());
            ((CommentViewHolder) holder).tvUsername.setText(comment.getUsername());
            ((CommentViewHolder) holder).tvTimestamp.setText(getTime(comment.getCreatedAt()));

        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).tvContent.setText(mInformation.getContent());
            ((HeaderViewHolder) holder).tvTitle.setText(mInformation.getTilte());
            ((HeaderViewHolder) holder).tvTimestamp.setText(getTime(mInformation.getCreatedAt()));
            ((HeaderViewHolder) holder).tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommitCommentActivity.class);
                    intent.putExtra("info", mInformation);
                    mContext.startActivity(intent);
                }
            });
        }


    }

    private Comment getItem(int position) {
        return mList.get(position - 1);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_timestamp)
        TextView tvTimestamp;
        @Bind(R.id.tv_edit)
        TextView tvEdit;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_username)
        TextView tvUsername;
        @Bind(R.id.tv_comment)
        TextView tvComment;
        @Bind(R.id.tv_timestamp)
        TextView tvTimestamp;


        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getTime(Date date) {
        if (date == null) {
            return "";
        }
        long timeNow = System.currentTimeMillis();
        long timeCreate = date.getTime();
        int minutes = (int) ((timeNow - timeCreate) / 1000 / 60);

        if (minutes == 0) {
            return "刚刚";
        } else if (minutes == 1) {
            return "1分钟前";
        } else if (minutes == 2) {
            return "2分钟前";
        } else if (minutes == 3) {
            return "3分钟前";
        } else if (minutes == 4) {
            return "4分钟前";
        } else if (minutes == 5) {
            return "5分钟前";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
            return simpleDateFormat.format(date);
        }

    }
}
