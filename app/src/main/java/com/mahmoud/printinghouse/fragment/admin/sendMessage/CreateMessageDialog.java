package com.mahmoud.printinghouse.fragment.admin.sendMessage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.databinding.DialogSendMessageBinding;

import java.util.List;
import java.util.Objects;

public class CreateMessageDialog extends DialogFragment {

    private DialogSendMessageBinding messageBinding ;
    private SendMessageOnClick sendMessageOnClick ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NewDialog1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        messageBinding = DialogSendMessageBinding.inflate(inflater,container,false);
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
        return messageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageBinding.setIsLoading(false);
        messageBinding.sendMsg.setOnClickListener(mOnSendClickListener);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    private View.OnClickListener mOnSendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(messageBinding.edtMessage.getText().toString().trim())) {
                return;
            }
            sendMessageOnClick.onClick(messageBinding.edtMessage.getText().toString().trim());
        }
    };


    public void setOnClickView(SendMessageOnClick sendMessageOnClick){
        this.sendMessageOnClick = sendMessageOnClick ;
    }


    public void isLoading(boolean isLoading){
        messageBinding.setIsLoading(isLoading);
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        dialog.cancel();
    }

}
