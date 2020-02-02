package com.mahmoud.printinghouse.Utils;

public interface IParentFragmentView {


    void setupUI();
    void showProgressDialog();
    void hideProgressDialog();
    void showLoadMoreProgress();
    void hideLoadMoreProgress();
    void showLoadingDialog();
    void hideLoadingDialog();
    void showConfirmingDialog();
    void hideConfirmingDialog();
    void getErrorMessage(String msg, Throwable t);
}
