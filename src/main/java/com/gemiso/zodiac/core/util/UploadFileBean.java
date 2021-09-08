package com.gemiso.zodiac.core.util;


import java.io.Serializable;

public class UploadFileBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4906692661191465934L;

    private String divcd;
    private String up_yn;
    private String dest;
    private String updir;
    private String maxsize;
    private String dirtype;
    private String file_id;
    private String file_div_cd;
    private String rname_yn;
    private String srcpath;
    private String notallowext;

    public String getDivcd()
    {
        return this.divcd;
    }
    public void setDivcd(String divcd) {
        this.divcd = divcd;
    }
    public String getUp_yn() {
        return this.up_yn;
    }
    public void setUp_yn(String up_yn) {
        this.up_yn = up_yn;
    }
    public String getDest() {
        return this.dest;
    }
    public void setDest(String dest) {
        this.dest = dest;
    }
    public String getUpdir() {
        return this.updir;
    }
    public void setUpdir(String updir) {
        this.updir = updir;
    }
    public String getMaxsize() {
        return this.maxsize;
    }
    public void setMaxsize(String maxsize) {
        this.maxsize = maxsize;
    }
    public String getDirtype() {
        return this.dirtype;
    }
    public void setDirtype(String dirtype) {
        this.dirtype = dirtype;
    }
    public String getFile_id() {
        return this.file_id;
    }
    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }
    public String getFile_div_cd() {
        return this.file_div_cd;
    }
    public void setFile_div_cd(String file_div_cd) {
        this.file_div_cd = file_div_cd;
    }
    public String getRname_yn() {
        return this.rname_yn;
    }
    public void setRname_yn(String rname_yn) {
        this.rname_yn = rname_yn;
    }
    public String getSrcpath() {
        return this.srcpath;
    }
    public void setSrcpath(String srcpath) {
        this.srcpath = srcpath;
    }

    public String getNotallowext()
    {
        return this.notallowext;
    }

    public void setNotallowext(String notallowext)
    {
        this.notallowext = notallowext;
    }
}