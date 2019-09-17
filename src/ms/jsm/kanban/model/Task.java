package ms.jsm.kanban.model;

import javafx.beans.property.*;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {

    private String title;
    private Integer priority;
    private LocalDate expiration;
    private String description;

    private static final long serialVersionUID = 19L;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task(){
        this(null, 1);
    }

    public Task(String title, int priority){
        this.title = title;
        this.priority = priority;
        this.expiration = LocalDate.of(LocalDate.now().getYear() + 1,
                LocalDate.now().getMonth(),
                LocalDate.now().getDayOfMonth());
        this.description = null;
    }

    public Task(String title, int priority, LocalDate expiration, String description){
        this.title = title;
        this.priority = priority;
        this.expiration = expiration;
        this.description = description;
    }

    public Task(String title, int priority, LocalDate expiration){
        this.title = title;
        this.priority = priority;
        this.expiration = expiration;
        this.description = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public String getDetails(){
        return title + '\n' + priority.toString() + '\n' + expiration.toString() + '\n' + description;
    }
}
