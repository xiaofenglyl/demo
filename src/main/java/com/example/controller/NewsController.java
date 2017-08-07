package com.example.controller;

import com.example.model.*;
import com.example.service.*;
import com.example.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asus-Iabx on 2017/4/19.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AliyunService aliyunService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void setimage(@RequestParam("name") String imagename,
                         HttpServletResponse response)
    {
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(new File(DemoUtil.FILEDIR + imagename)), response.getOutputStream());
        }catch (Exception e)
        {
            logger.error("读取图片错误"+e.getMessage());
        }
    }




    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadimage(@RequestParam("file") MultipartFile file)
    {
        try{
            //String fileURL=newsService.saveimage(file);
            String fileURL=aliyunService.saveimage(file);
            if(fileURL == null)
            {
                return DemoUtil.getJSONString(1,"图片上传失败");
            }
            return DemoUtil.getJSONString(0,fileURL);

        }catch (Exception e)
        {
            logger.error("上传图片失败" + e.getMessage());
            return DemoUtil.getJSONString(1,"上传失败");
        }
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }



    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId,Model model )
    {
        try {
            News news = newsService.getById(newsId);

            if (news != null) {

                int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
                if (localUserId != 0) {
                    model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                    model.addAttribute("likec", likeService.getLikeStatus(localUserId, EntityType.ENTITY_COMMENT, news.getId()));
                    //System.out.println(likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
                } else {
                    model.addAttribute("like", 0);
                    model.addAttribute("likec", 0);
                }


                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                for (Comment comment : comments) {
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment", comment);
                    commentVO.set("user", userService.getuser(comment.getUserId()));
                    commentVOs.add(commentVO);
                }
                model.addAttribute("comments", commentVOs);
            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getuser(news.getUserId()));
        } catch (Exception e) {
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(path = {"/user/addNews"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("link") String link,
                          @RequestParam("title") String title)
    {
        try{
            News news = new News();
            if(hostHolder.getUser() != null)
            {
                news.setUserId(hostHolder.getUser().getId());
            }
            else{
                //匿名Id
                news.setUserId(3);
            }
            news.setLink(link);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            newsService.addNews(news);
            return DemoUtil.getJSONString(0);

        }catch (Exception e)
        {
            return DemoUtil.getJSONString(1,"发布失败");
        }

    }
}
