package org.taker.reddit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taker.reddit.dto.SubredditDto;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {

    public void createSubreddit(@RequestBody SubredditDto subredditDto) {

    }
}
