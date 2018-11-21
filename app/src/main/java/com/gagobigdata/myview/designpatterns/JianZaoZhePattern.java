package com.gagobigdata.myview.designpatterns;

/**
 * Created by llx on 2018/10/30.
 */
public class JianZaoZhePattern {
}

class Person {

    private String name;
    private int age;
    private String address;
    private String birthday;
    private String birthplace;

    //构造方法私有
    private Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", birthday='" + birthday + '\'' +
                ", birthplace='" + birthplace + '\'' +
                '}';
    }

    public static class Builder {

        private String name;
        private int age;
        private String address;
        private String birthday;
        private String birthplace;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setBirthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder setBirthplace(String birthplace) {
            this.birthplace = birthplace;
            return this;
        }

        public Person build() {
            Person person = new Person();
            person.name = name;
            person.address = address;
            person.birthday = birthday;
            person.birthplace = birthplace;
            if (age >= 0) {
                person.age = age;
            }
            return person;
        }
    }
}

class Client {
    public static void main(String[] args) {
        //经典链式调用
        Person person = new Person.Builder()
                .setName("llx")
                .setAge(23)
                .setAddress("HeNan")
                .build();
        System.out.println(person.toString());
    }
}


