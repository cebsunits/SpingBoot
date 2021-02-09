package com.tao.hai.algo;

import java.util.Stack;

/**最小栈
 * FILO 先入后出
 *
 * */
public class MinStack {

    private Stack<Integer> mainStacks=new Stack<Integer>();
    private Stack<Integer> minStacks=new Stack<Integer>();

    /***入栈操作*/
    public void push(int element){
        mainStacks.push(element);
        //如果辅助栈为空，或者新元素小于等于辅助栈栈顶，则将新元素放入辅助栈栈顶
        if(minStacks.isEmpty()||element<=minStacks.peek()){
            minStacks.push(element);
        }
    }
    /**出栈操作*/
    public Integer pop(){
        //如果出栈元素与辅助栈栈顶元素相等，则辅助栈出栈
        if(mainStacks.peek().equals(minStacks.peek())){
            minStacks.pop();
        }
        return mainStacks.pop();
    }
    /**获取最小min值*/
    public int getMin() throws Exception{
        if(mainStacks.isEmpty()){
            throw new Exception("Stack is empty");
        }
        return minStacks.peek();
    }
    /**获取当前栈顶值*/
    public int get()throws Exception{
        if(mainStacks.isEmpty()){
            throw new Exception("Stack is empty");
        }
        return mainStacks.peek();
    }
    public static void main(String[] args) throws Exception{
        MinStack stack=new MinStack();
        stack.push(4);
        stack.push(9);
        stack.push(4);
        stack.push(3);
        stack.push(5);
        stack.push(6);
        stack.push(7);
        System.out.println(stack.getMin());
        System.out.println("stack top:"+stack.get());
        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();
        System.out.println(stack.getMin());
    }
}
