package ms.jsm.kanban.view;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import ms.jsm.kanban.MainApp;
import ms.jsm.kanban.model.Task;
import ms.jsm.kanban.model.TaskCell;
import ms.jsm.kanban.model.TaskCellFactory;
import ms.jsm.kanban.model.TaskViewSkin;

public class MainBoardController {
    @FXML
    private ListView<Task> taskToDoList;
    @FXML
    private ListView<Task> taskOpenList;
    @FXML
    private ListView<Task> taskDoneList;

    private MainApp mainApp;
    private final KeyCombination ctrl_r = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_DOWN);
    private final KeyCombination ctrl_l = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_DOWN);

    public MainBoardController(){    }

    @FXML
    private void initialize(){
        taskToDoList.setCellFactory(new TaskCellFactory());
        taskOpenList.setCellFactory(new TaskCellFactory());
        taskDoneList.setCellFactory(new TaskCellFactory());

        taskToDoList.setSkin(new TaskViewSkin<>(taskToDoList));
        taskOpenList.setSkin(new TaskViewSkin<>(taskOpenList));
        taskDoneList.setSkin(new TaskViewSkin<>(taskDoneList));

        taskToDoList.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Task currentItem = taskToDoList.getSelectionModel().getSelectedItem();
                editTask(currentItem);
                ((TaskViewSkin) taskToDoList.getSkin()).refresh();
            }
        });
        taskOpenList.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                Task currentItem = taskOpenList.getSelectionModel().getSelectedItem();
                editTask(currentItem);
                ((TaskViewSkin) taskOpenList.getSkin()).refresh();
            }
        });
        taskDoneList.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                    Task currentItem = taskDoneList.getSelectionModel().getSelectedItem();
                    editTask(currentItem);
                    ((TaskViewSkin) taskDoneList.getSkin()).refresh();
            }
        });

        taskToDoList.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                Task currentItem = taskToDoList.getSelectionModel().getSelectedItem();
                editTask(currentItem);
                ((TaskViewSkin) taskToDoList.getSkin()).refresh();
            }else if(ctrl_r.match(event)){
                int i = taskToDoList.getSelectionModel().getSelectedIndex();
                taskOpenList.getItems().add(taskToDoList.getItems().get(i));
                taskToDoList.getItems().remove(i);
                //taskOpenList.getSelectionModel().select(i);
            }else if(event.getCode() == KeyCode.RIGHT){
                taskOpenList.getSelectionModel().select(taskToDoList.getSelectionModel().getSelectedIndex());
            }
        });
        taskOpenList.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                Task currentItem = taskOpenList.getSelectionModel().getSelectedItem();
                editTask(currentItem);
                ((TaskViewSkin) taskOpenList.getSkin()).refresh();
            }else if(ctrl_r.match(event)){
                int i = taskOpenList.getSelectionModel().getSelectedIndex();
                taskDoneList.getItems().add(taskOpenList.getItems().get(i));
                taskOpenList.getItems().remove(i);
                //taskDoneList.getSelectionModel().select(i);
            }else if(ctrl_l.match(event)){
                int i = taskOpenList.getSelectionModel().getSelectedIndex();
                taskToDoList.getItems().add(taskOpenList.getItems().get(i));
                taskOpenList.getItems().remove(i);
                //taskToDoList.getSelectionModel().select(i);
            }else if(event.getCode() == KeyCode.RIGHT) {
                taskDoneList.getSelectionModel().select(taskOpenList.getSelectionModel().getSelectedIndex());
            }else if(event.getCode() == KeyCode.LEFT){
                taskToDoList.getSelectionModel().select(taskOpenList.getSelectionModel().getSelectedIndex());
            }
        });
        taskDoneList.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                Task currentItem = taskDoneList.getSelectionModel().getSelectedItem();
                editTask(currentItem);
                ((TaskViewSkin) taskDoneList.getSkin()).refresh();
            }else if(ctrl_l.match(event)){
                int i = taskDoneList.getSelectionModel().getSelectedIndex();
                taskOpenList.getItems().add(taskDoneList.getItems().get(i));
                taskDoneList.getItems().remove(i);
                //taskOpenList.getSelectionModel().select(i);
            }else if(event.getCode() == KeyCode.LEFT){
                taskOpenList.getSelectionModel().select(taskDoneList.getSelectionModel().getSelectedIndex());
            }
        });

        taskToDoList.setContextMenu(new TaskContextMenu(taskToDoList));
        taskOpenList.setContextMenu(new TaskContextMenu(taskOpenList));
        taskDoneList.setContextMenu(new TaskContextMenu(taskDoneList));

        taskToDoList.setOnDragEntered((DragEvent event) -> {
            if(taskToDoList.getItems().isEmpty()){
                taskToDoList.getItems().add(new Task());
            }
        });
        taskOpenList.setOnDragEntered((DragEvent event) -> {
            if(taskOpenList.getItems().isEmpty()){
                taskOpenList.getItems().add(new Task());
            }
        });
        taskDoneList.setOnDragEntered((DragEvent event) -> {
            if(taskDoneList.getItems().isEmpty()){
                taskDoneList.getItems().add(new Task());
            }
        });

        taskToDoList.setOnDragExited((DragEvent event) -> {
            ObservableList<Task> tlist = taskToDoList.getItems();
            int ind = -1;
            for(Task t: tlist){
                if(t.getTitle()==null){
                    ind = tlist.indexOf(t);
                }
            }
            if(-1<ind){ tlist.remove(ind); }
        });
        taskOpenList.setOnDragExited((DragEvent event) -> {
            ObservableList<Task> tlist = taskOpenList.getItems();
            int ind = -1;
            for(Task t: tlist){
                if(t.getTitle()==null){
                    ind = tlist.indexOf(t);
                }
            }
            if(-1<ind){ tlist.remove(ind); }
        });
        taskDoneList.setOnDragExited((DragEvent event) -> {
            ObservableList<Task> tlist = taskDoneList.getItems();
            int ind = -1;
            for(Task t: tlist){
                if(t.getTitle()==null){
                    ind = tlist.indexOf(t);
                }
            }
            if(-1<ind){ tlist.remove(ind); }
        });
    }

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;

        taskToDoList.setItems(mainApp.getToDo());
        taskDoneList.setItems(mainApp.getDone());
        taskOpenList.setItems(mainApp.getOpen());
    }

    @FXML
    private void handleAddTask() {
        Task tempTask = new Task();
        boolean isOk = mainApp.showEditBoard(tempTask);
        if(isOk){
            mainApp.getToDo().add(tempTask);
        }
    }

    @FXML
    private void editTask(Task selectedTask) {
        if (selectedTask != null) {
            boolean okClicked = mainApp.showEditBoard(selectedTask);
            if (okClicked) {
            }
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No task selected");
            alert.setContentText("Please select a task in the table.");

            alert.showAndWait();
        }
    }

    private class TaskContextMenu extends ContextMenu {

        public TaskContextMenu(ListView<Task> list) {

            MenuItem mEdit = new MenuItem("Edit");
            mEdit.setOnAction(event -> {
                editTask(list.getSelectionModel().getSelectedItem());
                ((TaskViewSkin) list.getSkin()).refresh();
                event.consume();
            });

            MenuItem mDelete = new MenuItem("Delete");
            mDelete.setOnAction(event -> {
                int selectedIndex = list.getSelectionModel().getSelectedIndex();
                list.getItems().remove(selectedIndex);
                event.consume();
            });

            getItems().add(mEdit);
            getItems().add(mDelete);
        }

    }
}
