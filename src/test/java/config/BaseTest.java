package config;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.Assertion;
import utils.YamlObject;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.filters;

@Slf4j
public class BaseTest {
    public static final String MESSAGE_PATH = "message";

    @BeforeTest
    public void setUp() {
        baseURI = System.getenv("APPS_URL") == null ? "http://localhost:5000" : System.getenv("APPS_URL");
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    public void assertHtml(String html, YamlObject... yamlObjects) {
        Document parse = Jsoup.parse(html);
        Elements buttons = parse.getElementsByClass("form-group");
        for (YamlObject obj : yamlObjects) {
            Element button = buttons.stream()
                    .filter(btn -> btn.children().get(0).id().equals(obj.getId()))
                    .findFirst()
                    .get();
            button = button.children().get(0);
            if (button != null) {
                log.info("Check button id html: {}, equals to: {}", button.id(), obj.getId());
                new Assertion().assertTrue(button.id().equals(obj.getId()));
                log.info("Check button text html: {}, equals to: {}", button.text(), obj.getLabel());
                new Assertion().assertTrue(button.text().equals(obj.getLabel()));
                if (obj.getLink() != null) {
                    log.info("Check button href html: {}, equals to: {}", button.attr("href"), obj.getLink());
                    new Assertion().assertTrue(button.attr("href").equals(obj.getLink()));
                } else {
                    log.info("Check button disabled html: {}", button.className());
                    new Assertion().assertTrue(button.className().contains("disabled"));
                }
//                if (obj.getDepends() != null) {
//                    new Assertion().assertTrue(button.attr("href").equals(obj.getLink()));
//                }
            } else {
                throw new NullPointerException("Not found html object with Id: " + obj.getId());
            }
        }
    }
}
