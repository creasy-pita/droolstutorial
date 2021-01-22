package com.other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pojo.Person;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by lujq on 1/12/2021.
 */
public class BigObjRule {
    @Test
    public void testBigObjRule() throws URISyntaxException, IOException {


        java.util.List<Object> a = new ArrayList<>();
        java.util.List b = new ArrayList();

        KieServices kss = KieServices.Factory.get();
        KieContainer kc = kss.getKieClasspathContainer();
        KieSession ks =kc.newKieSession("bigobj");
        //查看resouce目录
        System.out.println(this.getClass().getResource("/").getPath());
        java.net.URL url = this.getClass().getResource("/bigobj.json");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        String jsonStr = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        JSONObject obj = JSON.parseObject(jsonStr);

        ks.setGlobal("$obj",obj);
        int count = ks.fireAllRules();
        System.out.println("总执行了"+count+"条规则");
        System.out.println(obj);
        ks.dispose();
    }

}
