package com.mahmoud.printinghouse.Utils;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ParentFragment extends Fragment implements IParentFragmentView {

    @Nullable
    //@BindView(R.id.tv_title)
    protected TextView tv_title;
    @Nullable
    //@BindView(R.id.v_actionbar)
    protected View v_actionbar;
    @Nullable
    //@BindView(R.id.v_loading)
    protected View v_loading;
    @Nullable
    //@BindView(R.id.progress_bar)
    protected ProgressBar progress_bar;
    @Nullable
    //@BindView(R.id.v_empty)
    public View v_empty;
    @Nullable
    //@BindView(R.id.v_noInternet)
    public View v_noInternet;
    @Nullable
    //@BindView(R.id.v_serverError)
    protected View v_serverError;


    protected String token;
    protected String language;
    //protected Paginate mPaginate;

    IInternetUtility retryClickListener;

    protected Dialog LoadingDialog;
    protected Dialog mConfirmDeleteDialog;

    protected ImageView iv_cancelDeleteDialog;
    protected Button btn_cancelDelete, btn_confirmDelete;
    protected TextView tv_dialog_title, tv_cancelMessage;


    @Override
    public void setupUI() {
        if (SharedPrefManager.getInstance(getContext()).getLoginStatus()) {
            token = "Bearer ";///+SharedPrefManager.getInstance(getContext()).getUserData().getToken();
        } else {
            token = "Bearer ";
        }
        language = ParentClass.getLocalization(getContext());

        if (v_noInternet != null) {
            v_noInternet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("hhhhh", "ya beh ");
                    retryClickListener.onRetryClick();
                }
            });
        }


        //init paginate
        /*mPaginate=new Paginate();
        mPaginate.setCurrentPage(1);
        mPaginate.setTotal(0);
*/
    }

    @Override
    public void showProgressDialog() {
        if (v_loading != null) {
            v_loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (v_loading != null) {
            v_loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadMoreProgress() {
        if (progress_bar != null) {
            progress_bar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoadMoreProgress() {
        if (progress_bar != null) {
            progress_bar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (LoadingDialog != null) {
            LoadingDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (LoadingDialog != null) {
            LoadingDialog.dismiss();
        }
    }

    @Override
    public void showConfirmingDialog() {
        Log.v("hhhh", "delete dialog clicked");
        if (mConfirmDeleteDialog != null) {
            mConfirmDeleteDialog.show();
        }
    }

    @Override
    public void hideConfirmingDialog() {
        if (mConfirmDeleteDialog != null) {
            mConfirmDeleteDialog.dismiss();
        }
    }

    @Override
    public void getErrorMessage(String msg, Throwable t) {
        hideProgressDialog();
        hideLoadMoreProgress();
        hideLoadingDialog();
        hideConfirmingDialog();
        if (t != null) {
            ParentClass.handleException(getContext(), t);
        } else {
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    public void setRetryClickListener(IInternetUtility retryClickListener) {
        this.retryClickListener = retryClickListener;
    }


    protected void initLoadingDialog() {
        //LoadingDialog = new Dialog(getContext(), R.style.NewDialog1);
        //LoadingDialog.setContentView(R.layout.view_dialog_loading);
        //LoadingDialog.setCancelable(false);
    }

}
