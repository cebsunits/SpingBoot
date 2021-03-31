package com.tao.hai.design.templet;
/**
 * 使用模板方法模式描述申请贷款过程
 * */
public abstract class LoanApplication {
    public void checkLoanApplication() throws ApplicationDenied{
        checkIdentity();
        checkIncomeHistory();
        checkCreditHistory();
        reportFindings();
    }
    protected abstract void checkIdentity() throws ApplicationDenied;
    protected abstract void checkIncomeHistory() throws ApplicationDenied;
    protected abstract void checkCreditHistory() throws ApplicationDenied;
    private void reportFindings(){

    }
}
