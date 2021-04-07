package com.test;

import com.tool.Util;

public class Main {
    public static void main(String[] args) {
        String s = new Driver().facade("hello, world!");
        System.out.println(Util.Upper(s));
    }
}
