
package raaghulr.com.expensemanager.presenter;

import raaghulr.com.expensemanager.database.ExpenseDatabaseHelper;
import raaghulr.com.expensemanager.model.ExpenseType;
import raaghulr.com.expensemanager.view.AddCategoryView;

public class CategoryPresenter {
  private final AddCategoryView view;
  private final ExpenseDatabaseHelper database;

  public CategoryPresenter(AddCategoryView view, ExpenseDatabaseHelper database) {
    this.view = view;
    this.database = database;
  }

  public boolean addCategory() {
    String newCategory = view.getCategory();
    if(newCategory.isEmpty()){
      view.displayError();
      return false;
    }

    database.addExpenseType(new ExpenseType(newCategory));
    return true;
  }
}
