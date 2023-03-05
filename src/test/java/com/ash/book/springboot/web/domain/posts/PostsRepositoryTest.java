package com.ash.book.springboot.web.domain.posts;

import com.ash.book.springboot.domain.posts.Posts;
import com.ash.book.springboot.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {
    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        String title = "테스트게시글";
        String contents = "테스트컨텐츠";
        String author = "ME";

        Posts posts = Posts.builder()
                .title(title)
                .content(contents)
                .author(author)
                .build();

        postsRepository.save(posts);

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts2 = postsList.get(0);
        assertThat(posts2.getTitle()).isEqualTo(title);
        assertThat(posts2.getContent()).isEqualTo(contents);
    }

    @Test
    public void BaseTimeEntity_등록() {
        //given
        LocalDateTime now = LocalDateTime.of(2023,3,5,0,0,0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()
        );

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        System.out.println(">>>>>> createDate=" + posts.getCreatedDate()+", modifiedDate=" + posts.getModifedDate());

        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifedDate()).isAfter(now);
    }
}
