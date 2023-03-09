package com.ash.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * myBatis의 DAO역할(DB Layer)
 * 인터페이스 생성 후 JpaRepsotiry<Entity 클래스, PK타입> 상속
 * 주의 : Entity클래스와 기본 Repository는 함께 위치해야함(밀접한 관계)
 *
**/
public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
