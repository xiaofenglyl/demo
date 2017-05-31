package com.example.service;

import com.example.DAO.CommentDAO;
import com.example.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by asus-Iabx on 2017/5/14.
 */
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(AliyunService.class);

    @Autowired
    CommentDAO commentDAO;

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }

    public Comment getCommentByEntityId(int entityId,int entityType)
    {
        return commentDAO.selectByEntityId(entityId,entityType);
    }
    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }
}
