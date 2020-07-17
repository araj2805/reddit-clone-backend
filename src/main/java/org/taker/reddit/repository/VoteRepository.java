package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taker.reddit.model.Post;
import org.taker.reddit.model.User;
import org.taker.reddit.model.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User user);


}
