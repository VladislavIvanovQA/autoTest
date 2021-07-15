package tests;

import config.BaseTest;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import rest.RestClient;
import rest.request.RequestUploadTemplate;
import ru.integrations.check.assertions.CheckBodyField;
import utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UploadTemplate extends BaseTest {

    @Test
    public void uploadTemplateCreatedYML() {
        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));
    }

    @Test
    public void uploadTemplateCreatedYAML() {
        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));
    }

    @Test
    public void uploadTemplateBadRequestInvalidYamlFormat() {
        String fileName = "temp".toLowerCase();
        File txt = FileUtils.generateYaml(fileName + ".txt");

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(txt), 400,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.oneOf("Allowed file types are {'yaml', 'yml'}", "Allowed file types are {'yml', 'yaml'}")));
    }

    @Test
    public void uploadTemplateFileNotFound() {
        new RestClient()
                .postUploadTemplate(given(), 400,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("No file part in the request")));
    }

    @Test
    public void uploadTemplateNoFileSelected() throws FileNotFoundException {
        String fileName = "temp".toLowerCase();
        File yml = FileUtils.generateYaml(fileName + ".yml");
        FileInputStream inputStream = new FileInputStream(yml);

        RequestSpecification file = given()
                .multiPart("file", "", inputStream);

        new RestClient()
                .postUploadTemplate(file, 400,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("No file selected for uploading")));
    }


    @Test
    public void uploadTemplateWithTmplId() {
        String fileName = "temp".toLowerCase();
        String customName = "customName".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName, customName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + customName)));
    }

    @Test
    public void uploadTemplateBadRequestEmptyTmplId() {
        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplateError(
                        new RequestUploadTemplate(fileName, ""), 400);
    }

    @Test
    public void uploadTemplateBadRequestEmptyYmlFile() throws IOException {
        String fileName = "temp".toLowerCase();
        File yml = new File(fileName + ".yml");
        yml.createNewFile();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(yml), 400,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Invalid template format!")));
    }

    @Test
    public void uploadTemplateMethodNotAllowed() {
        String fileName = "temp".toLowerCase();
        File yml = FileUtils.generateYaml(fileName + ".yml");

        RequestSpecification file = given()
                .formParam("file", yml);

        new RestClient()
                .postUploadTemplate(file, 405);
    }

    @Test
    public void uploadTemplateBadRequestNoSentFile() {
        RequestSpecification file = given()
                .multiPart("data", "{\"tmpl_id\":\"temp\"}");

        new RestClient()
                .postUploadTemplate(file, 400,
                        new CheckBodyField(MESSAGE_PATH, Matchers.equalTo("No file part in the request")));
    }
}
