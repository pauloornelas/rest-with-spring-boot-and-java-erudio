package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.BookVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import br.com.erudio.integrationtests.vo.pagedmodels.PagedModelBook;
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
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    private static YMLMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
        book = new BookVO();
    }
    
    @Test
    @Order(1)
    public void authorization() {
    	
    	AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var token = given()
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

            specification =
                new RequestSpecBuilder()
                    .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token)
                    .setBasePath("/api/book/v1")
                    .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
    }
      
    @Test
    @Order(2)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        
        mockBook();

        book = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(book, objectMapper)
				.when()
				.post()
				.then().statusCode(200).extract().body().as(BookVO.class, objectMapper);
        
        assertNotNull(book.getId());
        assertNotNull(book.getTitle());
        assertNotNull(book.getAuthor());
        assertNotNull(book.getPrice());
        assertTrue(book.getId() > 0);
        assertEquals("Docker Deep Dive", book.getTitle());
        assertEquals("Nigel Poulton", book.getAuthor());
        assertEquals(55.99D, book.getPrice());
    }
      
    @Test
    @Order(3)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        
        book.setTitle("Docker Deep Dive - Updated");

        BookVO bookUpdated = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .body(book, objectMapper)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);
        
        assertNotNull(bookUpdated.getId());
        assertNotNull(bookUpdated.getTitle());
        assertNotNull(bookUpdated.getAuthor());
        assertNotNull(bookUpdated.getPrice());
        assertEquals(bookUpdated.getId(), book.getId());
        assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
        assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        assertEquals(55.99, bookUpdated.getPrice());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        var foundBook = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .pathParam("id", book.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);
        
        assertNotNull(foundBook.getId());
        assertNotNull(foundBook.getTitle());
        assertNotNull(foundBook.getAuthor());
        assertNotNull(foundBook.getPrice());
        assertEquals(foundBook.getId(), book.getId());
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals(55.99, foundBook.getPrice());
    }
    
    @Test
    @Order(5)
    public void testDelete() {
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
            .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
                    .pathParam("id", book.getId())
                    .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
    
    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        var wrapper = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 0 , "limit", 10, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(PagedModelBook.class, objectMapper); 


        var books = wrapper.getContent();

        BookVO foundBookOne = books.get(0);
        
        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getPrice());
        assertEquals(144, foundBookOne.getId());
        assertEquals("10 000 timmar", foundBookOne.getTitle());
        assertEquals("Wyatan", foundBookOne.getAuthor());
        assertEquals(26.29, foundBookOne.getPrice());
        
        BookVO foundBookFive = books.get(4);
        
        assertNotNull(foundBookFive.getId());
        assertNotNull(foundBookFive.getTitle());
        assertNotNull(foundBookFive.getAuthor());
        assertNotNull(foundBookFive.getPrice());
        assertEquals(349, foundBookFive.getId());
        assertEquals("3 A.M.", foundBookFive.getTitle());
        assertEquals("Odelinda", foundBookFive.getAuthor());
        assertEquals(45.43, foundBookFive.getPrice());
    }
    
    @Test
    @Order(7)
    public void testHATEOAS() throws JsonMappingException, JsonProcessingException {
        var unthreatedContebt = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 0 , "limit", 10, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        
        var content = unthreatedContebt.replace("\n", "").replace("\r", "");
        
        var serverPort = TestConfigs.SERVER_PORT;
        String port = serverPort == 80 ? "" : ":" + String.valueOf(serverPort);
        
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost"+ port +"/api/book/v1/144\""));
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost"+ port +"/api/book/v1/234\""));
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost"+ port +"/api/book/v1/261\""));
        
        assertTrue(content.contains("page:  size: 12  totalElements: 1015  totalPages: 85  number: 0"));
        
        assertTrue(content.contains("rel: \"first\"  href: \"http://localhost"+ port +"/api/book/v1?direction=asc&page=0&size=12&sort=title,asc\""));
        assertTrue(content.contains("rel: \"self\"  href: \"http://localhost"+ port +"/api/book/v1?page=0&size=12&direction=asc\""));
        assertTrue(content.contains("rel: \"next\"  href: \"http://localhost"+ port +"/api/book/v1?direction=asc&page=1&size=12&sort=title,asc\""));
        assertTrue(content.contains("rel: \"last\"  href: \"http://localhost"+ port +"/api/book/v1?direction=asc&page=84&size=12&sort=title,asc\""));
        
    }
     
    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(55.99D);
        book.setLaunchDate(new Date("05/11/2017"));
    }
}