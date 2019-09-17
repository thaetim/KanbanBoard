package ms.jsm.kanban.model;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.scene.control.ListView;

public class TaskViewSkin<T> extends ListViewSkin<T> {

    public TaskViewSkin(ListView<T> arg0){
        super(arg0);
    }

    public void refresh() {
        super.flow.recreateCells();
    }
}
