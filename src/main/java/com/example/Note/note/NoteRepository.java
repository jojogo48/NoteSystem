package com.example.Note.note;

import com.example.Note.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.CollectionModel;

public interface NoteRepository extends JpaRepository<Note, Long> {


    @Query(value="SELECT * FROM Notes n WHERE n.notename like %:name%",nativeQuery = true)
    List<Note> findByName(@Param("name") String name);

    @Query(value="SELECT * FROM Notes n WHERE binary n.notename like %:name%",nativeQuery = true)
    List<Note> findByBinaryName(@Param("name") String name);

    @Query(value="SELECT * FROM notes LEFT JOIN noteclass ON notes.classname=noteclass.className",nativeQuery = true)
    List<Map<String,String>> joinTest();
}
