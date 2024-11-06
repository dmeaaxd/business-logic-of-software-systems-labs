package ru.danmax.app.delegates.category;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import ru.danmax.app.service.CategoryService;

@Named("updateCategory")
@RequiredArgsConstructor
public class UpdateCategoryDelegate implements JavaDelegate {
    private final CategoryService categoryService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long id = (Long) delegateExecution.getVariable("category_id");
        String name = (String) delegateExecution.getVariable("category_name");
        categoryService.update(id, name);
    }
}
