package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.post.application.AdminPostService;
import com.sungam1004.register.domain.post.application.AdminSavePostService;
import com.sungam1004.register.domain.post.dto.SavePostDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //MockMvc 사용
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminPostService postService;

    @Autowired
    private AdminSavePostService savePostService;

    @Test
    @DisplayName("Post 조회 - 1개 등록")
    void findPost1() throws Exception {
        // given
        SavePostDto post1 = new SavePostDto("title1", "content1", "2023-02-05",
                List.of(new SavePostDto.Question(1, "question1"),
                        new SavePostDto.Question(2, "question2")));
        savePostService.savePost(post1);

        System.out.println("=====================================");
        // expected
        mockMvc.perform(get("/api/users/posts/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(post1.getContent()))
                .andExpect(jsonPath("$[0].date").value(post1.getDate()))
                .andExpect(jsonPath("$[0].questions.size()").value(2))
                .andExpect(jsonPath("$[0].questions[0]").value(post1.getQuestions().get(0).getContent()))
                .andExpect(jsonPath("$[0].questions[1]").value(post1.getQuestions().get(1).getContent()))
                .andDo(print());
        System.out.println("=====================================");
    }

    @Test
    @DisplayName("Post 조회 - 6개 등록")
    void findPost6() throws Exception {
        // given
        IntStream.rangeClosed(1, 6).forEach(count -> {
            savePostService.savePost(new SavePostDto("title" + count, "content" + count, "2023-02-0" + count,
                    List.of(new SavePostDto.Question(1, "question1"),
                            new SavePostDto.Question(2, "question2"))));
        });

        System.out.println("=====================================");
        // expected
        mockMvc.perform(get("/api/users/posts/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$[0].date").value("2023-02-06"))
                .andExpect(jsonPath("$[1].date").value("2023-02-05"))
                .andExpect(jsonPath("$[2].date").value("2023-02-04"))
                .andExpect(jsonPath("$[3].date").value("2023-02-03"))
                .andExpect(jsonPath("$[4].date").value("2023-02-02"))
                .andDo(print());

        mockMvc.perform(get("/api/users/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].date").value("2023-02-01"))
                .andDo(print());
        System.out.println("=====================================");
    }

    @Test
    @DisplayName("Post 조회 - 12개 등록")
    void findPost12() throws Exception {
        // given
        IntStream.rangeClosed(1, 9).forEach(count -> {
            savePostService.savePost(new SavePostDto("title" + count, "content" + count, "2023-02-0" + count,
                    List.of(new SavePostDto.Question(1, "question1"),
                            new SavePostDto.Question(2, "question2"))));
        });
        IntStream.rangeClosed(10, 12).forEach(count -> {
            savePostService.savePost(new SavePostDto("title" + count, "content" + count, "2023-02-" + count,
                    List.of(new SavePostDto.Question(1, "question1"),
                            new SavePostDto.Question(2, "question2"))));
        });

        System.out.println("=====================================");
        // expected
        mockMvc.perform(get("/api/users/posts/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$[0].date").value("2023-02-12"))
                .andExpect(jsonPath("$[1].date").value("2023-02-11"))
                .andExpect(jsonPath("$[2].date").value("2023-02-10"))
                .andExpect(jsonPath("$[3].date").value("2023-02-09"))
                .andExpect(jsonPath("$[4].date").value("2023-02-08"))
                .andDo(print());

        mockMvc.perform(get("/api/users/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$[0].date").value("2023-02-07"))
                .andExpect(jsonPath("$[1].date").value("2023-02-06"))
                .andExpect(jsonPath("$[2].date").value("2023-02-05"))
                .andExpect(jsonPath("$[3].date").value("2023-02-04"))
                .andExpect(jsonPath("$[4].date").value("2023-02-03"))
                .andDo(print());

        mockMvc.perform(get("/api/users/posts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].date").value("2023-02-02"))
                .andExpect(jsonPath("$[1].date").value("2023-02-01"))
                .andDo(print());
        System.out.println("=====================================");
    }

}