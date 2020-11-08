package com.taotas.todomvc.testconfigs;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.Selenide.open;

public class WithClearedStorageAfterEachTest extends BaseTest{
    @BeforeEach
    public void loadApp() {
        openTodoMvc();
    }

    @AfterEach
    public void clearData() {
        Selenide.executeJavaScript("localStorage.clear()");
    }

    private void openTodoMvc(){
        open("/");
        Selenide.Wait().until(ExpectedConditions.jsReturnsValue(
                "return $._data($('#clear-completed').get(0), 'events')" +
                        ".hasOwnProperty('click')"));
    }
}
