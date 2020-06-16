package service;

import model.Category;

public interface CategoryService {
    public Iterable<Category> findAll();

    public Category findById(Integer id);

    public void save(Category category);

    public void delete(Integer id);
}
