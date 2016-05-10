package com.roger.xxt.data.bean;

import android.annotation.SuppressLint;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.roger.xxt.data.dao.InformationDao;

@SuppressLint("ParcelCreator")
@AVClassName("information")
public class Information extends AVObject {

    private String tilte;
    private String content;

    public AVUser getAuthor() {
        return (AVUser) get(InformationDao.AUTHOR);
    }

    public void setAuthor(AVUser author) {
        put(InformationDao.AUTHOR, author);
    }

    public String getTilte() {
        return getString(InformationDao.TITLE);
    }

    public void setTilte(String tilte) {
        put(InformationDao.TITLE, tilte);
    }

    public String getContent() {
        return getString(InformationDao.CONTENT);
    }

    public void setContent(String content) {
        put(InformationDao.CONTENT, content);
    }

    private AVUser author;


    public Information() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Information that = (Information) o;

        if (tilte != null ? !tilte.equals(that.tilte) : that.tilte != null) return false;
        return content != null ? content.equals(that.content) : that.content == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (tilte != null ? tilte.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
