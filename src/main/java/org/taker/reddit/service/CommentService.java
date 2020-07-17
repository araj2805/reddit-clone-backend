package org.taker.reddit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.taker.reddit.dto.CommentDto;
import org.taker.reddit.exception.PostNotFoundException;
import org.taker.reddit.mapper.CommentMapper;
import org.taker.reddit.model.Comment;
import org.taker.reddit.model.NotificationEmail;
import org.taker.reddit.model.Post;
import org.taker.reddit.model.User;
import org.taker.reddit.repository.CommentRepository;
import org.taker.reddit.repository.PostRepository;
import org.taker.reddit.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private AuthService authService;
    private CommentMapper commentMapper;
    private CommentRepository commentRepository;
    private MailContentBuilder mailContentBuilder;
    private MailService mailService;


    public void createComment(CommentDto commentDto) {

        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new PostNotFoundException("Post not found : " + commentDto.getPostId().toString()));

        Comment comment = commentMapper.commentDtoMapComment(commentDto, post, authService.getCurrentUser());

        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted on your post. ");
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " commented on your post", user.getEmail(), message));
    }

    public List<CommentDto> getALlCommentForPost(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found : " + postId.toString()));

        List<Comment> commentList = commentRepository.findByPost(post);

        return commentList.stream().map(commentMapper::commentMapToCommentDto).collect(Collectors.toList());
    }

    public List<CommentDto> getALlCommentForUser(String userName) {

        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("Username not found : " + userName));

        List<Comment> commentList = commentRepository.findByUser(user);

        return commentList.stream().map(commentMapper::commentMapToCommentDto).collect(Collectors.toList());
    }
}
