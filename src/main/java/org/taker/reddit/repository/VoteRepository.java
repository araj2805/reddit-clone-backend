package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taker.reddit.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
