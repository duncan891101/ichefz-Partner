package com.playhut.partner.business;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.utils.BitmapUtils;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.FileUtils;
import com.playhut.partner.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 */
public class ChooseAvatarBusiness {

    private Context mContext;

    private Dialog mChooseAvatarDialog;

    private BaseActivity mBaseActivity;

    public static final int ALBUM_REQUEST_CODE = 14344;

    public static final int TAKE_PHOTO_REQUEST_CODE = 12446;

    public static final int CROP_REQUEST_CODE = 15634;

    private int mCropImageWidth;

    private int mCropImageHeight;

    private UserSelectAvatarListener mUserSelectAvatarListener;

    private File mAvatarRootFile;

    public static final String TAKE_PHOTO_TEMP_NAME = "partner_take_photo_temp_";

    public static final String CROP_PHOTO_TEMP_NAME = "partner_crop_photo_temp_";

    public static final String AVATAR_NAME_END_WITH = ".png";

    private Uri mTakePhotoUri;

    private File mSaveFile;

    public ChooseAvatarBusiness(Context context, int cropImageWidth, int cropImageHeight) {
        this.mContext = context;
        this.mCropImageWidth = cropImageWidth;
        this.mCropImageHeight = cropImageHeight;
        mBaseActivity = (BaseActivity) mContext;
        mAvatarRootFile = FileUtils.getAvatarCachePath(context);
    }

    public void showChooseAvatarDialog(UserSelectAvatarListener userSelectAvatarListener) {
        this.mUserSelectAvatarListener = userSelectAvatarListener;
        if (mChooseAvatarDialog == null || !mChooseAvatarDialog.isShowing()) {
            mChooseAvatarDialog = DialogUtils.showChooseAvatarDialog(mContext, R.layout.choose_avatar_dialog_layout, true, false);
        }
        TextView cancelTv = (TextView) mChooseAvatarDialog.findViewById(R.id.tv_cancel);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissChooseAvatarDialog();
            }
        });
        TextView takePhotoTv = (TextView) mChooseAvatarDialog.findViewById(R.id.tv_take_photo);
        takePhotoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拍照上传
                dismissChooseAvatarDialog();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(mAvatarRootFile, TAKE_PHOTO_TEMP_NAME + System.currentTimeMillis());
                mTakePhotoUri = Uri.fromFile(file);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
                mBaseActivity.startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
            }
        });
        TextView fromAlbumTv = (TextView) mChooseAvatarDialog.findViewById(R.id.tv_from_album);
        fromAlbumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 相册选择
                dismissChooseAvatarDialog();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mBaseActivity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
            }
        });
    }

    private void dismissChooseAvatarDialog() {
        if (mChooseAvatarDialog != null && mChooseAvatarDialog.isShowing()) {
            mChooseAvatarDialog.dismiss();
            mChooseAvatarDialog = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                // 相册选择
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri cropUri = data.getData();
                    if (cropUri != null) {
                        initToCrop(cropUri);
                    } else {
                        ToastUtils.show(mContext.getString(R.string.upload_avatar_get_pic_fail));
                    }
                } else {
                    ToastUtils.show(mContext.getString(R.string.upload_avatar_get_pic_fail));
                }
                break;
            case CROP_REQUEST_CODE:
                // 裁剪后回调
                if (resultCode == Activity.RESULT_OK && mSaveFile != null) {
                    Bitmap bitmap = BitmapUtils.getBitmapFromFilePath(mSaveFile, mCropImageWidth * mCropImageHeight);
                    if (bitmap != null) {
                        if (mUserSelectAvatarListener != null) {
                            mUserSelectAvatarListener.select(bitmap, mSaveFile);
                        }
                    } else {
                        ToastUtils.show(mContext.getString(R.string.upload_avatar_crop_pic_fail));
                    }
                } else {
                    ToastUtils.show(mContext.getString(R.string.upload_avatar_crop_pic_fail));
                }
                break;
            case TAKE_PHOTO_REQUEST_CODE:
                // 拍照上传
                if (resultCode == Activity.RESULT_OK) {
                    initToCrop(mTakePhotoUri);
                } else {
                    ToastUtils.show(mContext.getString(R.string.upload_avatar_get_pic_fail));
                }
                break;
        }
    }

    private void initToCrop(Uri cropUri) {
        mSaveFile = new File(mAvatarRootFile, CROP_PHOTO_TEMP_NAME + System.currentTimeMillis() + AVATAR_NAME_END_WITH);
        startPhotoZoom(cropUri);
    }

    /**
     * 图片裁剪
     */
    private void startPhotoZoom(Uri cropUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", mCropImageWidth / mCropImageHeight);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", mCropImageWidth);
        intent.putExtra("outputY", mCropImageHeight);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mSaveFile));
        intent.putExtra("noFaceDetection", true);
        mBaseActivity.startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    public interface UserSelectAvatarListener {
        void select(Bitmap bitmap, File file);
    }

}
