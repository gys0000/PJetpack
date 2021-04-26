package com.gystry.designmodes;

import com.gystry.designmodes.bean.King;
import com.gystry.designmodes.singleton_pattern.ContSingleton;

public class MyClass {
    public static void main(String[] args) {
        King king = new King();
        ContSingleton.put("king",king);

        Object king1 = ((King) ContSingleton.get("king"));
        Object king2 = ((King) ContSingleton.get("king"));
        Object king3= ((King) ContSingleton.get("king"));
        Object king4 = ((King) ContSingleton.get("king"));

        System.out.println(king1+":"+king2);
    }
}