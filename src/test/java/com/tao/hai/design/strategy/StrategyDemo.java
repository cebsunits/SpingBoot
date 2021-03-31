package com.tao.hai.design.strategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;
/**
 * 使用lambda表达式后可以不用再创建指定的策略模式对象，直接使用即可
 * */
public class StrategyDemo {
    public static void main(String[] args) throws IOException {
        File outFile=new File("");
        Path inFile= Paths.get("/my.txt");
        Compressor compressor=new Compressor(new GzipCompressStrategy());
        compressor.compress(inFile,outFile);

        Compressor compressor1=new Compressor(new ZipCompressStrategy());
        compressor1.compress(inFile,outFile);
        //below use lambda
        Compressor compressor2=new Compressor(GZIPOutputStream::new);
        compressor2.compress(inFile,outFile);
        Compressor compressor3=new Compressor(ZipOutputStream::new);
        compressor3.compress(inFile,outFile);

    }
}
