/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：FeedbackActivity.java
 * 包名：com.jeremy.snapshot.ui.activity.FeedbackActivity
 * 当前修改时间：2021年11月29日 00:01:41
 * 上次修改时间：2021年11月28日 21:49:41
 */

package com.jeremy.snapshot.ui.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeremy.snapshot.R;
import com.jeremy.snapshot.adapter.PicturesAdapter;
import com.jeremy.snapshot.network.entity.FeedBack;
import com.jeremy.snapshot.network.tool.HttpTool;
import com.jeremy.snapshot.ui.addPhotoBottomDialog;
import com.jeremy.snapshot.ui.fragment.HomePageFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.unstoppable.submitbuttonview.SubmitButton;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.shaohui.bottomdialog.BottomDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private SubmitButton submitButton;
    private EditText title;
    private EditText content;
    private ImageView back;
    private ImageView addphoto;
    private ImageView imageLocation;
    private Button takephoto;
    private Button choosePic;
    private Button cancle;
    private TextView locationText;
    private TextView choosePosition;
    private addPhotoBottomDialog dialog;
    private PicturesAdapter picturesAdapter;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String currentPhotoPath;
    private RadioButton common_button;
    private RadioButton important_button;
    private RadioGroup degree;
    private NiceSpinner niceSpinner;
    private String uploadUrl = "";

    List<Uri> mselect = new ArrayList<>();

    List<String> path = new ArrayList<>();

    private Uri imageuri;

    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;

    public static final int MAX_LENGTH = 9;  //最大上传照片数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_feedback);
        initviews();
        initmap();

    }

    //region 初始化控件
    private void initviews() {
        submitButton = (SubmitButton) findViewById(R.id.btn_sub_feedback);
        title = (EditText) findViewById(R.id.fed_title);
        content = (EditText) findViewById(R.id.fed_content);
        back = (ImageView) findViewById(R.id.fb_back);
        takephoto = (Button) findViewById(R.id.btn_takephoto);
        choosePic = (Button) findViewById(R.id.btn_files);
        cancle = (Button) findViewById(R.id.btn_cancle);
        addphoto = (ImageView) findViewById(R.id.addpicture);
        locationText = (TextView) findViewById(R.id.location_text);
        imageLocation = (ImageView) findViewById(R.id.image_location);
        degree = (RadioGroup) findViewById(R.id.rg_degree);
        choosePosition = (TextView) findViewById(R.id.choose_position);
        imageLocation.setOnClickListener(this);
        addphoto.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        locationText.setOnClickListener(this);
        back.setOnClickListener(this);
        choosePosition.setOnClickListener(this);
        dialog = new addPhotoBottomDialog();
        niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("安全隐患", "卫生问题", "秩序问题"));
        niceSpinner.attachDataSource(dataset);
        picturesAdapter = new PicturesAdapter(path);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_pictures);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(picturesAdapter);
    }
    //endregion

    //region map初始化
    private void initmap() {
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        //mLocationClient.start();
        checkpermission();
    }
    //endregion

    //region 自定义LocationListener
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append(location.getProvince())
                    .append(location.getCity()).append(location.getDistrict())
                    .append(location.getStreet())
                    .append(location.getTown())
            ;
            locationText.setText(currentPosition);

