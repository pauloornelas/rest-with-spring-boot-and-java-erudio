package br.com.erudio.mapper.custom;

import org.springframework.stereotype.Service;

import br.com.erudio.model.Book;
import br.com.erudio.vo.v1.BookVO;

@Service
public class BookMepper {
	
	public static Book convertVoToEntity(BookVO vo){
		Book entity = new Book();
		
		entity.setAuthor(vo.getAuthor());
		entity.setId(vo.getKey());
		entity.setLaunchDate(vo.getLaunchDate());
		entity.setPrice(vo.getPrice());
		entity.setTitle(vo.getTitle());
		
		return entity;
	}
	
	public static BookVO convertEntityToVO(Book entity){
		BookVO vo = new BookVO();
		
		vo.setAuthor(entity.getAuthor());
		vo.setKey(entity.getId());
		vo.setLaunchDate(entity.getLaunchDate());
		vo.setPrice(entity.getPrice());
		vo.setTitle(entity.getTitle());
		
		return vo;
	}

}
