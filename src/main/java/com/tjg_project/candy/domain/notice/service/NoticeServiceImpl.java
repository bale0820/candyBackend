package com.tjg_project.candy.domain.notice.service;

import com.tjg_project.candy.domain.notice.entity.Notice;
import com.tjg_project.candy.domain.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public List<Notice> getNoticeList() {
            return noticeRepository.findAll();
    }
}
