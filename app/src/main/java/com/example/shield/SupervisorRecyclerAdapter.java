package com.example.shield;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/**
 * Created by effy on 2018/10/29.
 */

class SupervisorRecyclerAdapter extends RecyclerView.Adapter<SupervisorRecyclerAdapter.TabRecyclerHolder>{
    private Context mContext;
    private List<User> dataList = new ArrayList<>();
//    private onRecyclerItemClickerListener mListener;

    private LayoutInflater mInflater;
    String phone;


    public SupervisorRecyclerAdapter(String phone, Context context, List dataList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.phone=phone;
    }


    @Override
    public TabRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = mInflater.inflate(R.layout.contact_item, parent, false);


        return new SupervisorRecyclerAdapter.TabRecyclerHolder(item);
    }

    @Override
    public void onBindViewHolder(TabRecyclerHolder holder, final int position) {
        final User user = dataList.get(position);


        System.out.println("supervisorAdapter ====\n"+ user.getUsername());


        String first = user.getFirstSymbol();

        holder.imageView.setImageDrawable(new ColorCircleDrawable(first));

holder.tv_phone.setText(user.phoneNo);
        holder.tv_name.setText(user.getUsername());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyTask myTask= new MyTask(position, phone, user.phoneNo);
                myTask.execute();
            }
        });
        //恢复状态
        holder.recyclerViewItem.reset();
    }

    public void addItem(int position,User s) {
        dataList.add(position,s);
        notifyDataSetChanged();
        // uns.add(position, accountNo);
//        bookComments.add(position, bookComment);
//        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        dataList.remove(position);
//        notifyItemRemoved(position);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TabRecyclerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        TextView tv_name;
        TextView tv_phone;
        public LinearLayout click;
        public MyRecyclerViewItem recyclerViewItem;

        public TabRecyclerHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mHeader_iv);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);

            recyclerViewItem=(MyRecyclerViewItem) itemView.findViewById(R.id.scroll_item);
//            show=(TextView) itemView.findViewById(R.id.show);
            click= itemView.findViewById(R.id.click);

        }
    }


    private class MyTask extends AsyncTask<Void, Integer, Boolean> {
        String phone;
        String otherPhone;
        int position;

        public MyTask(int posistion, String phone, String otherPhone){
            this.position=posistion;
            this.phone=phone;
            this.otherPhone=otherPhone;
        }
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPreExecute() called");
//            textView.setText("loading...");
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("TAG", "doInBackground(Params... params) called");



//
            return request.DeleteEmergency(phone,otherPhone);
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {
//            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
//            progressBar.setProgress(progresses[0]);
//            textView.setText("loading..." + progresses[0] + "%");
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Boolean result) {

            if (result == false){
                Toasty.error(mContext, "Error ", Toast.LENGTH_SHORT, true).show();

            }else{
                removeItem(position);
            }



//            Log.i(TAG, "onPostExecute(Result result) called");
//            textView.setText(result);
//
//            execute.setEnabled(true);
//            cancel.setEnabled(false);
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
//            Log.i(TAG, "onCancelled() called");
//            textView.setText("cancelled");
//            progressBar.setProgress(0);
//
//            execute.setEnabled(true);
//            cancel.setEnabled(false);
        }
    }



}

