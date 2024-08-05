import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import io.restassured.http.ContentType;
import netscape.javascript.JSObject;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@com.github.tomakehurst.wiremock.junit5.WireMockTest(httpsEnabled = true, httpPort = 8080)
public class WireMockTest {

    @Test
    void testWireMock(WireMockRuntimeInfo wmRuntimeInfo) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Janek");
        jsonObject.put("age", 30);
        stubFor(WireMock.get("/hello")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonObject.toString())));

        int port = wmRuntimeInfo.getHttpPort();

        given()
                .when()
                .contentType(ContentType.JSON)
                .get("http://localhost:" + port + "/hello")
                .then()
                .body("name", containsString("Janek"))
                .body("age", Matchers.equalTo(30) )
                .statusCode(200)
                .log().all();
    }
}
