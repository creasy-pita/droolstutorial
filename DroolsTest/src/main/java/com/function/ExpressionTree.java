package com.function;

import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Stack;

/**
 * Created by lujq on 2/2/2021.
 */
public class ExpressionTree {

    public static Node constructTree(String postfix) {
        Stack<Node> st = new Stack<Node>();
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
                index += 2;
            } else {
                int from = index;
                while (++index < postfix.length()) {
                    c = postfix.charAt(index);
                    if ((c == '&' || c == '|')) {
                        break;
                    }
                }
                // from ,index-1
                String nodeStr = postfix.substring(from, index);
                String[] patternList = nodeStr.split(DroolsUtil.PATTERN_SEPARATOR);
                for (String pattern : patternList) {

                    if (StringUtils.isNotEmpty(pattern)) {
                        String[] split = pattern.split(DroolsUtil.SUB_SEPARATOR);
                        Node node = new Node();
                        node.setType("common");
                        node.setPattern(new RulePattern(split[0], split[1], split[2], split.length > 3 ? split[3] : null));
                        st.push(node);
                    }
                }
            }
        }
        root = st.peek();
        st.pop();
        return root;
    }

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
            return BooleanUtil.Compare(data.get(pattern.leftOperandKey).toString(),pattern.rightOperand,pattern.leftOperandType,pattern.compareSymbol );
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

class RulePattern {
    String leftOperandKey;
    String leftOperandType;
    String compareSymbol;
    String rightOperand;

    public RulePattern(String leftOperandKey, String leftOperandType, String compareSymbol, String rightOperand) {
        this.leftOperandKey = leftOperandKey;
        this.leftOperandType = leftOperandType;
        this.compareSymbol = compareSymbol;
        this.rightOperand = rightOperand;
    }

    public String getLeftOperandKey() {
        return leftOperandKey;
    }

    public void setLeftOperandKey(String leftOperandKey) {
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


