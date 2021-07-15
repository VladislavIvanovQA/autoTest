package tests;

import config.BaseTest;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import rest.RestClient;
import rest.request.RequestUploadTemplate;
import rest.response.ResponseListTemplates;
import ru.integrations.check.assertions.CheckBodyField;

public class ListTemplates extends BaseTest {
    @Test
    public void getListTemplates() {
        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));

        new RestClient()
                .getListTemplates(200,
                        new CheckBodyField("templates", Matchers.hasItem(fileName)));
    }

    @Test
    public void getListTemplatesEmpty() {
        ResponseListTemplates listTemplates = new RestClient().getListTemplates(200)
                .asPOJO(ResponseListTemplates.class);

        if (listTemplates.getTemplates().size() > 0) {
            listTemplates.getTemplates().forEach(temp -> {
                new RestClient().deleteTemplate(temp, 200);
            });
        }

        new RestClient()
                .getListTemplates(200, new CheckBodyField("templates", Matchers.hasSize(0)));
    }

    @Test
    public void getListTemplatesDeleteOne() {
        ResponseListTemplates listTemplates = new RestClient().getListTemplates(200)
                .asPOJO(ResponseListTemplates.class);

        if (listTemplates.getTemplates().size() > 0) {
            listTemplates.getTemplates().forEach(temp -> {
                new RestClient().deleteTemplate(temp, 200);
            });
        }

        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));

        String otherFileName = "temp1".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(otherFileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + otherFileName)));

        new RestClient()
                .getListTemplates(200, new CheckBodyField("templates", Matchers.hasSize(2)));

        new RestClient()
                .deleteTemplate(otherFileName, 200);

        new RestClient()
                .getListTemplates(200, new CheckBodyField("templates", Matchers.hasSize(1)));
    }

    @Test
    public void getListTemplatesInstallOne() {
        ResponseListTemplates listTemplates = new RestClient().getListTemplates(200)
                .asPOJO(ResponseListTemplates.class);

        if (listTemplates.getTemplates().size() > 0) {
            listTemplates.getTemplates().forEach(temp -> {
                new RestClient().deleteTemplate(temp, 200);
            });
        }

        String fileName = "temp".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(fileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + fileName)));

        String otherFileName = "temp1".toLowerCase();

        new RestClient()
                .postUploadTemplate(
                        new RequestUploadTemplate(otherFileName), 201,
                        new CheckBodyField(MESSAGE_PATH,
                                Matchers.equalTo("Template successfully uploaded. tmpl_id=" + otherFileName)));

        new RestClient()
                .getListTemplates(200, new CheckBodyField("templates", Matchers.hasSize(2)));

        new RestClient()
                .postInstallTemplate(otherFileName, 200);

        new RestClient()
                .getListTemplates(200, new CheckBodyField("templates", Matchers.hasSize(2)));
    }
}
