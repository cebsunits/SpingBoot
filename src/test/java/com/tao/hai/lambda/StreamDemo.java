package com.tao.hai.lambda;

import com.tao.hai.serializable.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

    public static void main(String[] args) {
        //create list static data
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<=100;i++){
            list.add(i);
        }
        //use lambda expression
        int total=list.stream().reduce(Integer::sum).orElse(0);
        System.out.println(total);
        //use object
        List<User> list1=new ArrayList<>();
        for(int i=0;i<=10;i++){
            User user=new User();
            user.setAge(i);
            user.setName("name="+i);
            list1.add(user);
        }
        int totalAge=list1.stream().mapToInt(User::getAge).sum();
        System.out.println("totalAge:"+totalAge);
        int totalA=list1.stream().collect(Collectors.summingInt(User::getAge));
        System.out.println("totalA:="+totalA);
        //statistics
        IntSummaryStatistics statistics=list1.stream().mapToInt(User::getAge).summaryStatistics();
        System.out.println("getAverage:="+statistics.getAverage());
        System.out.println("getMax:="+statistics.getMax());
        System.out.println("getMin:="+statistics.getMin());
        System.out.println("getSum:="+statistics.getSum());
        System.out.println("getCount:="+statistics.getCount());
        //filter
        List<User> list2=list1.stream().filter(user->user.getAge()%2==0).collect(Collectors.toList());
        System.out.println(list2);
        //flatMap union list
        List<User> listName= Stream.of(list1,list2).flatMap(u->u.stream()).collect(Collectors.toList());
        System.out.println(listName);
        //map
        List<String> mapList=Stream.of("abc","ddde","hello").map(x->x.toUpperCase()).collect(Collectors.toList());
        System.out.println(mapList);
        //
        List<String> beginNumbers=Stream.of("a2","1b").filter(x->isDigit(x.charAt(0))).collect(Collectors.toList());
        System.out.println(beginNumbers);
        //reduce
        int count=Stream.of(1,2,3).reduce(0,(acc,el)->acc+el);
        System.out.println("count："+count);
        BinaryOperator<Integer> accumulator=(acc,el)->acc+el;
        count =accumulator.apply(accumulator.apply(accumulator.apply(0,1),2),3);
        System.out.println("count："+count);
        //异步方式
        CompletableFuture<User> future=new CompletableFuture<>();
        //提供一个值
        future.complete(new User());

    }
    public static boolean isDigit(char c){
        if(Character.isDigit(c)){
            return true;
        }
        return false;
    }
}
