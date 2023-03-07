package br.com.erudio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.services.PersonServices;
import br.com.erudio.vo.v1.PersonVO;
import br.com.erudio.vo.v2.PersonVOV2;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonServices services;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonVO> findall() {
		return services.findAll();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO findById(@PathVariable(value = "id") Long id) {
		return services.findById(id);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO create(@RequestBody PersonVO person) {
		return services.create(person);
	}
	
	@PostMapping(value = "/v2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {
		return services.createV2(person);
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonVO update(@RequestBody PersonVO person) {
		return services.update(person);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.noContent().build();
	}
}
