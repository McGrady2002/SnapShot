/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：Record.java
 * 包名：com.jeremy.snapshot.network.entity.Record
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月27日 17:20:57
 */

package com.jeremy.snapshot.network.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Record {
    private int code;
    private String message;
    private List<FeedBack> data;

    public List<FeedBack> getData() {
        return data;
    }

    public void setData(List<FeedBack> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public List<records> getData() {
//        return data;
//    }

//    public void setData(List<records> data) {
//        this.data = data;
//    }

//    public class records{
//        public int getId() {
//            return id;
//        }
//
//        public String getImageUrl() {
//            return imageUrl;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public String getTime() {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            return dateFormat.format(time);
//        }
//
//        public String getCategory() {
//            return category;
//        }
//
//        public int getDegree() {
//            return degree;
//        }
//
//        public String getProcess() {
//            return process;
//        }
//
//        private int id;  //反馈编号
//        private String imageUrl; //图片地址
//        private String title;   //反馈标题
//        private String desc;  //反馈内容
//        private String address; //地址，定位
//        private Date time;  //发布时间
//        private String category; //类别：安全隐患、卫生问题、秩序问题
//        private int degree;  //级别：0-一般，  1-重要
//        private String process; //处理状态
//    }
}
