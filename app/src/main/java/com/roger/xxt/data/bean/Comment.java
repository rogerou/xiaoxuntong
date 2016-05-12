package com.roger.xxt.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.roger.xxt.data.dao.CommentDao;
import com.roger.xxt.data.dao.InformationDao;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
@AVClassName("comment")
public class Comment extends AVObject implements Parcelable {

    private AVUser author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Comment comment1 = (Comment) o;

        if (author != null ? !author.equals(comment1.author) : comment1.author != null)
            return false;
        if (comment != null ? !comment.equals(comment1.comment) : comment1.comment != null)
            return false;
        return information != null ? information.equals(comment1.information) : comment1.information == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (information != null ? information.hashCode() : 0);
        return result;
    }

    public String getComment() {
        return getString(CommentDao.COMMENT);
    }

    public void setComment(String comment) {
        put(CommentDao.COMMENT, comment);
    }

    public Information getInformation() {
        return (Information) get(CommentDao.INFORMATION);
    }

    public void setInformation(Information information) {
        put(CommentDao.INFORMATION, information);
    }

    public AVUser getAuthor() {
        return (AVUser) get(InformationDao.AUTHOR);
    }

    public void setAuthor(AVUser author) {
        put(InformationDao.AUTHOR, author);
    }

    private String comment;
    private Information information;

    public String getUsername() {
        return getString(CommentDao.COMMENTUSERNAME);
    }

    public void setUsername(String username) {
        put(CommentDao.COMMENTUSERNAME, username);
    }

    private String username;

    public Comment() {
        super();
    }


    protected Comment(Parcel in) {
        super(in);

    }

    public static final Parcelable.Creator<Comment> CREATOR = AVObjectCreator.instance;

}