//            Poi poi = location.getPoiList().get(0);
//            String poiName = poi.getName();    //获取POI名称
//            String poiTags = poi.getTags();    //获取POI类型
//            String poiAddr = poi.getAddr();    //获取POI地址 //获取周边POI信息
//            Log.d("POIA", location.getPoiList().size() + "");
//            for (int i = 0; i < location.getPoiList().size(); i++) {
//                Log.d("POIA", location.getPoiList().get(i).getAddr() + "\n");
//            }
            mLocationClient.stop();
        }
    }
    //endregion

    //region 检查定位权限，动态申请
    public void checkpermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(FeedbackActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(FeedbackActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(FeedbackActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(FeedbackActivity.this, permissions, 2);
        } else {
            mLocationClient.start();
        }
    }
    //endregion

    //region onClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sub_feedback:
                //submitButton.doResult(true);
                if (title.getText().toString().equals("") || content.getText().toString().equals("")) {
                    Toasty.error(FeedbackActivity.this, "请输入标题和问题描述", Toasty.LENGTH_SHORT).show();
                    submitButton.reset();
                    break;
                }
                if (path.size() > 0)
                    postImage();
                else
                    postInfo();
                break;
            case R.id.addpicture:
                show();
                break;
            case R.id.fb_back:
                finish();
                break;
            case R.id.image_location:
                //checkpermission();
                break;
            case R.id.location_text:
                //checkpermission();
//                Intent intent=new Intent(FeedbackActivity.this,LocationActivity.class);
//                startActivity(intent);
                break;
            case R.id.choose_position:
                Intent intent = new Intent(FeedbackActivity.this, LocationActivity.class);
                startActivityForResult(intent, 3);
                break;
            default:
                break;
        }
    }
    //endregion

    //region 拍照
    private void takePictures() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outputImage = new File(getExternalCacheDir(), "output_image" + timeStamp + ".jpg");
        try {
            if (outputImage.exists())
                outputImage.delete();
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageuri = FileProvider.getUriForFile(FeedbackActivity.this,
                    "com.jeremy.snapshot.fileprovider", outputImage);
        } else {
            imageuri = Uri.fromFile(outputImage);
        }
        currentPhotoPath = outputImage.getAbsolutePath();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(intent, TAKE_PHOTO);
    }
    //endregion

    //region 弹出选择拍照或相册的底部对话框
    private void show() {
        BottomDialog dialog = BottomDialog.create(getSupportFragmentManager());
        dialog.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                v.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btn_takephoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePictures();
                        dialog.dismiss();
                    }
                });
                v.findViewById(R.id.btn_files).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //打开相册，动态申请权限
                        if (ContextCompat.checkSelfPermission(FeedbackActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(FeedbackActivity.this, new String[]
                                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.setLayoutRes(R.layout.takephoto_layout).show();
    }
    //endregion

    //region 打开相册
    private void openAlbum() {
        Matisse.from(FeedbackActivity.this)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .showPreview(false) // Default is `true`
                .forResult(CHOOSE_PHOTO);
    }
    //endregion

    //region 获取图片路径
    @TargetApi(19)
    private void handleImageOnKitKat(Uri data, int i) {
        String imagePath = null;
        Uri uri = data;
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        path.add(imagePath);
    }

    private void handleImageBeforeKitKat(Uri data) {
        Uri uri = data;
        String imagePath = getImagePath(uri, null);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //endregion

    //region 上传图片
    private void postImage() {
        OkGo.<String>post(HttpTool.UPLOAD_URL)
                .tag(this)
                .params("file",new File(path.get(0)))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                int code = jsonObject.getInt("code");
                                if (code == 200) {
                                    uploadUrl = jsonObject.getString("data");
                                    showResponse("图片上传成功,正在上传其余信息...",2);
                                } else {
                                    showResponse("图片上传失败",0);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        postInfo();
                    }
                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        showResponse("网络连接错误",0);
                        postInfo();
                    }
                });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String imageType = "multipart/form-data";
//                    File file = new File(path.get(0));
//                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("file", file.getName(), fileBody)
//                            .addFormDataPart("imageType", imageType)
//                            .build();
//                    Log.d("FileName",file.getName());
//                    final Request request = new Request.Builder()
//                            .url(HttpTool.UPLOAD_URL)
//                            .post(requestBody).build();
//                    Call call = client.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                            Log.d("TAG", "上传图片失败");
//                            showResponse("图片上传失败",0);
//                            postInfo();
//                        }
//
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                            Log.d("TAG", "上传图片成功");
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                int code = jsonObject.getInt("code");
//                                if (code == 200) {
//                                    uploadUrl = jsonObject.getString("data");
//                                    showResponse("图片上传成功,正在上传其余信息...",2);
//                                } else {
//                                    showResponse("图片上传失败",0);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            postInfo();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
    //endregion

    //region 显示上传结果
    //  n==1成功 0失败 2上传中
    private void showResponse(String str,int n) {
                Toast.makeText(FeedbackActivity.this, str, Toast.LENGTH_SHORT).show();
                if (n==1)
                    submitButton.doResult(true);
                else if (n==2)
                    ;
                else
                    submitButton.doResult(false);
    }
    //endregion

    //region 上传随手拍信息
    private void postInfo() {
        MediaType JSON = MediaType.parse("application/json");
        long id = 0;
        int i = 0;
        if (degree.getCheckedRadioButtonId()==R.id.important)
            i=1;
        Gson gson=new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .create();
        FeedBack feedBack=new FeedBack(uploadUrl,title.getText().toString(),content.getText().toString()
                ,MainActivity.account,locationText.getText().toString(),niceSpinner.getSelectedItem().toString(),
                i,new Date(System.currentTimeMillis()),"已提交");
        String obj=gson.toJson(feedBack);
        JSONObject json=null;
        Log.d("TAG", obj);
        try {
             json = new JSONObject(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        OkGo.<String>post(HttpTool.FBSAVE_URL)
                .tag(this)
                .upRequestBody(requestBody)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 200) {
                                showResponse("所有信息上传成功",1);
                            } else {
                                showResponse("其余信息上传失败",0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        showResponse("其余信息上传失败",0);
                    }
                });
        //region 废弃
        //        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    MediaType JSON = MediaType.parse("application/json");
//                    long id = 0;
//                    int i = 0;
//                    if (degree.getCheckedRadioButtonId()==R.id.important)
//                        i=1;
//                    Gson gson=new GsonBuilder()
//                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
//                            .create();
//                    FeedBack feedBack=new FeedBack(uploadUrl,title.getText().toString(),content.getText().toString()
//                    ,MainActivity.account,locationText.getText().toString(),niceSpinner.getSelectedItem().toString(),
//                            i,new Date(System.currentTimeMillis()),"已提交");
//
//                    String obj=gson.toJson(feedBack);
//                    Log.d("TAG", obj);
//
//                    JSONObject json = new JSONObject(obj);
////                    try {
////                        json.put("id", id);
////                        json.put("imageUrl", uploadUrl);
////                        json.put("title", title.getText().toString());
////                        json.put("desc", content.getText().toString());
////                        json.put("account", MainActivity.account);
////                        json.put("address", locationText.getText().toString());
////                        json.put("category", niceSpinner.getSelectedItem().toString());
////                        json.put("degree", i);
////                        json.put("time", date);
//////                       json.put("time", new java.sql.Date(System.currentTimeMillis()));
////                        json.put("process", "已提交");
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
//                    Log.d("TAG", String.valueOf(json));
//                    final Request request = new Request.Builder()
//                            .url(HttpTool.FBSAVE_URL)
//                            .post(requestBody).build();
//                    Call call = client.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                            Log.d("TAG", "失败");
//                            showResponse("其余信息上传失败",0);
//                        }
//
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                            Log.d("TAG", "成功");
//                            //Log.d("TAG1", response.body().string());
//                            try {
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                int code = jsonObject.getInt("code");
//                                if (code == 200) {
//                                    showResponse("所有信息上传成功",1);
//                                } else {
//                                    showResponse("其余信息上传失败",0);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        //endregion
    }
    //endregion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    mLocationClient.start();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.d("Success", "aa:" + currentPhotoPath);
                    path.add(currentPhotoPath);
                    picturesAdapter.notifyDataSetChanged();
                    break;
                }
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mselect = Matisse.obtainResult(data);
                    Log.d("Tag", "mselect:" + mselect);
                    Log.d("FeedbackActivity", "CHOOSE");
                    for (int i = 0; i < mselect.size(); i++) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            handleImageOnKitKat(mselect.get(i), i);
                        } else {
                            handleImageBeforeKitKat(mselect.get(i));
                        }
                    }
                    picturesAdapter.notifyDataSetChanged();
                    for (int i = 0; i < path.size(); i++) {
                        Log.d("pathtag", "path:" + path.get(i));
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    locationText.setText(data.getStringExtra("location_return"));
                }
                break;
            default:
                break;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}