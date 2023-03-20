package br.com.erudio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.erudio.model.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long>{}
