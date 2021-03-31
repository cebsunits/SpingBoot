package com.tao.hai.design.templet;

public class EmployeeLoanApplication extends PersonLoanApplication {
    @Override
    protected void checkIncomeHistory() throws ApplicationDenied {
        System.out.println("这是自己人！");
    }
}
