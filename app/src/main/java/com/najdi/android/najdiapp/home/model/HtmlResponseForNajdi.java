package com.najdi.android.najdiapp.home.model;

import android.os.Build;
import android.text.Html;

public class HtmlResponseForNajdi {
    Title title;
    Content content;

    public Content getContent() {
        return content;
    }

    public Title getTitle() {
        return title;
    }

    public class Title {
        String rendered;

        public String getTitle() {
            return rendered;
        }
    }

    public class Content {
        String rendered;

        public String getContent() {
           return rendered;
        }

    }


}
