package com.mahmoud.printinghouse.fragment.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.Helpers.NetworkStateItemViewHolder;
import com.mahmoud.printinghouse.Utils.NetworkState;
import com.mahmoud.printinghouse.databinding.MyListBinding;
import com.mahmoud.printinghouse.databinding.NetworkItemBinding;
import com.mahmoud.printinghouse.databinding.ViewOrderBinding;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;

import java.util.Objects;

public class OrderAdapter extends PagedListAdapter<OrdersItem, RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    private NetworkState networkState;
    private OnItemClickListener listener;


    private LayoutInflater layoutInflater;

    public OrderAdapter() {
        super(OrdersItem.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_PROGRESS) {
            NetworkItemBinding headerBinding = NetworkItemBinding.inflate(layoutInflater, parent,
                    false);
            return new NetworkStateItemViewHolder(headerBinding);

        } else {
            return new ViewHolder(DataBindingUtil.inflate(layoutInflater,
                    R.layout.view_order,parent,false));
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            ((ViewHolder)holder).bind(getItem(position));
        } else {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ViewOrderBinding myListBinding;
        ViewHolder(@NonNull ViewOrderBinding myListBinding) {
            super(myListBinding.getRoot());
            this.myListBinding=myListBinding;
        }
        void bind(OrdersItem item){
            this.myListBinding.setItem(item);
            this.myListBinding.img.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(view,item, position);
                }
            });
            this.myListBinding.sendMsg.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(view,item, position);
                }
            });
            myListBinding.executePendingBindings();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, OrdersItem model, int position);
    }


    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

}
