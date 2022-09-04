class people {
    String id;
    String username;
    String sex;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public people(String id, String name, String sex) {
        this.id = id;
        this.username = name;
        this.sex = sex;
    }
}
