package com.tao.hai.design.templet;
/**
 * 使用函数接口实现检查方法并没有排除继承的方式。我们可以显式地在这些类中使用 Lambda 表达式或者方法引用。
 * 我们也不需要强制 EmployeeLoanApplication 继承 PersonalLoanApplication 来达到复用， 可以对同一个方法传递引用。
 * 它们之间是否天然存在继承关系取决于员工的借贷是否是普 通人借贷这种特殊情况，或者是另外一种不同类型的借贷。
 * 因此，使用这种方式能让我们 更加紧密地为问题建模。
 * */
public class CompanyLoanApplicationLambda extends LoanApplicationLambda {
    public CompanyLoanApplicationLambda(Company company){
        super(company::checkIdentity,company::checkHistoricalDebt,company::checkProfitAndLoss);
    }
}
