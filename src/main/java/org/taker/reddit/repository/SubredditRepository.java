package org.taker.reddit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taker.reddit.model.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
}
