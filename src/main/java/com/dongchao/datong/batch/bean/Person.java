package com.dongchao.datong.batch.bean;

import java.io.Serializable;

/**
 * 批处理领域模型类
 */
public class Person implements Serializable {
    private String name ;
    private Integer age ;
    private String nation ;
    private String address ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nation='" + nation + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
