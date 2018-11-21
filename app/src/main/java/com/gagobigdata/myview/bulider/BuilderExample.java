package com.gagobigdata.myview.bulider;

/**
 * Created by llx on 2018/3/27.
 */

public class BuilderExample {

    private final String name;
    private final String time;
    private final String weather;
    private final String address;

    public static class Builder{
        private final String name;
        private final String time;

        private String weather;
        private String address;

        public Builder(String name, String time) {
            this.name = name;
            this.time = time;
        }

        public Builder weather(String weather){
            this.weather = weather;
            return this;
        }

        public Builder address(String address){
            this.address = address;
            return this;
        }

        public BuilderExample build(){
            return new BuilderExample(this);
        }

    }

    private BuilderExample(Builder builder){
        name = builder.name;
        time = builder.time;
        weather = builder.weather;
        address = builder.address;
    }

}
