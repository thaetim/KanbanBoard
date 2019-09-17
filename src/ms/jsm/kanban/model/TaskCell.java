package ms.jsm.kanban.model;

import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;

public class TaskCell extends ListCell<Task> {

    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);

        //int index = this.getIndex();
        String title = null;

        if (item == null || empty) {
        } else {
            //title = (index + 1) + ". " + item.getTitle();
            title = item.getTitle();
            String tt = this.getItem().getTitle() + '\n' +
                    this.getItem().getPriority() + '\n' +
                    this.getItem().getExpiration();
            String desc = this.getItem().getDescription();
            if(!(desc==null)){
                tt = tt + '\n' + desc;
            }
            setTooltip(new Tooltip(tt));
        }

        this.setText(title);
        setGraphic(null);
    }
}
