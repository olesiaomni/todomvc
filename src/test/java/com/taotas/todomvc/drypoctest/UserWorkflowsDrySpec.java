package com.taotas.todomvc.drypoctest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
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
        add("a", "b", "c");
        todosShouldBe("a", "b", "c");
        itemsLeftShouldBe(3);

        // Edit
        editTodoName("b"," edited").pressEnter();

        // Complete and Clear
        todo("b edited").find(".toggle").click();
        element("#clear-completed").click();
        todosShouldBe("a", "c");

        // Cancel Edit
        editTodoName("a","to be canceled").pressEscape();

        // Delete
        todo("a").hover().find(".destroy").click();
        elements(todos).shouldHaveSize(1);
        itemsLeftShouldBe(1);
    }

    private final ElementsCollection todos = elements("#todo-list>li");

    private SelenideElement todo(String value) {
        return elements(todos).findBy(exactText(value));
    }

    private void todosShouldBe(String... todos) {
        elements(this.todos).shouldHave(exactTexts(todos));
    }

    private void itemsLeftShouldBe(int todosCount) {
        String count = Integer.toString(todosCount);
        element("#todo-count>strong").shouldHave(exactText(count));
    }

    private SelenideElement editTodoName(String oldTodo, String textToAdd) {
        prepareTodoForEditing(oldTodo);
        return elements(todos).findBy(cssClass("editing")).find(".edit")
                .append(textToAdd);
    }

    private void prepareTodoForEditing(String todo) {
        todo(todo).doubleClick();
    }

    private void add(String... todos) {
        for(String todo: todos) {
            element("#new-todo").append(todo).pressEnter();
        }
    }
}
