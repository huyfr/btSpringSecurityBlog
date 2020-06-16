package controller;


import model.Blog;
import model.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import service.BlogService;
import service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private static Logger logger = LogManager.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    public ModelAndView loadListCategory() {
        logger.trace("Go into loadListCategory()");
        ModelAndView listCategory = null;
        Iterable<Category> categories;
        try {
            categories = categoryService.findAll();
            logger.info(categories.toString());
            listCategory = new ModelAndView("category/list");
            listCategory.addObject("categories", categories);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadListCategory()");
        return listCategory;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView loadCreateForm() {
        logger.trace("Go into loadCreateForm()");
        ModelAndView createForm = null;
        Category category;
        try {
            category = new Category();
            createForm = new ModelAndView("category/create");
            createForm.addObject("category", category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadCreateForm()");
        return createForm;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createCategory(@ModelAttribute("category") Category category) {
        logger.trace("Go into createCategory()");
        ModelAndView created = null;
        try {
            categoryService.save(category);
            logger.info(category.toString());
//            created = new ModelAndView("category/create");
            created = backToListCategory(1, loadListCategory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit createCategory()");
        return created;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView loadEditForm(@PathVariable("id") Integer id) {
        logger.trace("Go into loadEditForm()");
        ModelAndView editForm = null;
        Category category;
        try {
            category = categoryService.findById(id);
            editForm = new ModelAndView("category/edit");
            editForm.addObject("category", category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadEditForm()");
        return editForm;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView editCategory(@ModelAttribute("category") Category category) {
        logger.trace("Go into editCategory()");
        ModelAndView edited = null;
        try {
            categoryService.save(category);
//            edited = new ModelAndView("category/edit");
            edited = backToListCategory(2, loadListCategory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit editCategory()");
        return edited;
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView loadViewDetail(@PathVariable("id") Integer id) {
        logger.trace("Go into loadViewDetail()");
        ModelAndView viewDetail = null;
        Category category;
        Iterable<Blog> listBlog;
        try {
            category = categoryService.findById(id);

            if (category == null) {
                viewDetail = new ModelAndView("error404");
                return viewDetail;
            }
            logger.info(category.toString());

            listBlog = blogService.findAllByCategoryByCategoryId(category);
            logger.info(listBlog.toString());

            viewDetail = new ModelAndView("category/viewDetail");
            viewDetail.addObject("category", category);
            viewDetail.addObject("listBlog", listBlog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadViewDetail()");
        return viewDetail;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView loadDeleteForm(@PathVariable("id") Integer id) {
        logger.trace("Go into loadDeleteForm()");
        ModelAndView deleteForm = null;
        Category category;
        try {
            category = categoryService.findById(id);
            deleteForm = new ModelAndView("category/delete");
            deleteForm.addObject("category", category);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit loadDeleteForm()");
        return deleteForm;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deleteCategory(@ModelAttribute("category") Category category) {
        logger.trace("Go into deleteCategory()");
        ModelAndView deleted = null;
        try {
            categoryService.delete(category.getId());
//            deleted = new ModelAndView("category/delete");
            deleted = backToListCategory(3, loadListCategory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.trace("Exit deleteCategory()");
        return deleted;
    }

    private ModelAndView backToListCategory(Integer type, ModelAndView modelAndView) {
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
