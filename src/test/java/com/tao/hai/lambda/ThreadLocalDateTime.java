package com.tao.hai.lambda;


import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;

/**
 * thread safe date time formater
 * */
public class ThreadLocalDateTime {

    public static ThreadLocal<DateFormatter> dateFormatter=ThreadLocal.withInitial(()->new DateFormatter(new SimpleDateFormat("dd-MMM-yyyy")));
}
