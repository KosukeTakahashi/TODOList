package jp.kosuke.todolist;

import net.cattaka.util.cathandsgendroid.annotation.DataModel;
import net.cattaka.util.cathandsgendroid.annotation.DataModelAttrs;

/**
 * Created by 孝輔 on 2016/04/23.
 */
@DataModel(find = {
        "id", "title", "color"
}, unique = {
        "title"
})

public class TODOMdl {

    @DataModelAttrs(primaryKey = true)
    private long id;
    private String title = "Title";
    private String subsc = "Subscription";
    private int color = 0x77FFFFFF;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubsc() {
        return subsc;
    }

    public void setSubsc(String subsc) {
        this.subsc = subsc;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

