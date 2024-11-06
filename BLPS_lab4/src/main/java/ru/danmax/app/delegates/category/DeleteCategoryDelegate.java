package ru.danmax.app.delegates.category;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.CategoryService;

@Named("deleteCategory")
@RequiredArgsConstructor
public class DeleteCategoryDelegate implements JavaDelegate {
    private final CategoryService categoryService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = (Long) delegateExecution.getVariable("category_id");
        categoryService.delete(id);
    }
}
