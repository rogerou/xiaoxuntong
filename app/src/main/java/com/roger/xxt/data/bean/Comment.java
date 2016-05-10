package com.roger.xxt.data.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.roger.xxt.data.dao.CommentDao;
import com.roger.xxt.data.dao.InformationDao;

/**
 * Created by YX201603-6 on 2016/5/10.
 */
@AVClassName("comment")
public class Comment extends AVObject {

    private AVUser author;

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

    public Comment() {
        
    }
}
