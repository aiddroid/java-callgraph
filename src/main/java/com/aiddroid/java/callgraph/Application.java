package com.aiddroid.java.callgraph;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
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
        // 初始化配置
        Settings settings = new Settings();
        settings.initFromCmdArgs(args);
        
        // 获取方法调用关系
        MethodCallExtractor extractor = new MethodCallExtractor(settings);
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
        
        logger.info("directedGraph:" + directedGraph + "\n");
        logger.info("View doT graph below via https://edotor.net/ :" + "\n");
        
        String doT = toDoT(directedGraph);
        logger.info(doT);
        
        // 获取output配置并判断是否需要保存doT到文件
        String outputFile = settings.getOutput();
        if (outputFile != null) {
            try {
                FileUtils.writeStringToFile(new File(outputFile), doT, "UTF-8", true);
                logger.info("doT image saved to " + outputFile);
            } catch (Exception e) {
                logger.error("write doT error, " + e.getMessage());
            }
        }
    }
    
    /**
     * 转换为doT
     * @param directedGraph
     * @return 
     */
    public static String toDoT(Graph<String, DefaultEdge> directedGraph) {
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> {
            // 替换掉特殊字符
            return Utils.removeIllegalChar(v);
        });
        
        // 为节点添加label
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
