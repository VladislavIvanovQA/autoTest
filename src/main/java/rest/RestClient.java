package rest;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.request.RequestUploadTemplate;
import ru.integrations.check.assertions.AssertableResponse;
import ru.integrations.check.assertions.CheckBodyField;

import static io.restassured.RestAssured.given;


public class RestClient {

    public static final String API_PATH = "/api/v1/templates";

    public AssertableResponse getIndexRequest() {
        return new AssertableResponse(given().get());
    }

    public AssertableResponse postUploadTemplate(RequestUploadTemplate template, Integer expectedStatusCode, CheckBodyField... checkBodyFields) {
        return new AssertableResponse(getRequestSpecification(template), expectedStatusCode, checkBodyFields);
    }

    public AssertableResponse postUploadTemplateError(RequestUploadTemplate template, Integer expectedStatusCode, CheckBodyField... checkBodyFields) {
        return new AssertableResponse(getRequestSpecificationError(template), expectedStatusCode, checkBodyFields);
    }

    public AssertableResponse postUploadTemplate(RequestSpecification requestSpecification, Integer expectedStatusCode, CheckBodyField... checkBodyFields) {
        return new AssertableResponse(given(requestSpecification).post(API_PATH), expectedStatusCode, checkBodyFields);
    }

    public AssertableResponse getListTemplates(Integer expectedStatusCode) {
        return new AssertableResponse(given().get(API_PATH), expectedStatusCode);
    }

    public AssertableResponse getListTemplates(Integer expectedStatusCode, CheckBodyField... checkBodyFields) {
        return new AssertableResponse(given().get(API_PATH), expectedStatusCode, checkBodyFields);
    }

    public AssertableResponse deleteTemplate(String templateId, Integer expectedStatusCode) {
        return new AssertableResponse(given()
                .pathParam("tmpl_id", templateId)
                .delete(API_PATH + "/{tmpl_id}"), expectedStatusCode);
    }

    public AssertableResponse deleteTemplate(String templateId, Integer expectedStatusCode, CheckBodyField... checkBodyFields) {
        return new AssertableResponse(given()
                .pathParam("tmpl_id", templateId)
                .delete(API_PATH + "/{tmpl_id}"), expectedStatusCode, checkBodyFields);
    }

    public AssertableResponse postInstallTemplate(String templateId, Integer expectedStatusCode) {
        return new AssertableResponse(given()
                .pathParam("tmpl_id", templateId)
                .post(API_PATH + "/{tmpl_id}/install"), expectedStatusCode);
    }

    public AssertableResponse postInstallTemplate(String templateId, Integer expectedStatusCode, CheckBodyField... checkBodyFields) {
        return new AssertableResponse(given()
                .pathParam("tmpl_id", templateId)
                .post(API_PATH + "/{tmpl_id}/install"), expectedStatusCode, checkBodyFields);
    }

    private Response getRequestSpecification(RequestUploadTemplate requestUploadTemplate) {
        RequestSpecification requestSpecification = given();
        requestSpecification.multiPart(requestUploadTemplate.getFile());

        if (requestUploadTemplate.getData() != null && !requestUploadTemplate.getData().isEmpty()) {
            requestSpecification.multiPart("data", requestUploadTemplate.getData());
        }
        return requestSpecification.post(API_PATH);
    }

    private Response getRequestSpecificationError(RequestUploadTemplate requestUploadTemplate) {
        RequestSpecification requestSpecification = given();
        requestSpecification.multiPart(requestUploadTemplate.getFile());
        requestSpecification.multiPart("data", requestUploadTemplate.getData());
        return requestSpecification.post(API_PATH);
    }
}
