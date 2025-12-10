package org.example.repository;

import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.title = :newTitle WHERE b.id = :id")
    int updateTitleById(@Param("id") Long id, @Param("newTitle") String newTitle);
}
