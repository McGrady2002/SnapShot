/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：Process.java
 * 包名：com.jeremy.snapshot.network.entity.Process
 * 当前修改时间：2021年12月01日 21:57:04
 * 上次修改时间：2021年12月01日 21:57:03
 */

package com.jeremy.snapshot.network.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Process {

    private int code;
    private String message;
    private List<items> data;

    public Process() {
        data=new ArrayList<>();
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

    public List<items> getData() {
        return data;
    }

    public void setData(List<items> data) {
        this.data = data;
    }

    public class items{
        private int id;
        private int feedBackId;
        private String desc;
        private Date time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFeedBackId() {
            return feedBackId;
        }

        public void setFeedBackId(int feedBackId) {
            this.feedBackId = feedBackId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTime() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(time);
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        private String imageUrl;
    }



}
