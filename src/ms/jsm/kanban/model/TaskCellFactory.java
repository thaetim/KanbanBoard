package ms.jsm.kanban.model;

import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import javafx.util.Callback;
import javafx.scene.control.ListView;

import java.time.LocalDate;

public class TaskCellFactory implements Callback<ListView<Task>, ListCell<Task>> {
    @Override
    public ListCell<Task> call(ListView<Task> listview){
        TaskCell cell = new TaskCell();

        cell.setOnDragDetected((MouseEvent event) -> {
            Task task = cell.getItem();
            if(task != null){
                System.out.print("Drag detected");
                Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();

                content.putString(task.getDetails());
                System.out.println(": " + task.getTitle());

                db.setContent(content);
                listview.getItems().remove(listview.getSelectionModel().getSelectedIndex());
                event.consume();
            }
        });

        cell.setOnDragEntered((DragEvent event) -> {
            cell.setStyle("-fx-background-color: lightblue;");
        });

        cell.setOnDragExited((DragEvent event) -> {
            cell.setStyle("");
        });

        cell.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if ( db.hasString() )
            {
                event.acceptTransferModes( TransferMode.COPY_OR_MOVE );
            }
            event.consume();
        });

        cell.setOnDragDropped((DragEvent event) -> {
            System.out.print( "Drag dropped" );
            Dragboard db = event.getDragboard();
            boolean success = false;
            if ( db.hasString() )
            {
                //extracting task data from string:
                String data = db.getString();

                //get title
                String title = data.substring(0,data.indexOf('\n'));
                data = data.substring(data.indexOf('\n')+1);

                //get priority
                int priority = Integer.parseInt(data.substring(0,data.indexOf('\n')));
                data = data.substring(data.indexOf('\n')+1);

                //get expiration date
                LocalDate expiration = LocalDate.parse(data.substring(0,data.indexOf('\n')));
                data = data.substring(data.indexOf('\n')+1);

                // (the description is the rest of the string)

                if(data.contains("null")){
                    listview.getItems().add(new Task(title,priority,expiration));
                }else{
                    listview.getItems().add(new Task(title,priority,expiration,data));
                }

                System.out.println(": " + title);
                success = true;
            }

            event.setDropCompleted( success );
            event.consume();
        });

        return cell;
    }
}
