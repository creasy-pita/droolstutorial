package com.function;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Stack;



/**
 * 布尔比较表达式树
 * Created by lujq on 2/2/2021.
 */
public class ExpressionTree {

    /**
     * 通过布尔表达式的后缀遍历字符序列构建布尔比较表达式树
     * @param postfix
     * @return
     */
    public static Node constructTree(String postfix) {
        Stack<Node> st = new Stack<>();
        Node root;
        int index = 0;
        while (index < postfix.length()) {
            char c = postfix.charAt(index);
            if (c == '&' || c == '|') {
                Node node = new Node();
                node.setType("united");
                node.setBoolOperator(new String(new char[]{c, c}));
                Node left = st.pop();
                Node right = st.pop();
                node.left = left;
                node.right = right;
                st.push(node);
                //&和| 需要出现两次
                index += 2;
            } else {
                int from = index;
                while (++index < postfix.length()) {
                    c = postfix.charAt(index);
                    if ((c == '&' || c == '|')) {
                        break;
                    }
                }
                String nodeStr = postfix.substring(from, index);
                String[] patternList = nodeStr.split(DroolsUtil.PATTERN_SEPARATOR);
                for (String pattern : patternList) {
                    if (StringUtils.isNotEmpty(pattern)) {
                        String[] split = pattern.split(DroolsUtil.SUB_SEPARATOR);
                        Node node = new Node();
                        node.setType("common");
                        node.setPattern(new RulePattern(split[0].split("\\."), split[1], split[2], split.length > 3 ? split[3] : null));
                        st.push(node);
                    }
                }
            }
        }
        root = st.peek();
        st.pop();
        return root;
    }

    /**
     * 布尔表达式运算
     * @param node 布尔表达式树
     * @param data 表示树中需要的数据
     * @return
     */
    public static boolean caculate(Node node, Map<String, Object> data) {
        if ("united".equals(node.getType())) {
            boolean left = caculate(node.left, data);
            boolean right = caculate(node.right, data);
            if ("&&".equals(node.getBoolOperator())) {
                return left && right;
            }else {
                return left || right;
            }
        }
        else {
            RulePattern pattern = node.getPattern();
            String[] keys = pattern.getLeftOperandKey();
            String leftValue = data.get(keys[keys.length - 1]).toString();
            return BooleanUtil.Compare(leftValue,pattern.rightOperand,pattern.leftOperandType,pattern.compareSymbol );
        }
    }

    /**
     * 布尔表达式运算
     * @param node 布尔表达式树
     * @param data 表示树中需要的数据
     * @return
     */
    public static boolean caculate(Node node, List<JSONObject> data) {
        if ("united".equals(node.getType())) {
            boolean left = caculate(node.left, data);
            boolean right = caculate(node.right, data);
            if ("&&".equals(node.getBoolOperator())) {
                return left && right;
            }else {
                return left || right;
            }
        }
        else {
            RulePattern pattern = node.getPattern();
            String[] keys = pattern.getLeftOperandKey();
            String leftValue = data.get(keys.length - 1).get(keys[keys.length - 1]).toString();
            return BooleanUtil.Compare(leftValue,pattern.rightOperand,pattern.leftOperandType,pattern.compareSymbol );
        }
    }
}

class Node {
    //united  common
    String type;
    //(当type是common)返回bool值的表达式  比如 age > 10
    RulePattern pattern;
    //（当type是united）bool操作符：&& 或者 ||
    String boolOperator;
    //（当type是united）bool操作符的两个操作数
    Node left, right;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RulePattern getPattern() {
        return pattern;
    }

    public void setPattern(RulePattern pattern) {
        this.pattern = pattern;
    }

    public String getBoolOperator() {
        return boolOperator;
    }

    public void setBoolOperator(String boolOperator) {
        this.boolOperator = boolOperator;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    Node() {

    }
}

/**
 * 布尔表达式的模式
 * <p>比如如下内容表示： subject 等于 "math"
 * <p>leftOperandKey = "subject" 也可以带上层实体路径并用[.]连接 比如 student.subject
 * <p>leftOperandType = "java.lang.String"
 * <p>compareSymbol = "=="
 * <p>rightOperand = "math"
 * <p>加上分隔符后格式： subject!@@!java.lang.String!@@!==!@@!math-@@-
 * */
class RulePattern {
    /**
     * 带上层实体路径并用[.]连接 比如 student.subject
     */
    String[] leftOperandKey;
    String leftOperandType;
    String compareSymbol;
    String rightOperand;

    public RulePattern(String[] leftOperandKey, String leftOperandType, String compareSymbol, String rightOperand) {
        this.leftOperandKey = leftOperandKey;
        this.leftOperandType = leftOperandType;
        this.compareSymbol = compareSymbol;
        this.rightOperand = rightOperand;
    }

    public String[] getLeftOperandKey() {
        return leftOperandKey;
    }

    public void setLeftOperandKey(String[] leftOperandKey) {
        this.leftOperandKey = leftOperandKey;
    }

    public String getCompareSymbol() {
        return compareSymbol;
    }

    public void setCompareSymbol(String compareSymbol) {
        this.compareSymbol = compareSymbol;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }
}




