package com.taotas.todomvc.drypoctest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.element;

public class UserWorkflowsDrySpec {
    @Test
    public void basicUserWorkflowTest()  {
        Configuration.timeout = 6000;

        open("http://todomvc4tasj.herokuapp.com/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return $._data($('#clear-completed').get(0), 'events')" +
                        ".hasOwnProperty('click')"));

        // Add
        add("a");
        add("b");
        add("c");

        elements(todoList).shouldHave(exactTexts("a", "b", "c"));
        checkTodoListCount("3");

        // Edit
        prepareTaskForEditing("b");
        editTaskName(" edited").pressEnter();

        // Complete and Clear
        elements(todoList).findBy(exactText("b edited")).find(".toggle").click();
        element("#clear-completed").click();
        elements(todoList).shouldHave(exactTexts("a", "c"));

        // Cancel Edit
        prepareTaskForEditing("a");
        editTaskName("to be canceled").pressEscape();

        // Delete
        elements(todoList).findBy(exactText("a")).hover().find(".destroy").click();
        elements(todoList).shouldHaveSize(1);
        checkTodoListCount("1");
    }

    private void checkTodoListCount(String tasksCount) {
        element("#todo-count>strong").shouldHave(exactText(tasksCount));
    }

    private SelenideElement editTaskName(String newText) {
        return elements(todoList).findBy(cssClass("editing")).find(".edit")
                .append(newText);
    }

    private void prepareTaskForEditing(String taskName) {
        elements(todoList).findBy(exactText(taskName)).doubleClick();
    }

    private final String todoList = "#todo-list>li";

    private void add(String text) {
        element("#new-todo").append(text).pressEnter();
    }
}
