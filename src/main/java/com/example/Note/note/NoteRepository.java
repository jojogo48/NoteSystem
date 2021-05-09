package com.example.Note.note;

import com.example.Note.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.CollectionModel;
import org.springframework.transaction.annotation.Transactional;

public interface NoteRepository extends JpaRepository<Note, Long> {


    @Query(value="SELECT * FROM Notes n WHERE n.note_name like %:name%",nativeQuery = true)
    List<Note> findByName(@Param("name") String name);

    @Query(value="SELECT * FROM Notes n WHERE binary n.note_name like %:name%",nativeQuery = true)
    List<Note> findByBinaryName(@Param("name") String name);

    @Query(value="SELECT * FROM Notes n WHERE n.category_name=:name",nativeQuery = true)
    List<Note> findByCategory(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value="UPDATE Notes n SET n.category_name=:name WHERE n.id=:id",nativeQuery = true)
    int setNoteCategory(@Param("id") String id,@Param("name") String name);

    @Query(value="SELECT * FROM notes LEFT JOIN categories ON notes.category_name=categories.name ",nativeQuery = true)
    List<Map<String,String>> joinTest();
}
