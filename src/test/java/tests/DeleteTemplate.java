package tests;

import config.BaseTest;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import rest.RestClient;
import rest.request.RequestUploadTemplate;
import ru.integrations.check.assertions.CheckBodyField;

import java.util.UUID;

public class DeleteTemplate extends BaseTest {
    @Test
    public void deleteTemplates() {
        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));

        new RestClient()
                .deleteTemplate(fileName, 200,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template with tmpl_id=" + fileName + " successfully deleted!")));
    }

    @Test
    public void deleteTemplatesBadRequestEmptyTmplId() {
        new RestClient()
                .deleteTemplate("", 400,
                        new CheckBodyField(MESSAGE_PATH, Matchers.equalTo("No tmpl_id in request!")));
    }

    @Test
    public void deleteTemplatesFileNotFound() {
        String fileName = UUID.randomUUID().toString();

        new RestClient()
                .deleteTemplate(fileName, 404,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("No template with tmpl_id=" + fileName + " found!")));
    }
}
