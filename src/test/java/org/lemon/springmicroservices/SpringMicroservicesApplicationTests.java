package org.lemon.springmicroservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
class SpringMicroservicesApplicationTests {

	WireMockServer wireMockServer;

	@BeforeEach
	void beforeEach() {
		wireMockServer = new WireMockServer(wireMockConfig().port(8089));
		wireMockServer.start();
	}

	@AfterEach
	void afterEach() {
		wireMockServer.stop();
	}

	@Test
	void contextLoads() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		stubFor(get(urlEqualTo("/find?author=fred"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
						.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
						.withBody("{\"title\":\"something\", \"author\": \"else\"}")));

		RestTemplate restClient = new RestTemplate();
		restClient.exchange("http://localhost:8080/v1/find?author=fred", HttpMethod.GET, null, String.class);

//		verify(getRequestedFor(urlEqualTo("/find")));
	}

}
