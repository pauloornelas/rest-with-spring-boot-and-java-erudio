package br.com.erudio.data.vo.v1;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "id", "author", "launchDate", "price", "title" })
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

	private static final long serialVersionUID = 1L;

	public BookVO() {
	}

	@Mapping("id")
	@JsonProperty("id")
	@Schema(description = "Id - chave no banco")
	private Long key;

	@Schema(description = "Nome do Autor")
	private String author;

	@Schema(description = "Data de Lançamento", example = "dd/MM/yyyy")
	private Date launchDate;

	@Schema(description = "Preço")
	private Double price;

	@Schema(description = "Titulo do Livro")
	private String title;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}
}
