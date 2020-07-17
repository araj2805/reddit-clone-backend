package org.taker.reddit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.taker.reddit.dto.PostRequest;
import org.taker.reddit.dto.PostResponse;
import org.taker.reddit.model.Post;
import org.taker.reddit.model.Subreddit;
import org.taker.reddit.model.User;
import org.taker.reddit.repository.CommentRepository;
import org.taker.reddit.repository.VoteRepository;
import org.taker.reddit.service.AuthService;

import java.time.ZoneId;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AuthService authService;

    @Mapping(target = "postId", source = "postRequest.postId")
    @Mapping(target = "postName", source = "postRequest.postName")
    @Mapping(target = "url", source = "postRequest.url")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post mapDtoToPost(PostRequest postRequest, Subreddit subreddit, User user);


    @Mapping(target = "id", source = "postId")
    @Mapping(target = "postName", source = "postName")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapPostToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
