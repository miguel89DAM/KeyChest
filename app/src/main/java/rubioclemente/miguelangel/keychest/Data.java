package rubioclemente.miguelangel.keychest;

public class Data {

    private int id;
    private String name,description,dataPassword;
    private User user;
    private Category category;


    public Data(){

    }

    public Data( String name, String description, String dataPassword, User user, Category category) {
        this.name = name;
        this.description = description;
        this.dataPassword = dataPassword;
        this.user = user;
        this.category = category;
    }

    public Data(int id, String name, String description, String dataPassword, User user, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dataPassword = dataPassword;
        this.user = user;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataPassword() {
        return dataPassword;
    }

    public void setDataPassword(String dataPassword) {
        this.dataPassword = dataPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
