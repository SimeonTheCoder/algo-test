package app;

import org.springframework.web.multipart.MultipartFile;

public class Data {
    MultipartFile file;
    String name;
    String task;

    public Data(MultipartFile file, String name, String task) {
        this.file = file;
        this.name = name;
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
