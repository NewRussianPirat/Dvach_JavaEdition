package com.example.dvach_javaedition.repository;

import com.example.dvach_javaedition.model.Post;
import com.example.dvach_javaedition.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            (SELECT * FROM (SELECT * FROM Post p WHERE p.topic_id = :topicID ORDER BY datetime ASC LIMIT 1) AS FIRST)
            UNION ALL
            (SELECT * FROM (SELECT * FROM Post p WHERE p.topic_id = :topicID ORDER BY datetime DESC LIMIT 3) AS LAST)
            ORDER BY datetime ASC
            """,
            nativeQuery = true)
    List<Post> findFirstAndThreeLastPosts(@Param("topicID") Long topicID);
    List<Post> findAllByTopicOrderByDatetimeAsc(Topic topic);
    Long countByTopic(Topic topic);
}
