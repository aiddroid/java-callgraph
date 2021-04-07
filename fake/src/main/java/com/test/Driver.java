
package com.test;

public class Driver {
    protected Stub stub = new StubImpl();
    
    public String facade(String b){
//        Stub stub = new StubImpl();
        final String s = stub.subString(b);;
        return s + "hahaha";
    }
}
