package com.example.shield;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by effy on 2018/10/30.
 */

public class MsgRecylerAdapter extends RecyclerView.Adapter<MsgRecylerAdapter.MsgRecylerHolder> {


    private Context mContext;
    private List<MyMessage> dataList = new ArrayList<>();

    private LayoutInflater mInflater;

    public MsgRecylerAdapter(Context context, List dataList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }
    @Override
    public MsgRecylerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = mInflater.inflate(R.layout.msg_item, parent, false);


        return new MsgRecylerAdapter.MsgRecylerHolder(item);
    }

    @Override
    public void onBindViewHolder(final MsgRecylerHolder holder, final int position) {
        final MyMessage msg = dataList.get(position);
        final Badge qBadgeView;

        holder.tv_content.setText(msg.content);
        holder.tv_title.setText(msg.title);
        if (!msg.hasRead){
            qBadgeView =  new QBadgeView(mContext).bindTarget(holder.itemView.findViewById(R.id.ll_iv)).setBadgeText("");


            holder.ll_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg.hasRead = true;

                    qBadgeView.hide(true);
                    Intent intent = new Intent(mContext,ReadMsgActivity.class);
                    mContext.startActivity(intent);

//                    mContext.startActivity();










//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                    builder.setTitle("问题：");
//                    builder.setMessage("请问你满十八岁了吗?");
//                    builder.setIcon(R.mipmap.ic_launcher_round);
//                    //点击对话框以外的区域是否让对话框消失
//                    builder.setCancelable(true);
//                    //设置正面按钮
//                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            Toast.makeText(context, "你点击了是的", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });


//                    builder.show();
//
                }

            });

        }else {


            holder.ll_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                msg.hasRead = true;


                    mContext.startActivity(new Intent(mContext, LoginActivity.class));

                }
            });
        }


        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ListActivity.this, "删除："+position, Toast.LENGTH_LONG).show();
                removeItem(position);
            }
        });
        //恢复状态
        holder.recyclerViewItem.reset();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MsgRecylerHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
        public LinearLayout ll_msg;
        public TextView show;
        public LinearLayout click;
        public MyRecyclerViewItem recyclerViewItem;
        public View v;
        public ImageView iv_msg;

        public LinearLayout ll_iv;

        public MsgRecylerHolder(View itemView) {

            super(itemView);

            v = itemView;
            ll_msg = itemView.findViewById(R.id.ll_msg);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_title = itemView.findViewById(R.id.tv_title);

            iv_msg = itemView.findViewById(R.id.iv_msg);

            recyclerViewItem=(MyRecyclerViewItem) itemView.findViewById(R.id.scroll_item);
//            show=(TextView) itemView.findViewById(R.id.show);
            click= itemView.findViewById(R.id.click);

            ll_iv = itemView.findViewById(R.id.ll_iv);





        }
    }
    public void addItem(int position, MyMessage msg){
        dataList.add(msg);
        dataList.add(position,msg);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        dataList.remove(position);
//        notifyItemRemoved(position);
        notifyDataSetChanged();
    }



}


