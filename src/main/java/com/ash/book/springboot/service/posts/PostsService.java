package com.ash.book.springboot.service.posts;

import com.ash.book.springboot.domain.posts.Posts;
import com.ash.book.springboot.domain.posts.PostsRepository;
import com.ash.book.springboot.web.dto.PostsResponseDto;
import com.ash.book.springboot.web.dto.PostsSaveRequestDto;
import com.ash.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    /**
     * update
     * 쿼리 날리는 부분이 없음. JPA의 영속성 컨텍스트를 사용.
     * JPA 영속성 컨텍스트는 엔티티를 영구 저장하는 환경(논리적 개념)
     * JPA의 엔티티매니저가 활성화된 상태에서 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된 상태.
     * 이 상태에서 해당데이터 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 데이터 변경분을 반영(더티 체킹)
     * @param id
     * @return
     */
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }
}
