package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taker.reddit.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
