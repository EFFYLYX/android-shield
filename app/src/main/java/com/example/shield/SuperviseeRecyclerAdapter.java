package com.example.shield;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by effy on 2018/10/30.
 */

public class SuperviseeRecyclerAdapter extends RecyclerView.Adapter<SuperviseeRecyclerAdapter.TabRecyclerHolder>{

    private Context mContext;
    private List<User> dataList = new ArrayList<>();
//    private onRecyclerItemClickerListener mListener;


    public SuperviseeRecyclerAdapter(Context context, List dataList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }


    private LayoutInflater mInflater;
    @Override
    public SuperviseeRecyclerAdapter.TabRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = mInflater.inflate(R.layout.supervisee_item, parent, false);




        return new SuperviseeRecyclerAdapter.TabRecyclerHolder(item);

    }

    public void addItem(int position, User u) {
        dataList.add(position,u);
        notifyDataSetChanged();
        // uns.add(position, accountNo);
//        bookComments.add(position, bookComment);
//        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public void onBindViewHolder(SuperviseeRecyclerAdapter.TabRecyclerHolder holder, int position) {


        User u = dataList.get(position);
        String first = u.getFirstSymbol();
        holder.imageView.setImageDrawable(new ColorCircleDrawable(first ));
        holder.tv_name.setText(u.getUsername());
        holder.tv_phone.setText(u.phoneNo);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TabRecyclerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_name;
        TextView tv_phone;

        public TabRecyclerHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.mHeader_iv);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);

        }
    }
}
