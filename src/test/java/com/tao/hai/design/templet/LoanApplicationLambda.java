package com.tao.hai.design.templet;
/**
 *采用这种方式，而不是基于继承的模式的好处是不需要在 LoanApplicationLambda 及其子类中实 现算法，
 * 分配功能时有了更大的灵活性。比如，我们想让 Company 类负责所有的检查，那 么 Company 类就会多出一系列方法
 * */
public class LoanApplicationLambda {

    private Criteria identity;
    private Criteria creditHistory;
    private Criteria incomeHistory;
    public LoanApplicationLambda(Criteria identity,Criteria creditHistory,Criteria incomeHistory){
        this.identity=identity;
        this.creditHistory=creditHistory;
        this.incomeHistory=incomeHistory;
    }
    public void checkLoanApplication() throws ApplicationDenied{
        identity.check();
        creditHistory.check();
        incomeHistory.check();
    }
    private void reportFindings(){

    }
}
