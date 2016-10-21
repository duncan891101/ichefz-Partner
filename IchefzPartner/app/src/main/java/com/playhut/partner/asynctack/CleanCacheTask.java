package com.playhut.partner.asynctack;

import android.content.Context;
import android.os.AsyncTask;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.utils.FileUtils;
import com.playhut.partner.utils.ToastUtils;

import java.io.File;

/**
 *
 */
public class CleanCacheTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    private BaseActivity activity;

    public CleanCacheTask(Context context) {
        this.mContext = context;
        activity = (BaseActivity) context;
    }

    @Override
    protected void onPreExecute() {
        activity.showLoadingDialog(mContext.getString(R.string.loading_dialog_loading), true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        File imageRootFile = FileUtils.getImageCachePath(mContext);
        deleteFile(imageRootFile);
        File avatarRootFile = FileUtils.getAvatarCachePath(mContext);
        deleteFile(avatarRootFile);
        return null;
    }

    private void deleteFile(File imageRootFile){
        if (imageRootFile != null && imageRootFile.exists() && imageRootFile.isDirectory()) {
            File[] files = imageRootFile.listFiles();
            for (File file : files) {
                if (file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.dismissLoadingDialog();
        ToastUtils.show("Clear cache successfully");
    }

}
