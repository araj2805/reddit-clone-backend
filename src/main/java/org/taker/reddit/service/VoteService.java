package org.taker.reddit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.taker.reddit.dto.VoteDto;
import org.taker.reddit.exception.PostNotFoundException;
import org.taker.reddit.exception.SpringRedditException;
import org.taker.reddit.model.Post;
import org.taker.reddit.model.Vote;
import org.taker.reddit.repository.PostRepository;
import org.taker.reddit.repository.VoteRepository;

import java.util.Optional;

import static org.taker.reddit.model.VoteType.UPVOTE;

@Service
@Slf4j
@AllArgsConstructor
public class VoteService {

    private PostRepository postRepository;
    private VoteRepository voteRepository;
    private AuthService authService;

    public void vote(VoteDto voteDto) {

        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new PostNotFoundException("Post not found with ID : " + voteDto.getPostId().toString()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, post.getUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType()))
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "D for this post");

        if (UPVOTE.equals(voteDto.getVoteType()))
            post.setVoteCount(post.getVoteCount() + 1);
        else
            post.setVoteCount(post.getVoteCount() - 1);

        voteRepository.save(voteDtoMapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote voteDtoMapToVote(VoteDto voteDto, Post post) {
        return Vote.builder().post(post).voteType(voteDto.getVoteType()).user(authService.getCurrentUser()).build();
    }
}
