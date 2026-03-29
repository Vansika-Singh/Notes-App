package com.vansika.notes_app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/*
   WHAT THIS FILE IS:
   The repository layer — responsible for all database operations on the Note table.
   By extending JpaRepository, Spring gives us save(), findAll(), findById(),
   deleteById() and more for free without writing any SQL.

   JpaRepository<Note, Long> means:
   - Note  = the Entity (table) we are working with
   - Long  = the data type of the primary key (our id field)
*/
public interface NoteRepository extends JpaRepository<Note, Long> {

    /*
       SEARCH METHOD:
       This uses a custom JPQL query (Java Persistence Query Language).
       JPQL looks like SQL but works on Java class names and field names,
       not table names and column names.

       :keyword is a named parameter — Spring replaces it with the actual
       value passed into the method at runtime.

       LOWER() makes the search case-insensitive so searching "hello"
       also finds notes with "Hello" or "HELLO" in the title or content.

       We use @Query here instead of method name derivation because
       the logic (search across two fields) is too complex for method naming.
    */
    @Query("SELECT n FROM Note n WHERE " +
            "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Note> searchNotes(@Param("keyword") String keyword);
}