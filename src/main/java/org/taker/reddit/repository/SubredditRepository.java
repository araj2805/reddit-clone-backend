package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taker.reddit.model.Subreddit;

import java.util.Optional;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String subredditName);
}
