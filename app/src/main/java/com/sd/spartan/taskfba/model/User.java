package com.sd.spartan.taskfba.model;

public class User {

    private String name, email, password, age, date_of_birth ;


    public User(String name, String email, String password, String age, String date_of_birth) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.date_of_birth = date_of_birth;
    }


    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAge() {
        return age;
    }

    public User setAge(String age) {
        this.age = age;
        return this;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public User setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                '}';
    }
}
