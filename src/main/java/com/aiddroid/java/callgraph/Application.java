package com.aiddroid.java.callgraph;

import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用程序主类
 * @author allen
 */
public class Application {
    
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    
    /**
     * 主方法
     * @param args 
     */
    public static void main(String[] args) {
        // 获取方法调用关系
        MethodCallExtractor extractor = new MethodCallExtractor();
        Map<String, List<String>> methodCallRelation = extractor.getMethodCallRelationByDefault();
        
        // 声明有向图
        Graph<String, DefaultEdge> directedGraph =
            new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
        
        // 构建有向图
        for (Map.Entry<String, List<String>> entry : methodCallRelation.entrySet()) {
            String caller = entry.getKey();
            // 添加节点和边
            directedGraph.addVertex(caller);
            for (String callee : entry.getValue()) {
                directedGraph.addVertex(callee);
                directedGraph.addEdge(caller, callee);
            }
        }
        
        System.out.println("directedGraph:" + directedGraph + "\n");
        System.out.println("View doT graph below via https://edotor.net/ :" + "\n");
        System.out.println(toDoT(directedGraph));
    }
    
    /**
     * 绘制doT图形
     * @param directedGraph
     * @return 
     */
    public static String toDoT(Graph<String, DefaultEdge> directedGraph) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> {
            return v.replaceAll("[^a-zA-Z0-9]", "_");
        });
        exporter.setVertexAttributeProvider((v) -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute(v.toString()));
            return map;
        });
        Writer writer = new StringWriter();
        exporter.exportGraph(directedGraph, writer);
        
        return writer.toString();
    }
}
