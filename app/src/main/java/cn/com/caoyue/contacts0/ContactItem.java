package cn.com.caoyue.contacts0;

/**
 * Created by 冰月 on 2015/12/9.
 */
public class ContactItem {

    private String name;
    private String number;

    public ContactItem(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
