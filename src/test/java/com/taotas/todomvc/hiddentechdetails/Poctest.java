package com.taotas.todomvc.hiddentechdetails;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.taotas.todomvc.testconfigs.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.element;

public class Poctest extends BaseTest {

    @Test
    public void basicUserWorkflowTest()  {

        openTodoMvc();

        add("a", "b", "c");
        todosShouldBe("a", "b", "c");
        leftItemsShouldBe(3);

        edit("b", "b edited");

        toggle("b edited");
        checkNonCompletedTodos("a", "c");

        cancelEditing("a", "a to be canceled");

        delete("a");
        todos.shouldHaveSize(1);
        leftItemsShouldBe(1);
    }

    private ElementsCollection todos = elements("#todo-list>li");

    private SelenideElement exactTodo(String textToFindBy) {
        return todos.findBy(exactText(textToFindBy));
    }

    private SelenideElement startEditing(String oldTodo, String textToAdd) {
        exactTodo(oldTodo).doubleClick();
        return todos.findBy(cssClass("editing")).find(".edit")
                .setValue(textToAdd);
    }

    private void openTodoMvc(){
        open("/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return $._data($('#clear-completed').get(0), 'events')" +
                        ".hasOwnProperty('click')"));
    }

    private void checkNonCompletedTodos(String... todos){
        element("#clear-completed").click();
        todosShouldBe(todos);
    }

    private void edit(String oldTodo, String textToAdd){
        startEditing(oldTodo, textToAdd).pressEnter();
    }

    private void cancelEditing(String oldTodo, String textToAdd){
        startEditing(oldTodo, textToAdd).pressEscape();
    }

    private void delete(String todo){
        exactTodo(todo).hover().find(".destroy").click();
    }

    private void toggle(String todo){
        exactTodo(todo).find(".toggle").click();
    }

    private void leftItemsShouldBe(int todosCount) {
        String count = Integer.toString(todosCount);
        element("#todo-count>strong").shouldHave(exactText(count));
    }

    private void todosShouldBe(String... todos) {
        elements("#todo-list>li").shouldHave(exactTexts(todos));
    }

    private void add(String... todos) {
        for (String text : todos) {
            element("#new-todo").setValue(text).pressEnter();
        }
    }
}
