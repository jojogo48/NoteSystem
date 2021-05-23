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


    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE n.note_name like %:name% AND n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findByNameAndUid(@Param("name") String name,@Param("uid")Long uid);

    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id  WHERE binary n.note_name like %:name% AND n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findByBinaryNameAndUid(@Param("name") String name,@Param("uid")Long uid);

    @Query(value="SELECT *  FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE category_name=:name AND n.uid=:uid",nativeQuery = true)
    List<Note> findByCategoryAndUid(@Param("name") String name,@Param("uid")Long uid);

    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE category_name=:categoryName AND n.note_name like %:name% AND n.format=:format AND n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findByNameAndCategoryAndFormatAndUid(@Param("categoryName") String categoryName,@Param("name") String name,@Param("format") String format,@Param("uid")Long uid);

    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE  n.note_name like %:name% AND n.format=:format AND n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findByNameAndFormatAndUid(@Param("name") String name,@Param("format") String format,@Param("uid")Long uid);

    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE  category_name=:categoryName AND n.note_name like %:name%  AND n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findByNameAndCategoryAndUid(@Param("name") String name,@Param("categoryName") String categoryName,@Param("uid")Long uid);


    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE category_name=:categoryName AND n.format=:format AND binary n.note_name like %:name% AND n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findByBinaryNameAndCategoryAndFormatAndUid(@Param("categoryName") String categoryName,@Param("name") String name,@Param("format") String format,@Param("uid")Long uid);

    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE  n.format=:format AND n.uid=:uid AND binary n.note_name like %:name%",nativeQuery = true)
    List<Map<String,Object>> findByBinaryNameAndFormatAndUid(@Param("name") String name,@Param("format") String format,@Param("uid")Long uid);

    @Query(value="SELECT n.id,n.note_name,n.format,.n.description,c.category_name FROM Notes n LEFT JOIN Categories c ON n.category_id=c.id WHERE  category_name=:categoryName AND n.uid=:uid AND binary n.note_name like %:name% " ,nativeQuery = true)
    List<Map<String,Object>> findByBinaryNameAndCategoryAndUid(@Param("name") String name,@Param("categoryName") String categoryName,@Param("uid")Long uid);


    @Modifying
    @Transactional
    @Query(value="UPDATE Notes n SET n.category_id=:category_id WHERE n.id=:id AND n.uid=:uid",nativeQuery = true)
    int setNoteCategoryAndUid(@Param("id") String id,@Param("category_id") Long category_id,@Param("uid") Long uid);

    @Query(value="SELECT * FROM Notes n WHERE n.uid=:uid",nativeQuery = true)
    List<Map<String,Object>> findNoteByUid(@Param("uid")Long uid);


}
