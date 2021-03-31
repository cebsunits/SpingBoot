package com.tao.hai.design.templet;

public interface Company {
    public void checkIdentity() throws ApplicationDenied;
    public void checkProfitAndLoss() throws ApplicationDenied;
    public void checkHistoricalDebt() throws ApplicationDenied;
}
