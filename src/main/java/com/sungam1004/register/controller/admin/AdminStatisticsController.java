package com.sungam1004.register.controller.admin;

import com.sungam1004.register.service.AdminExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/statistics")
@Slf4j
public class AdminStatisticsController {

    private final AdminExcelService adminExcelService;

    @Value("${file.path}")
    public String filePath;

    @GetMapping
    public ResponseEntity<UrlResource> statisticsForm() throws MalformedURLException {
        String fileName = adminExcelService.statistics();
        UrlResource resource = new UrlResource("file:" + filePath + "/" + fileName);

        //아래 문자를 ResponseHeader에 넣어줘야 한다. 그래야 링크를 눌렀을 때 다운이 된다.
        //정해진 규칙이다.
        String contentDisposition = "attachment; filename=\"" + fileName + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}

