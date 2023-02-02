package com.sungam1004.register.domain.api;

import com.sungam1004.register.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class ImageApi {

    private final ImageService imageService;

    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) {
        // <img src="/images/[파일이름]">
        // th:src="@{${item.getImgPath()}}"
        return imageService.getImageFile(filename);
    }

}
