package com.sungam1004.register.domain.post.application;

import com.sungam1004.register.domain.post.dto.PostDetailDto;
import com.sungam1004.register.domain.post.dto.PostResponseDto;
import com.sungam1004.register.domain.post.dto.PostSummaryDto;
import com.sungam1004.register.domain.post.entity.Post;
import com.sungam1004.register.domain.post.exception.PostNotFoundException;
import com.sungam1004.register.domain.post.repository.PostRepository;
import com.sungam1004.register.global.manager.SundayDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFindPostService {
    private final PostRepository postRepository;

    public List<PostSummaryDto> findPostSummaryDtoList() {
        final String sortProperty = "date";
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.ASC, sortProperty));

        List<LocalDate> sundayDates = SundayDate.dates.stream()
                .map(date -> LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .toList();

        return createPostSummaryDtoList(posts, sundayDates);
    }

    private List<PostSummaryDto> createPostSummaryDtoList(List<Post> posts, List<LocalDate> sundayDates) {
        List<PostSummaryDto> postSummaryDtoList = new ArrayList<>();
        int searchIndex = 0;
        for (LocalDate sundayDate : sundayDates) {
            PostSummaryDto postSummaryDto;

            if (searchIndex >= posts.size()) {
                postSummaryDto = PostSummaryDto.createFromNotExistPost(sundayDate);
            }
            else {
                Post post = posts.get(searchIndex);

                if (isEqualPostToDate(post, sundayDate)) {
                    postSummaryDto = PostSummaryDto.createFromExistPost(post.getId(), post.getTitle(), sundayDate);
                    searchIndex++;
                }
                else {
                    postSummaryDto = PostSummaryDto.createFromNotExistPost(sundayDate);
                }
            }

            postSummaryDtoList.add(postSummaryDto);
        }
        return postSummaryDtoList;
    }

    private boolean isEqualPostToDate(Post post, LocalDate sundayDate) {
        return post.getDate().equals(sundayDate);
    }

    public PostDetailDto postDetail(Long postId) {
        Post post = postRepository.findWithQuestionsById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostDetailDto.createFromPost(post);
    }

    public List<PostResponseDto> findPostUsingPage(int page) {
        final int dataNumberPerPage = 5;
        final String sortProperty = "date";

        Pageable pageable = PageRequest.of(page, dataNumberPerPage, Sort.by(Sort.Direction.DESC, sortProperty));
        return postRepository.findSliceBy(pageable).stream()
                .map(PostResponseDto::of)
                .collect(Collectors.toList());
    }
}
