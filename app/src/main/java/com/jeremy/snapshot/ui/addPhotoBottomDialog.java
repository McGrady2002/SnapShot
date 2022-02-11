/*
 * 项目名：福大随手拍(SnapShot)
 * 作者：张晋铭
 * 类名：addPhotoBottomDialog.java
 * 包名：com.jeremy.snapshot.ui.addPhotoBottomDialog
 * 当前修改时间：2021年11月29日 00:02:20
 * 上次修改时间：2021年10月24日 23:51:52
 */

package com.jeremy.snapshot.ui;

import android.view.View;

import com.jeremy.snapshot.R;

import me.shaohui.bottomdialog.BaseBottomDialog;

public class addPhotoBottomDialog extends BaseBottomDialog implements View.OnClickListener {
    @Override
    public int getLayoutRes() {
        return R.layout.takephoto_layout;
    }

    @Override
    public void bindView(View v) {
        v.findViewById(R.id.btn_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancle:
                this.dismiss();
        }
    }
}
