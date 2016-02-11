
package raaghulr.com.expensemanager.view;

import java.util.List;

import raaghulr.com.expensemanager.model.Expense;

public interface TodaysExpenseView {
  void displayTotalExpense(Long totalExpense);
  void displayTodaysExpenses(List<Expense> expenses);
}
