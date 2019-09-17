package ms.jsm.kanban.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ms.jsm.kanban.model.Task;

import java.time.LocalDate;

public class EditBoardController {

    private Stage dialogStage;
    private Task task;
    private boolean isOk = false;

    @FXML
    TextField titleField;
    @FXML
    TextField priorityField;
    @FXML
    TextArea descArea;
    @FXML
    DatePicker datePicker;

    public EditBoardController(){    }

    private final KeyCombination shift_enter = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_DOWN);

    @FXML
    public void initialize(){

        // force the priority field to be numeric only
        priorityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    priorityField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        titleField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ESCAPE)handleCancel();
                if(event.getCode() == KeyCode.ENTER){
                    handleOk();
                }
            }
        });
        priorityField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ESCAPE)handleCancel();
                if(event.getCode() == KeyCode.ENTER){
                    handleOk();
                }
            }
        });
        datePicker.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ESCAPE)handleCancel();
                if(event.getCode() == KeyCode.ENTER){
                    handleOk();
                }
            }
        });
        descArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.ESCAPE)handleCancel();
                if(shift_enter.match(event)){
                    descArea.appendText("\n");
                }else if(event.getCode()==KeyCode.ENTER){
                    handleOk();
                }
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        this.dialogStage.setResizable(false);
    }
    public boolean isOk() {
        return isOk;
    }

    //key Shift + Enter
    @FXML
    private void handleOk(){
        if(isInputValid()){
            task.setTitle(titleField.getText());
            task.setPriority(Integer.parseInt(priorityField.getText()));
            task.setExpiration(datePicker.getValue());
            task.setDescription(descArea.getText());

            isOk = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }

        if (priorityField.getText() == null || priorityField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n";
        } else {
            // try to parse the priority into an int.
            try {
                Integer.parseInt(priorityField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid priority (must be an integer)!\n";
            }
        }

        if (datePicker.getValue() == null || datePicker.getValue().isBefore(LocalDate.now())) {
            errorMessage += "No valid birthday!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    //key Esc
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setTask(Task task) {
        this.task = task;

        titleField.setText(task.getTitle());
        priorityField.setText(Integer.toString(task.getPriority()));
        datePicker.setValue(task.getExpiration());
        descArea.setText(task.getDescription());
    }
}
