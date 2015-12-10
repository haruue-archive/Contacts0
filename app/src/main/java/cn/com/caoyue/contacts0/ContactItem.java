package cn.com.caoyue.contacts0;

public class ContactItem {

    private int id;
    private String name;
    private String number;

    public ContactItem(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
