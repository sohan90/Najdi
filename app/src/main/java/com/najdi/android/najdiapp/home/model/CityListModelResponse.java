package com.najdi.android.najdiapp.home.model;

import java.util.List;

public class CityListModelResponse {
    boolean status;
    List<City> cities;
    List<Category> categories;


    public List<Category> getCategories() {
        return categories;
    }

    public List<City> getCities() {
        return cities;
    }

    public boolean isStatus() {
        return status;
    }

    public static class City {
        String id;
        String name;
        String active_status;
        String created_at;


        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getActive_status() {
            return active_status;
        }
    }

    public static class Category extends City{

    }
}
