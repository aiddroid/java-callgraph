## java-call-graph
A demo app show you how to generate a java call graph.

### setup
- import project to netbeans
- update your own configure in src/main/resources/*.cfg
- "Build Project"


### run
```
java -jar java-callgraph-1.0-SNAPSHOT.jar
```

### call graph
- view doT graph below via https://edotor.net/
```
strict digraph G {
  com_test_StubImpl_subString_java_lang_String_ [ label="com.test.StubImpl.subString(java.lang.String)" ];
  com_test_Texter_getSuffix__ [ label="com.test.Texter.getSuffix()" ];
  com_tool_Util_Upper_java_lang_String_ [ label="com.tool.Util.Upper(java.lang.String)" ];
  com_test_Stub_subString_java_lang_String_ [ label="com.test.Stub.subString(java.lang.String)" ];
  com_test_Main_main_java_lang_String___ [ label="com.test.Main.main(java.lang.String[])" ];
  com_test_Driver_facade_java_lang_String_ [ label="com.test.Driver.facade(java.lang.String)" ];
  com_test_StubImpl_subString_java_lang_String_ -> com_test_Texter_getSuffix__;
  com_test_Main_main_java_lang_String___ -> com_test_Driver_facade_java_lang_String_;
  com_test_Main_main_java_lang_String___ -> com_tool_Util_Upper_java_lang_String_;
  com_test_Driver_facade_java_lang_String_ -> com_test_Stub_subString_java_lang_String_;
}
```

### screenshot
![image](./src/main/resources/screenshot.png)
