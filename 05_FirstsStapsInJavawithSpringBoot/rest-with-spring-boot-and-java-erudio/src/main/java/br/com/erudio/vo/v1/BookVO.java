package br.com.erudio.vo.v1;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import br.com.erudio.util.DateHelper;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "id", "author", "launchDate", "price", "title" })
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Mapping("id")
	@JsonProperty("id")
	@Schema(description = "Id - chave no banco")
	private Long key;

	@Schema(description = "Nome do Autor")
	private String author;

	@Schema(description = "Data de Lançamento", example = "dd/MM/yyyy")
	private String launchDate;

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

	@JsonProperty("launchDate")
	public String getLaunch_dateString() {
		return launchDate;
	}

	@JsonIgnore
	public Date getLaunchDate() {
		Date data = null;
		try {
			data = DateHelper.parseStringToPattern(launchDate, DateHelper.DATE_PATTERN_DEFAULT_API);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	@JsonProperty("launchDate")
	public void setLaunchDate(String launchDate) {
		this.launchDate = launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		this.launchDate = DateHelper.formatDateToPattern(launchDate, DateHelper.DATE_PATTERN_DEFAULT_API);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(author, key, launchDate, price, title);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookVO other = (BookVO) obj;
		return Objects.equals(author, other.author) && Objects.equals(key, other.key)
				&& Objects.equals(launchDate, other.launchDate) && Objects.equals(price, other.price)
				&& Objects.equals(title, other.title);
	}

}
