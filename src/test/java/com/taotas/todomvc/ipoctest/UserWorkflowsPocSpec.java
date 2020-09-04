package com.taotas.todomvc.ipoctest;
import com.codeborne.selenide.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class UserWorkflowsPocSpec {
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
        elements("#todo-list>li").shouldHave(exactTexts("a", "b", "c"));
        element("#todo-count>strong").shouldHave(exactText("3"));

        // Edit
        elements("#todo-list>li").findBy(exactText("b")).doubleClick();
        elements("#todo-list>li").findBy(cssClass("editing"))
                .find(".edit").append(" edited").pressEnter();

        // Complete and Clear
        elements("#todo-list>li").findBy(exactText("b edited")).find(".toggle").click();
        element("#clear-completed").click();
        elements("#todo-list>li").shouldHave(exactTexts("a", "c"));

        // Cancel Edit
        elements("#todo-list>li").findBy(exactText("a")).doubleClick();
        elements("#todo-list>li").findBy(cssClass("editing")).find(".edit")
                .append("to be canceled").pressEscape();

        // Delete
        elements("#todo-list>li").findBy(exactText("a")).hover().find(".destroy").click();
        elements("#todo-list>li").shouldHaveSize(1);
        element("#todo-count>strong").shouldHave(exactText("1"));
    }

    private void add(String text) {
            element("#new-todo").append(text).pressEnter();
    }
}
