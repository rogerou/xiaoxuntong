package com.roger.xxt.event;

import com.roger.xxt.data.bean.Comment;

/**
 * Created by YX201603-6 on 2016/5/12.
 */
public class AddCommentEvent {
    public AddCommentEvent(Comment comment) {
        mComment = comment;
    }

    public Comment getComment() {
        return mComment;
    }

    public void setComment(Comment comment) {
        mComment = comment;
    }

    private Comment mComment;
}
