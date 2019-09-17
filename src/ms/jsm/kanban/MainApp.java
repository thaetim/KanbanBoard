package ms.jsm.kanban;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ms.jsm.kanban.model.Task;
import ms.jsm.kanban.view.EditBoardController;
import ms.jsm.kanban.view.MainBoardController;
import ms.jsm.kanban.view.RootController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainApp extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Task> tasksToDo = FXCollections.observableArrayList();
    private ObservableList<Task> tasksOpen = FXCollections.observableArrayList();
    private ObservableList<Task> tasksDone = FXCollections.observableArrayList();

    public MainApp(){

    }

    public ObservableList<Task> getToDo(){
        return tasksToDo;
    }
    public ObservableList<Task> getOpen(){
        return tasksOpen;
    }
    public ObservableList<Task> getDone(){
        return tasksDone;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Kanban");
        this.primaryStage.setMinHeight(200);
        this.primaryStage.setMinWidth(680);
        initRootLayout();
        showMainBoard();
    }

    public void dummyTasks(){
        tasksDone.add(new Task("Make tea",1));
        tasksDone.add(new Task("Finish this project",1));
        tasksDone.add(new Task("XD",1));
        tasksOpen.add(new Task("Learn JavaFX",2));
        tasksOpen.add(new Task("Learn how to not give up on learning JavaFX",20));
        tasksToDo.add(new Task("Sleep",10));
        tasksToDo.add(new Task("Make dank memez",10));
    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader rootloader = new FXMLLoader();
            rootloader.setLocation(MainApp.class.getResource("view/Root.fxml"));
            rootLayout = rootloader.load();

            // Set root controller
            RootController rootController = rootloader.getController();
            rootController.setMainApp(this);

            // Load last session ----------- +
            loadLast(rootController);
            //dummyTasks();

            // Set program to save before closing ----------- +
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("Stage is closing");
                rootController.handleClose();
            });

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLast(RootController c){
        String path = System.getProperty("user.dir")+"\\kanban_last.ser";
        try{
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            c.deserializeTaskList(in,this.getToDo());
            c.deserializeTaskList(in,this.getOpen());
            c.deserializeTaskList(in,this.getDone());
            in.close();
            fileIn.close();
            System.out.println("Loaded last session");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showMainBoard() {
        try {
            // Load main board.
            FXMLLoader mainloader = new FXMLLoader();
            mainloader.setLocation(MainApp.class.getResource("view/MainBoard.fxml"));
            GridPane board = (GridPane) mainloader.load();

            // Set main board into the center of root layout.
            rootLayout.setCenter(board);

            // Give controller access to the main app.
            MainBoardController mainBoardController = mainloader.getController();
            mainBoardController.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public boolean showEditBoard(Task task){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditBoard.fxml"));
            StackPane editBoard = (StackPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Task");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBoard);
            dialogStage.setScene(scene);

            // Set the task into the controller.
            EditBoardController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTask(task);

            // Show the dialog and wait until the user closes it.
            dialogStage.showAndWait();

            return controller.isOk();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        launch(args);
    }
}
