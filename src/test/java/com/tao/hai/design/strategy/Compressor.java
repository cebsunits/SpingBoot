package com.tao.hai.design.strategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Compressor {
    private final CompressStrategy strategy;
    public Compressor(CompressStrategy strategy){
        this.strategy=strategy;
    }
    public void compress(Path inFile,File outFile) throws IOException {
        try(OutputStream out=new FileOutputStream(outFile)){
            Files.copy(inFile,strategy.compress(out));
        }
    }
}
