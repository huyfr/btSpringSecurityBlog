package repository;

import model.Blog;
import model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlogRepository extends PagingAndSortingRepository<Blog, Integer> {
    public Iterable<Blog> findAllByCategoryByCategoryId(Category category);

    public Page<Blog> findAllByTitleContaining(String title, Pageable pageable);
}