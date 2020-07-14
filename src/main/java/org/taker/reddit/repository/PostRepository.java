package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.taker.reddit.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
