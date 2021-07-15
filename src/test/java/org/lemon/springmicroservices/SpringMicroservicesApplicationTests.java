package org.lemon.springmicroservices;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SpringMicroservicesApplicationTests {

	@Autowired
	MockMvc mockMvc;

	WireMockServer wireMockServer;

	@BeforeEach
	void beforeEach() {
		wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));
		wireMockServer.start();
	}

	@AfterEach
	void afterEach() {
		wireMockServer.stop();
	}

	@Test
	void testRootPath() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/v1");
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("default"));
	}

	@Test
	void testFindAuthor() throws Exception {
		final String JSON_TEXT = "{\"title\":\"something\", \"author\": \"else\"}";

		UrlPattern urlPattern = WireMock.urlEqualTo("/");
		ResponseDefinitionBuilder aResponse = WireMock.aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody(JSON_TEXT);
		MappingBuilder aRequest = WireMock.get(urlPattern).willReturn(aResponse);
		wireMockServer.stubFor(aRequest);

		RequestBuilder request = MockMvcRequestBuilders.get("/v1/find?author=fred");
		mockMvc.perform(request)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content()
						.json(JSON_TEXT));
	}
}
