package com.example.accessing_data_mysql.publications;

import org.springframework.data.repository.CrudRepository;

public interface PublicationRepository extends CrudRepository<Publication, Integer> {
	Iterable<Publication> findByAuthorId(Integer authorId);
}
