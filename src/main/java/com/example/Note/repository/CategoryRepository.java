package com.example.Note.repository;

import com.example.Note.entity.Category;
import com.example.Note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query(value="SELECT * FROM Categories c WHERE c.uid=:uid",nativeQuery = true)
    List<Category> findByUid(@Param("uid") Long uid);
    @Query(value="SELECT id FROM Categories c WHERE c.uid=:uid AND c.category_name='未分類'",nativeQuery = true)
    Long findUncategorizedIdByUid(@Param("uid") Long uid);

}
