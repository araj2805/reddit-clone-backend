package org.taker.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubredditDto {

    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;
}
