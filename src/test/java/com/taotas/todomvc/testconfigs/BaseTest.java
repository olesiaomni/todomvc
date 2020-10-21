package com.taotas.todomvc.testconfigs;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    {
        Configuration.browser = "chrome";
        Configuration.baseUrl = System.getProperty(
                "selenide.baseUrl","http://todomvc4tasj.herokuapp.com/");
        Configuration.fastSetValue = true;
    }
    /*
    @BeforeEach
    public void setup(){
        Configuration.browser = "chrome";
        Configuration.baseUrl = "http://todomvc4tasj.herokuapp.com/";
        Configuration.fastSetValue = true;
    }*/
}
