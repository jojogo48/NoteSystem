package com.example.Note.repository;

import com.example.Note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NoteRepository extends JpaRepository<Note, Long> {


    @Query(value="SELECT * FROM Notes n WHERE n.note_name like %:name% AND n.uid=:uid",nativeQuery = true)
    List<Note> findByNameAndUid(@Param("name") String name,@Param("uid")Long uid);

    @Query(value="SELECT * FROM Notes n WHERE binary n.note_name like %:name% AND n.uid=:uid",nativeQuery = true)
    List<Note> findByBinaryNameAndUid(@Param("name") String name,@Param("uid")Long uid);

    @Query(value="SELECT * FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE category_name=:name AND n.uid=:uid",nativeQuery = true)
    List<Note> findByCategoryAndUid(@Param("name") String name,@Param("uid")Long uid);

    @Query(value="SELECT * FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE category_name=:categoryName AND n.note_name like %:name% AND n.format=:format AND n.uid=:uid",nativeQuery = true)
    List<Note> findByNameAndCategoryAndFormatAndUid(@Param("categoryName") String categoryName,@Param("name") String name,@Param("format") String format,@Param("uid")Long uid);
    @Query(value="SELECT * FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE category_name=:categoryName AND n.format=:format AND binary n.note_name like %:name% AND n.uid=:uid",nativeQuery = true)
    List<Note> findByBinaryNameAndCategoryAndFormatAndUid(@Param("categoryName") String categoryName,@Param("name") String name,@Param("format") String format,@Param("uid")Long uid);
    @Modifying
    @Transactional
    @Query(value="UPDATE Notes n SET n.category_id=:category_id WHERE n.id=:id AND n.uid=:uid",nativeQuery = true)
    int setNoteCategoryAndUid(@Param("id") String id,@Param("category_id") Long category_id,@Param("uid") Long uid);

    @Query(value="SELECT * FROM Notes n WHERE n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findNoteByUid(@Param("uid")Long uid);


}
