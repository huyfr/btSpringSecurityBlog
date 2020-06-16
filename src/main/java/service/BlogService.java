package service;

import model.Blog;
import model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    public Page<Blog> findAll(Pageable pageable);

    public Blog findById(Integer id);

    public void save(Blog blog);

    public void delete(Integer id);

    public Iterable<Blog> findAllByCategoryByCategoryId(Category category);

    public Page<Blog> findAllByTitleContaining(String title, Pageable pageable);
}
