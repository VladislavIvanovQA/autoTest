package rest.request;

import lombok.Data;
import utils.FileUtils;
import utils.YamlObject;

import java.io.File;
import java.util.List;

@Data
public class RequestUploadTemplate {
    private File file;
    private String data;

    public RequestUploadTemplate(String fileName) {
        this.file = FileUtils.generateYaml(fileName + ".yml");
    }

    public RequestUploadTemplate(File file) {
        this.file = file;
    }

    public RequestUploadTemplate(List<YamlObject> file, String fileName) {
        this.file = FileUtils.generateYaml(fileName + ".yml");
    }

    public RequestUploadTemplate(String fileName, String data) {
        this.file = FileUtils.generateYaml(fileName + ".yml");
        setData(data);
    }

    public RequestUploadTemplate(File file, String data) {
        this.file = file;
        setData(data);
    }

    private void setData(String data) {
        this.data = data.isEmpty() ? "" : String.format("{\"tmpl_id\":\"%s\"}", data);
    }
}
