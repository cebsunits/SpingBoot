package com.tao.hai.design.strategy;

import java.io.IOException;
import java.io.OutputStream;

public interface CompressStrategy {
    public OutputStream compress(OutputStream data) throws IOException;
}
