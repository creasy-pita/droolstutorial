package com.Runtime;

/**
 * Created by lujq on 5/13/2021.
 */
import com.Runtime.RuleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *   规则引擎运行时管理器
 *
 *   1.规则引擎在启动的时候，会加载默认的一个运行时
 *   然后初始化一个测试用的运行时
 * 　@author yujl2
 * 　@date: 2019/10/8 15:19
 *
 */
public class GisRuleEngineRuntimeManager {
    private Logger logger = LoggerFactory.getLogger(GisRuleEngineRuntimeManager.class);
    // 工作的容器列表
    private List<DroolsEngineRuntime> runtimeList = new ArrayList<>();
    private HashMap<String,DroolsEngineRuntime> runtimeMap = new HashMap<>();
    // 测试用的运行时，用于规则平台的运行时构建时，提供一个编译环境。如果编译失败说明规则有问题
    private DroolsEngineRuntime testRuntime;
    private RuleProvider ruleProvider;


    public GisRuleEngineRuntimeManager(RuleProvider rp)throws Exception{
        if(rp == null){
            logger.error("系统没有检测到规则提供者，请检查配置项");
        }
        this.ruleProvider=rp;
        logger.info("----开始初始化默认运行时----");
        runtimeList.add(new DroolsEngineRuntime(ruleProvider));
        // 用于规则平台构建运行时的时候，编译用的运行时
        testRuntime = new DroolsEngineRuntime(rp,"1.0.0-SNAPSHOT","test-runtime","com.gisquest");
        logger.info("----默认运行时初始化完成----");
    }

    public GisRuleEngineRuntimeManager()throws Exception{}

    public void putRuntime(String key, RuleProvider rp) throws Exception {
        runtimeMap.put(key, new DroolsEngineRuntime(rp));
    }

    public DroolsEngineRuntime getRuntime(String key) throws Exception {
        return runtimeMap.get(key);
    }

    public DroolsEngineRuntime getDefaultRuntime(){
        return runtimeList.get(0);
    }
    public DroolsEngineRuntime getTestRuntime(){
        return testRuntime;
    }
}

