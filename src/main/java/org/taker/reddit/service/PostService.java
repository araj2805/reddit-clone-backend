package org.taker.reddit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.taker.reddit.dto.PostRequest;
import org.taker.reddit.dto.PostResponse;
import org.taker.reddit.exception.PostNotFoundException;
import org.taker.reddit.exception.SubredditNotFoundException;
import org.taker.reddit.mapper.PostMapper;
import org.taker.reddit.model.Post;
import org.taker.reddit.model.Subreddit;
import org.taker.reddit.model.User;
import org.taker.reddit.repository.PostRepository;
import org.taker.reddit.repository.SubredditRepository;
import org.taker.reddit.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {


    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public void savePost(PostRequest postRequest) {

        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName()).orElseThrow(() -> new SubredditNotFoundException("SUbreddit not found : " + postRequest.getSubredditName()));

        User user = authService.getCurrentUser();

        Post post = postMapper.mapDtoToPost(postRequest, subreddit, user);

        postRepository.save(post);
    }


    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found : " + id.toString()));

        PostResponse postResponse = postMapper.mapPostToDto(post);
        return postResponse;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPost() {

        List<Post> postList = postRepository.findAll();

        List<PostResponse> postResponseList = postList.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());
        return postResponseList;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SubredditNotFoundException("Subreddit not found : " + id.toString()));

        List<Post> postList = postRepository.findAllBySubreddit(subreddit);

        List<PostResponse> postResponseList = postList.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());

        return postResponseList;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not Found : " + username));

        List<Post> postList = postRepository.findAllByUser(user);

        List<PostResponse> postResponseList = postList.stream().map(postMapper::mapPostToDto).collect(Collectors.toList());

        return postResponseList;
    }
}
