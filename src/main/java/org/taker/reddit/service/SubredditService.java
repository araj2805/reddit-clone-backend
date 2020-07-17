package org.taker.reddit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taker.reddit.dto.SubredditDto;
import org.taker.reddit.exception.SpringRedditException;
import org.taker.reddit.mapper.SubredditMapper;
import org.taker.reddit.model.Subreddit;
import org.taker.reddit.repository.SubredditRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubredditService {

    @Autowired
    SubredditRepository subredditRepository;

    @Autowired
    SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto saveSubreddit(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No Subreddit found with this id"));

        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
