package ru.danmax.app.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.danmax.app.entity.Category;
import ru.danmax.app.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }


    public void create(String name) throws Exception {
        if (name == null || name.isEmpty()){
            throw new Exception("Category name cannot be empty");
        }

        if (categoryRepository.findByName(name).isPresent()) {
            throw new Exception("This category already present");
        }

        Category newCategory = Category.builder()
                .name(name)
                .build();

        categoryRepository.save(newCategory);
    }

    public void update(Long id, String newName) throws Exception {
        if (id == null){
            throw new Exception("Category id cannot be empty");
        }

        if (newName == null || newName.isEmpty()){
            throw new Exception("New category name cannot be empty");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));


        if (newName.equals(category.getName())) {
            throw new Exception("New category name cannot be identical to the old one");
        }

        if (categoryRepository.findByName(newName).isPresent()) {
            throw new Exception("This category already present");
        }

        category.setName(newName);
        categoryRepository.save(category);
    }

    public void delete(Long id) throws Exception {
        if (id == null){
            throw new Exception("Category id cannot be empty");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));

        categoryRepository.delete(category);
    }

    public Category getCategoryById(Long id) throws Exception{
        if (id == null){
            throw new Exception("Category id cannot be empty");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));
    }
}
