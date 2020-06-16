package controller;

import model.Blog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import service.BlogService;

import java.util.Optional;

@Controller
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private BlogService blogService;

    public static String getPrincipal(){
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView loadListBlog(@RequestParam("search") Optional<String> search, Pageable pageable) {
        logger.trace("Go into loadListBlog()");
        ModelAndView listBlog = null;
        Page<Blog> blogPage;
        try {
            if (search.isPresent()) {
                blogPage = blogService.findAllByTitleContaining(search.get(), pageable);
            } else {
                blogPage = blogService.findAll(pageable);
            }
            logger.info(blogPage.getContent());
            listBlog = new ModelAndView("index");
            listBlog.addObject("listBlogIndex", blogPage);
            listBlog.addObject("user", getPrincipal());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadListBlog()");
        return listBlog;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin() {
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println(context.getAuthentication().getName());
        return "admin";
    }
}
