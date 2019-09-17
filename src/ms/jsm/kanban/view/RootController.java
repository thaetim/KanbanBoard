package ms.jsm.kanban.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import ms.jsm.kanban.MainApp;
import ms.jsm.kanban.model.Task;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javafx.application.Platform.exit;

public class RootController {

    private MainApp mainApp;
    private FileChooser fileChooser;

    public RootController(){    }

    public void initialize(){
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:/PROJECT CODE/Java/JSM6"));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleClose(){
        //save before closing
        try{
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir")+"\\kanban_last.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            serializeTaskList(mainApp.getToDo(),out);
            serializeTaskList(mainApp.getOpen(),out);
            serializeTaskList(mainApp.getDone(),out);
            out.close();
            fileOut.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Saved before closing");
        exit();
    }

    @FXML
    private void handleSave(){
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Serialized data files (*.ser)","*.ser"));
        fileChooser.setInitialFileName("kanban_save.ser");
        File dir = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if(dir!=null){
            try{
                FileOutputStream fileOut = new FileOutputStream(dir.getAbsolutePath());
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                serializeTaskList(mainApp.getToDo(),out);
                serializeTaskList(mainApp.getOpen(),out);
                serializeTaskList(mainApp.getDone(),out);
                out.close();
                fileOut.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            System.out.println("Serialization success");
        }
    }

    private void serializeTaskList(ObservableList<Task> list, ObjectOutputStream out){
        //convert to ArrayList
        ArrayList<Task> alist = new ArrayList<>(list.size());
        alist.addAll(list);
        //serialize
        try{
            out.writeObject(alist);
            System.out.println("List serialized");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpen(){
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Serialized data files (*.ser)","*.ser"));
        File dir = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if(dir!=null){
            try{
                FileInputStream fileIn = new FileInputStream(dir.getAbsolutePath());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                deserializeTaskList(in,mainApp.getToDo());
                deserializeTaskList(in,mainApp.getOpen());
                deserializeTaskList(in,mainApp.getDone());
                in.close();
                fileIn.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            System.out.println("Deserialization success");
        }
    }

    public void deserializeTaskList(ObjectInputStream in, ObservableList<Task> list){
        //deserialize
        ArrayList<Task> alist = null;
        try{
            alist = (ArrayList<Task>) in.readObject();
            System.out.println("List serialized data opened");
        }catch(IOException | ClassNotFoundException e){
            if(e instanceof ClassNotFoundException){
                System.err.println("Deserialization: class not found");
            }
            e.printStackTrace();
        }
        //convert to ObservableList<Task>
        if(alist!=null & !alist.isEmpty()){
            list.clear();
            list.addAll(alist);
        }
    }

    @FXML
    private void handleExport(){
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)","*.csv"));
        fileChooser.setInitialFileName("kanban.csv");
        File dir = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        String path = dir.getAbsolutePath().replaceAll(".csv","");
        try{
            FileWriter writer = new FileWriter(path+"_1todo.csv");
            writer.append(taskListToCSV(mainApp.getToDo()));writer.flush();
            writer = new FileWriter(path+"_2open.csv");
            writer.append(taskListToCSV(mainApp.getOpen()));writer.flush();
            writer = new FileWriter(path+"_3done.csv");
            writer.append(taskListToCSV(mainApp.getDone()));writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("CSV export succesful");
    }

    private String taskListToCSV(ObservableList<Task> tlist){
        StringBuilder sb = new StringBuilder();
        sb.append("Title,Priority,Expiration,Description");
        for(Task t: tlist){
            sb.append("\n");
            sb.append(t.getTitle());                    sb.append(',');
            sb.append(t.getPriority());                 sb.append(',');
            sb.append(t.getExpiration().toString());    sb.append(',');
            sb.append(t.getDescription());
        }
        return sb.toString();
    }

    @FXML
    private void handleImport(){
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files (*.csv)","*.csv"));
        List<File> dirs = fileChooser.showOpenMultipleDialog(mainApp.getPrimaryStage());
        int loaded_count = 0;
        for(File f: dirs){
            String path = f.getPath();
            System.out.println(path);
            System.out.println(f.getAbsolutePath());
            if((path.substring(path.lastIndexOf("\\"))).contains("todo")){
                mainApp.getToDo().clear();
                mainApp.getToDo().addAll(taskListFromCSV(f.getAbsolutePath()));
                loaded_count++;
            }
            if((path.substring(path.lastIndexOf("\\"))).contains("open")){
                mainApp.getOpen().clear();
                mainApp.getOpen().addAll(taskListFromCSV(f.getAbsolutePath()));
                loaded_count++;
            }
            if((path.substring(path.lastIndexOf("\\"))).contains("done")){
                mainApp.getDone().clear();
                mainApp.getDone().addAll(taskListFromCSV(f.getAbsolutePath()));
                loaded_count++;
            }
        }
        System.out.println("Import: Succesfully loaded "+loaded_count+" files.");
    }

    private ObservableList<Task> taskListFromCSV(String filepath){
        ObservableList<Task> tlist = FXCollections.observableArrayList();
        String line;
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(filepath));
            br.readLine();
            while((line = br.readLine()) != null){
                Task t = new Task();
                String[] vals = line.split(",");
                t.setTitle(vals[0]);
                t.setPriority(Integer.parseInt(vals[1]));
                t.setExpiration(LocalDate.parse(vals[2]));
                try{
                    t.setDescription(vals[3]);
                }catch(IndexOutOfBoundsException e){
                    t.setDescription("");
                }
                tlist.add(t);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tlist;
    }
}
