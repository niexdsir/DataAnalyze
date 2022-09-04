public class SqlTableBean {
    private String ywname;
    private String zwname;
    private String danwei;
    private String zdtype;
    private String beizhu;
    private String ispri;
    private String crtime;
    private String chtime;
    private String user;

    public String getYwname() {
        return ywname;
    }

    public void setYwname(String ywname) {
        this.ywname = ywname;
    }

    public String getZwname() {
        return zwname;
    }

    public void setZwname(String zwname) {
        this.zwname = zwname;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getZdtype() {
        return zdtype;
    }

    public void setZdtype(String zdtype) {
        this.zdtype = zdtype;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getIspri() {
        return ispri;
    }

    public void setIspri(String ispri) {
        this.ispri = ispri;
    }

    public String getCrtime() {
        return crtime;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public String getChtime() {
        return chtime;
    }

    public void setChtime(String chtime) {
        this.chtime = chtime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public SqlTableBean(String ywname, String zwname, String danwei, String zdtype, String beizhu, String ispri, String crtime, String chtime, String user) {
        this.ywname = ywname;
        this.zwname = zwname;
        this.danwei = danwei;
        this.zdtype = zdtype;
        this.beizhu = beizhu;
        this.ispri = ispri;
        this.crtime = crtime;
        this.chtime = chtime;
        this.user = user;
    }
}
