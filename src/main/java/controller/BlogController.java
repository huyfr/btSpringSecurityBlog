package controller;

import model.Blog;
import model.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import service.BlogService;
import service.CategoryService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private static Logger logger = LogManager.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute("categories")
    public Iterable<Category> categories() {
        return categoryService.findAll();
    }

    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    public ModelAndView loadListBlog(Pageable pageable, @RequestParam("search") Optional<String> search) {
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
            listBlog = new ModelAndView("blog/list");
            listBlog.addObject("listBlog", blogPage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadListBlog()");
        return listBlog;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView loadCreateForm() {
        logger.trace("Go into loadCreateForm()");
        ModelAndView createForm = null;
        Blog blog;
        try {
            blog = new Blog();
            createForm = new ModelAndView("blog/create");
            createForm.addObject("blog", blog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadCreateForm()");
        return createForm;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createBlog(@ModelAttribute("blog") Blog blog, Pageable pageable, @RequestParam("search") Optional<String> search) {
        logger.trace("Go into createBlog()");
        ModelAndView created = null;
        LocalDateTime createdDate;
        try {
            if (blog.getCreatedDate() == null) {
                createdDate = LocalDateTime.now();
                blog.setCreatedDate(Timestamp.valueOf(createdDate));
            }
            blogService.save(blog);
            created = backToListBlog(1, loadListBlog(pageable, search));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit createBlog()");
        return created;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView loadEditForm(@PathVariable("id") Integer id) {
        logger.trace("Go into loadEditForm()");
        ModelAndView editForm = null;
        Blog blog;
        try {
            blog = blogService.findById(id);
            editForm = new ModelAndView("blog/edit");
            editForm.addObject("blog", blog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadEditForm()");
        return editForm;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView editBlog(@ModelAttribute("blog") Blog blog, Pageable pageable, @RequestParam("search") Optional<String> search) {
        logger.trace("Go into editBlog()");
        ModelAndView edited = null;
        try {
            logger.info(blog.toString());
            blogService.save(blog);
            edited = backToListBlog(2, loadListBlog(pageable, search));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit editBlog()");
        return edited;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView loadDeleteForm(@PathVariable("id") Integer id) {
        logger.trace("Go into loadDeleteForm()");
        ModelAndView deleteForm = null;
        Blog blog;
        try {
            blog = blogService.findById(id);
            logger.info(blog.toString());
            deleteForm = new ModelAndView("blog/delete");
            deleteForm.addObject("blog", blog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadDeleteForm()");
        return deleteForm;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteBlog(@ModelAttribute("blog") Blog blog, Pageable pageable, @RequestParam("search") Optional<String> search) {
        logger.trace("Go into deleted()");
        ModelAndView deleted = null;
        try {
            logger.info(blog.toString());
            blogService.delete(blog.getId());
            deleted = backToListBlog(3, loadListBlog(pageable, search));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit deleted()");
        return deleted;
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView loadViewDetail(@PathVariable("id") Integer id) {
        logger.trace("Go into loadViewDetail()");
        ModelAndView viewDetail = null;
        Blog blog;
        try {
            blog = blogService.findById(id);
            logger.info(blog.toString());
            viewDetail = new ModelAndView("blog/viewDetail");
            viewDetail.addObject("blog", blog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadViewDetail()");
        return viewDetail;
    }

    private ModelAndView backToListBlog(Integer type, ModelAndView modelAndView) {
        switch (type) {
            case 1:
                modelAndView.addObject("alert", "Them thanh cong");
                break;
            case 2:
                modelAndView.addObject("alert", "Sua thanh cong");
                break;
            case 3:
                modelAndView.addObject("alert", "Xoa thanh cong");
                break;
        }
        return modelAndView;
    }
}