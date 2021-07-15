package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    public static File generateYaml(String fileName) {
        YamlObject obj = new YamlObject("id", "label");
        YamlObject obj1 = new YamlObject("id1", "label1");
        YamlObject obj2 = new YamlObject("id1", "label1", "http://ya.ru", "id");
        obj2.setLink("http://ya.ru");

        List<YamlObject> list = new ArrayList<>();
        list.add(obj);
        list.add(obj1);
        list.add(obj2);

        return createYAMLFile(new File(fileName), list);
    }

    public static File generateYaml(String fileName, YamlObject... yamlObjects) {
        List<YamlObject> list = Arrays.asList(yamlObjects);
        return createYAMLFile(new File(fileName), list);
    }

    public static File generateYaml(String fileName, List<YamlObject> yamlObjects) {
        return createYAMLFile(new File(fileName), yamlObjects);
    }

    private static File createYAMLFile(File yamlFile, List<YamlObject> list) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        try {
            mapper.writeValue(yamlFile, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yamlFile;
    }
}
