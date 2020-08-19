package com.creasypita.drools.core;

import org.drools.compiler.kie.builder.impl.KieRepositoryImpl;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 规则引擎上下文
 * 　@author yujl2
 * 　@date: 2019/9/29 10:01
 */
public class DroolsEngineRuntime {
    private Logger logger = LoggerFactory.getLogger(DroolsEngineRuntime.class);
    // local db
    private String currentRuntimeType="";
    // type=local 则为local-file   type=db 则为运行时名称
    private String currentRuntimeName="";
    // 知识服务
    private KieServices kieServices;

    // 知识库
    private KieRepository kieRepository;

    // 文件系统
    private KieFileSystem kieFileSystem;
//    // 规则提供者，如本地文件，或者向规则平台加载规则
//    private RuleProvider ruleProvider;

    // 规则容器
    private KieContainer kieContainer;
    /*
    默认的GAV，如果构造方法传入的参数为空，则会采用默认值创建运行时
     */
    private static final String DEFAULT_VERSION = "1.0.0-SNAPSHOT";
    private static final String DEFAULT_ARTIFACT = "rule-package";
    private static final String DEFAULT_GROUP = "com.gisquest";
    private String version;
    private String artifact;
    private String group;

    // 规则文件存储的位置，当规则初始化策略为Local-File 的时候使用
    private static final String DEFAULT_FILE_PATH = "rules/rules.drl";

    private String filePath;

    /**
     * 创建一个引擎运行时
     * 根据一个ruleProvider
     */
    public DroolsEngineRuntime() throws Exception{
        logger.info("正在创建运行时...");
        /*
            读取配置
         */
        this.filePath = DEFAULT_FILE_PATH;
        this.version = DEFAULT_VERSION;
        this.artifact = DEFAULT_ARTIFACT;
        this.group = DEFAULT_GROUP;
        kieServices = KieServices.Factory.get();
        kieRepository = kieServices.getRepository();
        kieFileSystem = kieServices.newKieFileSystem();
        // 初始化default GAV
        ((KieRepositoryImpl) kieRepository).setDefaultGAV(getDefaultReleaseId());
        // 由gisq.service.ruleexcutor.provider决定
        //初始化时默认如下规则
        kieFileSystem.write(ResourceFactory.newClassPathResource("com/creasypita/drools/rules/SuggestApplicant.drl"));

        initKieContainer();
        logger.info("运行时创建成功");
    }

    /**
     * 获得规则运行的容器
     * @return
     */
    public KieContainer getKieContainer() {
        return this.kieContainer;
    }

    /**
     * 获取默认的GAV
     * @return
     */
    public ReleaseId getDefaultReleaseId() {
        return new ReleaseIdImpl(DEFAULT_GROUP, DEFAULT_ARTIFACT, DEFAULT_VERSION);
    }

    /**
     * 获取这个runtime的GAV
     * @return
     */
    public ReleaseId getCurrentReleaseId() {
        return new ReleaseIdImpl(this.group, this.artifact, this.version);
    }

    /**
     * 根据这个runtime的fileSystem初始化container
     */
    public void initKieContainer() throws Exception{
        KieBuilder kieBuilder = kieServices.newKieBuilder(this.kieFileSystem).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            logger.error(results.getMessages().toString(), new IllegalStateException("rule kieContainer init error!"));
            throw new Exception("rule kieContainer init error!");
        }
        this.kieContainer = kieServices.newKieContainer(getCurrentReleaseId());
    }


    /**
     * 根据外部byte[] 的drl文件来初始化container
     * @param context
     */
    public void rebuildKieContainer(byte[] context) throws Exception{
        logger.info("根据指定上下文来重建container");
        kieFileSystem.delete("src/main/resources/" + filePath);
        kieFileSystem.write("src/main/resources/" + filePath, context);
        initKieContainer();
    }

    public void clearRule() throws Exception{
        kieFileSystem.delete("src/main/resources/" + filePath);
        initKieContainer();
    }

    /**
     * 部署一个规则运行时
     * @param context 外部指定的运行时，即drl文件转为的byte数组
     */
    public void deploy(byte[] context)throws Exception{
        logger.info("部署一个外部指定的drl运行时");
        kieFileSystem.delete("src/main/resources/" + filePath);
        kieFileSystem.write("src/main/resources/" + filePath, context);
        initKieContainer();
    }


    /**
     * 部署一个规则运行时
     * @param resource
     */
    public void deploy(Resource resource)throws Exception{
        logger.info("部署一个外部指定的drl运行时");
//        kieFileSystem.delete("src/main/resources/" + filePath);
        kieFileSystem.write( resource);
        initKieContainer();
    }


    public String getCurrentRuntimeType() {
        return currentRuntimeType;
    }


    public String getCurrentRuntimeName() {
        return currentRuntimeName;
    }
}

