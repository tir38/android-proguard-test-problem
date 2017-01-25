package com.example.myapplication.subpackage;

/**
 * This is a class that is never referenced and should be removed by ProGuard
 */
public class EmptyClass {

    private int id;

    public EmptyClass(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}