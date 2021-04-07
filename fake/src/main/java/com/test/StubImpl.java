package com.test;

public class StubImpl implements Stub {
    @Override
    public String subString(String a) {
        return a.substring(0,5) + new Texter().getSuffix();
    }
}
