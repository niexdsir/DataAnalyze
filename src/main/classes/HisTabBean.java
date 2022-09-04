public class HisTabBean {
    private int id;
    private String tabname;
    private String crtime;
    private int user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTabname() {
        return tabname;
    }

    public void setTabname(String tabname) {
        this.tabname = tabname;
    }

    public String getCrtime() {
        return crtime;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public HisTabBean(int id, String tabname, String crtime, int user) {
        this.id = id;
        this.tabname = tabname;
        this.crtime = crtime;
        this.user = user;
    }
}
