package tests;

import config.BaseTest;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import rest.RestClient;
import rest.request.RequestUploadTemplate;
import ru.integrations.check.assertions.CheckBodyField;
import utils.FileUtils;
import utils.YamlObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class InstallTemplate extends BaseTest {
    @Test
    public void installTemplate() {
        String idPerent = UUID.randomUUID().toString();
        YamlObject yamlObject = new YamlObject(idPerent, "Label");
        yamlObject.setLink("http://ya.ru");

        YamlObject dependObj = new YamlObject(UUID.randomUUID().toString(), "Label");
        yamlObject.setLink("http://ya.ru");
        yamlObject.setDepends(idPerent);

        List<YamlObject> list = Arrays.asList(yamlObject, dependObj);

        String fileName = "installTemplate".toLowerCase();
        File yaml = FileUtils.generateYaml(fileName + ".yml", list);

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(yaml), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));

        new RestClient()
                .postInstallTemplate(fileName, 200,
                        new CheckBodyField(MESSAGE_PATH, Matchers.equalTo("Template with tmpl_id=" + fileName + " successfully installed!")));

        String html = new RestClient()
                .getIndexRequest()
                .execute()
                .body().asPrettyString();

        assertHtml(html, yamlObject, dependObj);
    }

    @Test
    public void uploadTemplateBadRequestInvalidYamlFormat() throws IOException {
        String fileName = UUID.randomUUID().toString().toLowerCase();
        File yml = new File(fileName + ".yml");
        yml.createNewFile();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(yml), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));

        new RestClient()
                .postInstallTemplate(fileName, 400,
                        new CheckBodyField(MESSAGE_PATH, Matchers.equalTo("Invalid template format!")));
    }

    @Test
    public void uploadTemplateFileNotFound() {
        String filename = UUID.randomUUID().toString().toLowerCase();

        new RestClient()
                .postInstallTemplate(filename, 404,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("No template with tmpl_id=" + filename + " found!")));
    }

    @Test
    public void uploadTemplateBadRequestEmptyTmplId() {
        new RestClient()
                .postInstallTemplate("", 400,
                        new CheckBodyField(MESSAGE_PATH, Matchers.equalTo("No tmpl_id in request!")));
    }
}
