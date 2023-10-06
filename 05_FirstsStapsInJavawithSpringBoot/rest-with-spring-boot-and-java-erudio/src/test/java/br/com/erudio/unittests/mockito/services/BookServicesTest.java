package br.com.erudio.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BooksRepository;
import br.com.erudio.services.BooksServices;
import br.com.erudio.unittests.mapper.mocks.MockBook;
import br.com.erudio.vo.v1.BookVO;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	@InjectMocks
	private BooksServices service;

	@Mock
	BooksRepository booksRepository;

	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);

		when(booksRepository.findById(1L)).thenReturn(Optional.of(entity));

		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("[</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		assertEquals("Some Title1", result.getTitle());
	}

	@Test
	void testCreate() {
		Book entity = input.mockEntity(1);

		Book persisted = entity;
		persisted.setId(1L);

		BookVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(booksRepository.save(entity)).thenReturn(persisted);

		var result = service.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("[</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		assertEquals("Some Title1", result.getTitle());
	}

	@Test
	void testCreateWithNullBook() {

		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});

		String expetedMessage = "it is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expetedMessage));

	}

	@Test
	void testDelete() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);

		when(booksRepository.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);
	}

	@Test
	void testUpdate() {
		Book entity = input.mockEntity(1);
		entity.setId(1L);

		Book persisted = entity;
		persisted.setId(1L);

		BookVO vo = input.mockVO(1);
		vo.setKey(1L);

		when(booksRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(booksRepository.save(entity)).thenReturn(persisted);

		var result = service.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("[</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(25D, result.getPrice());
		assertNotNull(result.getLaunchDate());
		assertEquals("Some Title1", result.getTitle());
	}

	@Test
	void testUpdateWithNullBook() {

		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expetedMessage = "it is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expetedMessage));
	}
}
