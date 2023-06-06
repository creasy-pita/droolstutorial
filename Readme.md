# drools tutorial

## 2021-05-13

改造runtimemanager,提供多个drools runtime的版本


### 演示应用配置

**待做**  配置请假总天数5天内



## 2020年8月22日

### :grey_question: 问题全量评估过程如何避免

### 加入《Drools7.10权威指南》的测试用例

github: https://github.com/projectLzh/DroolsTest

## 2020年8月20日

加入自定义的规则过滤器CPAgendaFilter,可以只触发校验规则文件中Filter匹配的规则
校验前有全量评估过程，同样会调用kiecontainer所有的规则（即没有触发的规则也会调用）
校验前有全量评估过程会执行，所以一些规则中一些参数为null需要避免

## kiecontainer deploy more rules

see the test RulesListTest method WhenDeployMoreRules_ThenOldIsRemainThere
kiefileSystem 会保存加载的rules
kiecontainer 可以有多个，kieServices.newKieContainer 会创建新的kiecontainer实例
