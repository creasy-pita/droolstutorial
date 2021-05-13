package com.Runtime;


/**
 *   规则提供者
 * 　@author yujl2
 * 　@date: 2019/9/29 19:30
 *
 */
public interface RuleProvider {
    /*
       描述规则来源描述信息
     */
    String getRuleSourceDescription();
    /**
     * 加载规则
     * @return 规则文件对应的byte数组
     */
    GzGzyxs loadRule();
}
