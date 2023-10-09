package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.PersonVO;
import br.com.erudio.integrationtests.vo.pagedmodels.PagedModelPerson;
import br.com.erudio.integrationtests.vo.wrappes.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup() {
		objectMapper = new YMLMapper();
		person = new PersonVO();
	}

	@Test
	@Order(1)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var accessToken = given()
				.config(
						RestAssuredConfig
							.config()
							.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(
									TestConfigs.CONTENT_TYPE_YML,
									ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
					.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class, objectMapper)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	
	@Test
	@Order(2)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper)
				.when()
				.post()
				.then().statusCode(200).extract().body().as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		
		assertTrue(persistedPerson.getId() > 0);
		assertTrue(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(3)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		person.setLastName("Piquet Souto Maior");
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper)
				.when()
				.post()
				.then().statusCode(200).extract().body().as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertEquals(person.getId(), persistedPerson.getId());
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		
		assertTrue(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(4)
	public void testDisabePersonById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then().statusCode(200).extract().body().as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		
		assertTrue(persistedPerson.getId() > 0);
		assertFalse(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(5)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then().statusCode(200).extract().body().as(PersonVO.class, objectMapper);
		
		person = persistedPerson;
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertEquals("Nelson", persistedPerson.getFirstName());
		assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
		assertEquals("Brasília - DF - Brasil", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
		
		assertTrue(persistedPerson.getId() > 0);
		assertFalse(persistedPerson.getEnabled());
	}
	
	@Test
	@Order(6)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.config(RestAssuredConfig
				.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId())
				.when()
				.delete("{id}")
				.then().statusCode(204);
	}
	
	@Test
	@Order(7)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var wrapper = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body().as(PagedModelPerson.class, objectMapper);
		
		var people = wrapper.getContent();
		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		
		assertEquals(670, foundPersonOne.getId());
		assertEquals("Alic", foundPersonOne.getFirstName());
		assertEquals("Terbrug", foundPersonOne.getLastName());
		assertEquals("3 Eagle Crest Court", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());
		assertTrue(foundPersonOne.getEnabled());
		
		PersonVO foundPersonThree = people.get(2);
		
		assertNotNull(foundPersonThree.getId());
		assertNotNull(foundPersonThree.getFirstName());
		assertNotNull(foundPersonThree.getLastName());
		assertNotNull(foundPersonThree.getAddress());
		assertNotNull(foundPersonThree.getGender());
		
		assertEquals(839, foundPersonThree.getId());
		assertEquals("Alison", foundPersonThree.getFirstName());
		assertEquals("Cantua", foundPersonThree.getLastName());
		assertEquals("45 Raven Terrace", foundPersonThree.getAddress());
		assertEquals("Female", foundPersonThree.getGender());
		assertTrue(foundPersonThree.getEnabled());
		
	}
	
	@Test
	@Order(8)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		var wrapper = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("firstName", "telem")
				.queryParams("page", 0, "size", 10, "direction", "asc")
					.when()
					.get("findPersonByName/{firstName}")
				.then()
					.statusCode(200)
						.extract()
						.body().as(PagedModelPerson.class, objectMapper);
		
		var people = wrapper.getContent();
		PersonVO foundPersonOne = people.get(0);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		
		assertEquals(3, foundPersonOne.getId());
		assertEquals("Télem", foundPersonOne.getFirstName());
		assertEquals("Borges Giro", foundPersonOne.getLastName());
		assertEquals("Guarulhos - São Paulo", foundPersonOne.getAddress());
		assertEquals("Female", foundPersonOne.getGender());
		assertTrue(foundPersonOne.getEnabled());
	}
	
	@Test
	@Order(9)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
		
		given().spec(specificationWithoutToken)
			.config(RestAssuredConfig
				.config()
				.encoderConfig(EncoderConfig.encoderConfig()
						.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
			.contentType(TestConfigs.CONTENT_TYPE_YML)
			.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
			.then()
				.statusCode(403);
	}
	
	@Test
	@Order(10)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
		
		var unthreatedContebt = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
						.body().asString();
		
		var content = unthreatedContebt.replace("\n", "").replace("\r", "");
		
		var serverPort = TestConfigs.SERVER_PORT;
        String port = serverPort == 80 ? "" : ":" + String.valueOf(serverPort);
		
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost"+ port +"/api/person/v1/670\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost"+ port +"/api/person/v1/839\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost"+ port +"/api/person/v1/790\""));
		
		assertTrue(content.contains("page:  size: 10  totalElements: 1003  totalPages: 101  number: 3"));
		
		assertTrue(content.contains("rel: \"first\"  href: \"http://localhost"+ port +"/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"prev\"  href: \"http://localhost"+ port +"/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"self\"  href: \"http://localhost"+ port +"/api/person/v1?page=3&size=10&direction=asc\""));
		assertTrue(content.contains("rel: \"next\"  href: \"http://localhost"+ port +"/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"last\"  href: \"http://localhost"+ port +"/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\""));
		
	}

	private void mockPerson() {
		person.setFirstName("Nelson");
		person.setLastName("Piquet");
		person.setAddress("Brasília - DF - Brasil");
		person.setGender("Male");
		person.setEnabled(true);
	}

}