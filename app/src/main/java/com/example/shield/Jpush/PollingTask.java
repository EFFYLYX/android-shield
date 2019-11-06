package com.example.shield.Jpush;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by effy on 2018/11/15.
 */

public class PollingTask {

    private static final String TAG = "PollingTask";
    private Handler mHandler;
    private InnerTask mInnerTask;
    private TaskObserver mTaskObserver;
    // 默认心跳时间5秒
    private static final int HEART_BEAT_RATE = 5;
    private long mHeartBeatRate;
    private TaskObservable mObservable;
    /**
     * 创建任务
     * @return  ...
     */
    public PollingTask createTask(){
        createTask(HEART_BEAT_RATE);
        return this;
    }
    /**
     * 创建任务
     * @param heartBeatRate 心跳时间，单位秒
     * @return  ...
     */
    public PollingTask createTask(int heartBeatRate) {
        this.mHeartBeatRate = heartBeatRate * 1000;
        mHandler = new Handler();
        mInnerTask = new InnerTask(this);
        mTaskObserver = new TaskObserver();
        mObservable = new TaskObservable();
        mObservable.addObserver(mTaskObserver);
        return this;
    }
    /**
     * 设置心跳任务时间
     * @param heartBeatRate 心跳时间，单位秒
     * @return  ...
     */
    public PollingTask setHearBeatRate(int heartBeatRate){
        this.mHeartBeatRate = heartBeatRate * 1000;
        return this;
    }
    /**
     * 链接启动任务
     * @return  ...
     */
    public PollingTask connected() {
        if (mHandler == null || mInnerTask == null) {
            throw new RuntimeException("please call createTask method create polling task!");
        }
        if (mHeartBeatRate <= 0) {
            throw new RuntimeException("please set HeartBeatRate by setHearBeatRate method!");
        }
        mHandler.removeCallbacks(mInnerTask);
        mHandler.post(mInnerTask);
        return this;
    }

    public boolean hasStart(){
        return mHeartBeatRate >0;
    }
    /**
     * 通知任务执行完成
     */
    public void notifyTaskFinish(){
        mObservable.notifyTaskFinish();
    }
    /**
     * 销毁任务
     */
    public void destroyTask(){
        if (mHandler == null || mInnerTask == null) {
            throw new RuntimeException("please call createTask method create polling task!");
        }
        mHandler.removeCallbacks(mInnerTask);
    }
    private class InnerTask implements Runnable {
        private WeakReference<PollingTask> mWeak;
        InnerTask(PollingTask wrapper) {
            mWeak = new WeakReference<>(wrapper);
        }
        @Override
        public void run() {
            PollingTask outClass = mWeak.get();
            if (outClass != null) {
                if (outClass.mOnTaskListener != null) {
                    // 开始执行任务
                    outClass.mOnTaskListener.executeTask(outClass);
                }
            }
        }
    }
    /**
     * 任务观察者
     */
    private class TaskObserver implements Observer {
        @Override
        public void update(Observable observable, Object data) {
            // 通过观察者模式，被观察的任务通知了任务轮询观察者，需要去更新开启新的一轮任务的执行，利用handler的postDelayed起到延时心跳的效果
            mHandler.postDelayed(mInnerTask, mHeartBeatRate);
        }
    }



    /**
     * 被观察的任务
     */
    private class TaskObservable extends Observable {
        public void notifyTaskFinish() {
            // 标识状态或者内容发送改变
            setChanged();
            // 通知所有的观察者
            notifyObservers();
        }
    }
    private OnTaskListener mOnTaskListener;
    /**
     * 监听任务状态
     * @param listener  监听接口
     */
    public PollingTask setOnTaskListener(OnTaskListener listener) {
        this.mOnTaskListener = listener;
        return this;
    }
    public interface OnTaskListener {
        void executeTask(PollingTask pollingTask);
    }
}