/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：Bean.java
 * 包名：com.jeremy.snapshot.network.entity.Bean
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年11月28日 09:19:11
 */

package com.jeremy.snapshot.network.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bean implements Serializable {
    public int code;

    public String message;

    public List<News> data;

    public static class News {
        private int id;

        private String imageUrl;

        private String title;

        private String desc;

        private String publishAccount;

        public Date getPublishTime() {
            return publishTime;
        }

        private Date publishTime;

        public News(int id, String imageUrl, String title, String desc, String publishAccount, Date date) {
            this.id = id;
            this.imageUrl = imageUrl;
            this.title = title;
            this.desc = desc;
            this.publishAccount = publishAccount;
            this.publishTime = date;
        }

        public int getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getPublishAccount() {
            return publishAccount;
        }

        public String getDate() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(publishTime);
        }

        public String getTime() {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            return dateFormat.format(publishTime);
        }
    }
}
