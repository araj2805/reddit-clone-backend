package org.taker.reddit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.taker.reddit.dto.CommentDto;
import org.taker.reddit.model.Comment;
import org.taker.reddit.model.Post;
import org.taker.reddit.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment commentDtoMapComment(CommentDto commentDto, Post post, User user);


    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "createdTime", source = "comment.createdDate")
    CommentDto commentMapToCommentDto(Comment comment);
}
