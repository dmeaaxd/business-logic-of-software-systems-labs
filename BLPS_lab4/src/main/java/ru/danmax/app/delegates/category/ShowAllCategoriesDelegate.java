package ru.danmax.app.delegates.category;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.entity.Category;
import ru.danmax.app.service.CategoryService;

import java.util.List;

@Named("showAllCategories")
@RequiredArgsConstructor
public class ShowAllCategoriesDelegate implements JavaDelegate {
    private final CategoryService categoryService;
    private final String TAB = " ";

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<Category> categories = categoryService.getAll();

        delegateExecution.setVariable("categories_count", categories.size());
        delegateExecution.setVariable("categories", convertCategoriesToJson(categories));
    }

    private String convertCategoriesToJson(List<Category> categories){
        StringBuilder categoriesJson = new StringBuilder("[\n");
        int index = 0;
        for (Category category : categories) {
            categoriesJson.append(addLeftTabToAllLine(convertCategoryToJson(category)));
            index += 1;
            if (index != categories.size()){
                categoriesJson.append(",\n");
            }
            else{
                categoriesJson.append("\n");
            }
        }
        categoriesJson.append("]");

        return categoriesJson.toString();
    }

    private String convertCategoryToJson(Category category){
        return String.format("""
                {
                %s "category_id": %d,
                %s "category_name": %s
                }""",
                TAB, category.getId(),
                TAB, category.getName());
    }

    private String addLeftTabToAllLine(String string){
        String[] lines = string.split("/n");
        String result = "";

        int index = 0;
        for (String line : lines){
            index += 1;
            if (index != lines.length){
                result = TAB + line + "/n";
            }
            else {
                result = TAB + line;
            }
        }
        return result;
    }
}
