package com.tjg_project.candy.domain.notice.controller;

import com.tjg_project.candy.domain.notice.entity.Notice;
import com.tjg_project.candy.domain.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/all")
    public List<Notice> getNoticeList() {

        return noticeService.getNoticeList();
    }

}
