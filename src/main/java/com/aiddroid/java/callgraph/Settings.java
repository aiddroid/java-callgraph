package com.aiddroid.java.callgraph;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设置类
 * @author allen
 */
public class Settings {
    private static Logger logger = LoggerFactory.getLogger(Settings.class);
    
    // 源码目录列表
    private List<String> srcDirs = new ArrayList<String>();
    
    // lib目录列表
    private List<String> libDirs = new ArrayList<String>();
    
    // 需要跳过的pattern列表
    private List<Pattern> skipPatterns = new ArrayList<Pattern>();
    
    // doT图形输出路径
    private String output = null;
    
    // 配置
    private Options options = new Options();

    /**
     * 构造方法
     */
    public Settings() {
        initOptions();
    }
    
    /**
     * 初始化命令行配置
     */
    private void initOptions(){
        Option srcOption = new Option("s", "src", true, "source code dirs [required]");
        srcOption.setArgs(Option.UNLIMITED_VALUES);
        srcOption.setRequired(true);
        options.addOption(srcOption);
        
        Option libOption = new Option("l", "lib", true, "library dirs");
        libOption.setArgs(Option.UNLIMITED_VALUES);
        // libOption.setRequired(true);
        options.addOption(libOption);
        
        Option skipOption = new Option("k", "skip", true, "skip patterns");
        skipOption.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(skipOption);
        
        Option outputOption = new Option("o", "output", true, "doT file output path");
        options.addOption(outputOption);
        
        options.addOption("h", "help", false, "show help info");
    }
    
    /**
     * 通过命令行参数初始化
     * @param args 
     */
    public void initFromCmdArgs(String[] args) {
        DefaultParser parser = new DefaultParser();
        
        try {
            CommandLine cmdLine = parser.parse(options, args);
            
            // 判断是否是help
            if (cmdLine.hasOption("help")) {
                printHelp(0);
                return;
            }
            
            // 判断是否有源码参数
            if (cmdLine.hasOption("src")) {
                setSrcDirs(Arrays.asList(cmdLine.getOptionValues("src")));
            }
            
            // 判断是否有lib参数
            if (cmdLine.hasOption("lib")) {
                setLibDirs(Arrays.asList(cmdLine.getOptionValues("lib")));
            }
            
            // 判断是否有skip参数
            if (cmdLine.hasOption("skip")) {
                setSkipPatterns(Arrays.asList(cmdLine.getOptionValues("skip")));
            }
            
            // 判断是否有output参数
            if (cmdLine.hasOption("output")) {
                setOutput(cmdLine.getOptionValue("output"));
            }
            
            logger.debug("settings:" + this.toString());
        } catch (Exception e) {
            logger.error("Invalid arguments, " + e.getMessage());
            printHelp(1);
        }
    }
    
    /**
     * 获取目录绝对路径
     * @param dir
     * @return 
     */
    private String getDirAbsPath(String dir) {
        Path path = Paths.get(dir);
        // 判断是否为目录
        if (!path.toFile().isDirectory()) {
            logger.error(dir + " is not a directory.");
            printHelp(1);
            return null;
        }
        
        // 判断是否可读
        if (!Files.isReadable(path)) {
            logger.error(dir + " is not readable.");
            printHelp(1);
            return null;
        }
        
        String absPath = path.toAbsolutePath().toString();
        return absPath;
    }
    
    /**
     * 打印帮助信息
     * @param exitCode 
     */
    public void printHelp(int exitCode) {
       HelpFormatter formatter = new HelpFormatter();
       // 避免参数排序
       formatter.setOptionComparator(null);
       formatter.printHelp("java -jar java-call-graph-1.0-SNAPSHOT-jar-with-dependencies.jar ", 
               "========================================\n" + 
               "========= java-callgraph 1.0 ===========\n" + 
               "========================================",
               options,
               "========================================",
               true
       );
       
       System.exit(exitCode);
    }

    public List<String> getSrcDirs() {
        return srcDirs;
    }

    public void setSrcDirs(List<String> srcDirs) {
        for (String srcDir : srcDirs) {
            // 获取绝对路径并加入到列表
            String absPath = getDirAbsPath(srcDir);
            if (absPath != null) {
                this.srcDirs.add(absPath);
            }
        }
    }

    public List<String> getLibDirs() {
        return libDirs;
    }

    public void setLibDirs(List<String> libDirs) {
        for (String libDir : libDirs) {
            // 获取绝对路径并加入到列表
            String absPath = getDirAbsPath(libDir);
            if (absPath != null) {
                this.libDirs.add(absPath);
            }
        }
    }

    public List<Pattern> getSkipPatterns() {
        return skipPatterns;
    }

    public void setSkipPatterns(List<String> skipPatterns) {
        this.skipPatterns = skipPatterns.stream().map(t -> Pattern.compile(t)).collect(Collectors.toList());
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        Path path = Paths.get(output);
        // 判断如果是目录，则报错
        if (path.toFile().isDirectory()) {
            logger.error(output + " should be a file, while a folder instead.");
            printHelp(1);
            return;
        }
        
        this.output = path.toAbsolutePath().toString();
    }

    @Override
    public String toString() {
        return "Settings{" + "srcDirs=" + srcDirs + ", libDirs=" + libDirs + ", skipPatterns=" + skipPatterns + ", output=" + output + ", options=" + options + '}';
    }
    
    
}
