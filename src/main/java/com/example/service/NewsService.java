package com.example.service;

import com.example.DAO.NewsDAO;
import com.example.model.News;
import com.example.util.DemoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by asus-Iabx on 2017/4/9.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId,int limit,int offset)
    {
        return newsDAO.selectByUserIdAndOffset(userId, limit, offset);
    }

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public String saveimage(MultipartFile file) throws IOException
    {
        int dotpos=file.getOriginalFilename().lastIndexOf(".");
        if(dotpos<0)
            return null;
        String fileext=file.getOriginalFilename().substring(dotpos+1).toLowerCase();
        if(!DemoUtil.isFileAllowed(fileext))
        {
            return null;
        }
        String filename= UUID.randomUUID().toString().replaceAll("-","")+"."+fileext;
        Files.copy(file.getInputStream(),new File(DemoUtil.FILEDIR+filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return DemoUtil.DEMO_DOMAIN+"image?name="+filename;
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        System.out.println(count);
        return newsDAO.updateLikeCount(id, count);
    }
}
