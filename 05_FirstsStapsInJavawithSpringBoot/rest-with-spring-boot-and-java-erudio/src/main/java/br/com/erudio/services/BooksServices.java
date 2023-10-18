package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.BooksController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.BookMepper;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BooksRepository;

@Service
public class BooksServices {

	private Logger logger = Logger.getLogger(BooksServices.class.getName());
	
	@Autowired
	BooksRepository booksRepository;
	
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

		logger.info("Finding all books!");
		
		var booksPage = booksRepository.findAll(pageable);
		
		var bookVosPage = booksPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
		bookVosPage.map(p -> p.add(linkTo(methodOn(BooksController.class).findById(p.getKey())).withSelfRel()));

		Link link = linkTo(methodOn(BooksController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(bookVosPage, link);
	}
	
	public PagedModel<EntityModel<BookVO>> findBookByTitle(String title, Pageable pageable) {

		logger.info("Finding all Book by title!");
		
		var bookPage = booksRepository.findBookByTitle(title, pageable);
		
		var BookVosPage = bookPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
		BookVosPage.map(p -> p.add(linkTo(methodOn(BooksController.class).findById(p.getKey())).withSelfRel()));

		Link link = linkTo(methodOn(BooksController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(BookVosPage, link);
	}

	public BookVO findById(Long id) {

		logger.info("Finding one book!");

		var entity = booksRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		var vo = BookMepper.convertEntityToVO(entity);

		vo.add(linkTo(methodOn(BooksController.class).findById(id)).withSelfRel());

		return vo;
	}

	public BookVO create(BookVO book) {

		if (book == null) throw new RequiredObjectIsNullException();

		logger.info("Creating one book!");

		var entity = DozerMapper.parseObject(book, Book.class);
		entity = booksRepository.save(entity);
		var vo = DozerMapper.parseObject(entity, BookVO.class);

		vo.add(linkTo(methodOn(BooksController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public BookVO update(BookVO book) {

		if (book == null) throw new RequiredObjectIsNullException();

		logger.info("Updating one book!");

		var entity = booksRepository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		var vo = new BookVO();
		
		try {
			vo = BookMepper.convertEntityToVO(booksRepository.save(entity));
		} catch (Exception e) {
			logger.info("Deleting one book!");
		}

		

		vo.add(linkTo(methodOn(BooksController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long key) {
		logger.info("Deleting one book!");

		var entity = booksRepository.findById(key).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		booksRepository.delete(entity);
	}

}
