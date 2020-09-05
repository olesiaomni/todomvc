package com.taotas.todomvc.kisspoctest;

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

public class UserWorkflowsKissSpec {
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
        todosShouldBe("a", "b", "c");
        leftItemsShouldBe(3);

        // Edit
        startEditing("b", " edited").pressEnter();

        // Complete and Clear
        exactTodo("b edited").find(".toggle").click();
        element("#clear-completed").click();
        todosShouldBe("a", "c");

        // Cancel Edit
        startEditing("a", "to be canceled").pressEscape();

        // Delete
        exactTodo("a").hover().find(".destroy").click();
        todos.shouldHaveSize(1);
        leftItemsShouldBe(1);
    }

    ElementsCollection todos = elements("#todo-list>li");

    private SelenideElement exactTodo(String textToFindBy) {
        return todos.findBy(exactText(textToFindBy));
    }

    private SelenideElement startEditing(String oldTodo, String textToAdd) {
        exactTodo(oldTodo).doubleClick();
        return todos.findBy(cssClass("editing")).find(".edit")
                .append(textToAdd);
    }

    private void leftItemsShouldBe(int todosCount) {
        String count = Integer.toString(todosCount);
        element("#todo-count>strong").shouldHave(exactText(count));
    }

    private void todosShouldBe(String... todos) {
        elements("#todo-list>li").shouldHave(exactTexts(todos));
    }

    private void add(String todo) {
        element("#new-todo").append(todo).pressEnter();
    }
}
