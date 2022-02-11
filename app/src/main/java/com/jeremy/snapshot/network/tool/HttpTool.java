/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：HttpTool.java
 * 包名：com.jeremy.snapshot.network.tool.HttpTool
 * 当前修改时间：2021年12月04日 18:58:37
 * 上次修改时间：2021年12月04日 18:58:36
 */

package com.jeremy.snapshot.network.tool;

public class HttpTool {
    public static String LOGIN_URL="http://49.235.134.191:8080/user/login";  //登录URL
    public static String SAVE_URL="http://49.235.134.191:8080/user/save";   //注册URL
    public static String NEWS_URL="http://49.235.134.191:8080/news/get";  //请求新闻列表URL
    public static String UPLOAD_URL="http://49.235.134.191:8080/file/image/upload";  //图片上传URL
    public static String FBSAVE_URL="http://49.235.134.191:8080/feedback/save";  //随手拍信息上传URL
    public static String FBRECORD_URL="http://49.235.134.191:8080/feedback/get";  //反馈历史记录列表URL
    public static String EVALUATE_URL="http://49.235.134.191:8080/feedback/evaluate";  //发表评价URL
    public static String PROCESS_URL="http://49.235.134.191:8080/feedback/process";  //查看进度URL
}
