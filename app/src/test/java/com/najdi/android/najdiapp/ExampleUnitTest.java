package com.najdi.android.najdiapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String app = "attribute_pa_size_slug";
        if(app.endsWith("_slug")){
          String pa_key =  app.replace("attribute_","");
          String finalResult = pa_key.replace("_slug","");
        }
    }
}