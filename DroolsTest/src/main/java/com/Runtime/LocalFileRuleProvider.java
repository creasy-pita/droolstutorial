package com.Runtime;

import org.apache.commons.io.IOUtils;
import org.drools.core.io.impl.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * 　@author yujl2
 * 　@date: 2019/9/29 19:32
 *
 */
public class LocalFileRuleProvider extends AbstractRuleProvider {
    Logger logger = LoggerFactory.getLogger(LocalFileRuleProvider.class);

    /** 规则文件所在目录 */
    private String LocalFilePath;

    public LocalFileRuleProvider(String localFilePath) {
        LocalFilePath = localFilePath;
    }

    @Override
    public GzGzyxs loadRule() {
        byte[] bytes = null;
        // todo 如果出错使用 org.springframework.core.io.ClassPathResource
        ClassPathResource resource = new ClassPathResource(LocalFilePath);
        try {
            bytes = IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            logger.error("规则文件不存在，请检查："+LocalFilePath,e);
        }
        logger.info("-----读取到本地规则运行时-----");
        logger.info(new String(bytes,StandardCharsets.UTF_8));
        logger.info("-----------------------------");
        GzGzyxs yxs = new GzGzyxs();
        yxs.setSxw(bytes);
        return yxs;
    }

    @Override
    public String getRuleSourceDescription() {
        return "基于本地drl文件提供规则运行时，文件路径为： "+LocalFilePath;
    }
}
