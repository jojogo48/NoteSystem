package com.example.Note.repository;


import com.example.Note.entity.Note;
import com.example.Note.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value="SELECT * FROM Users u WHERE u.user_name=:username AND u.password=:password",nativeQuery = true)
    User findByName(@Param("username") String username,@Param("password") String password);
}
