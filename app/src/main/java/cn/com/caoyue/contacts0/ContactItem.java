package cn.com.caoyue.contacts0;

public class ContactItem {

    private long id;
    private String name;
    private String number;

    public ContactItem(long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public void setId(long newId) {
        this.id = newId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
