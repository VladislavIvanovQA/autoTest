package utils;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestUtils {


    public static <T> T sendAndGetResponse(Class<T> responseObject, RequestSpecification requestSpecification) {
        RequestSpecification specification;

        if (requestSpecification == null) {
            specification = given();
        } else {
            specification = requestSpecification;
        }

        return specification
                .with()
                .post()
                .then()
                .extract()
                .as(responseObject);
    }
}
