package com.playhut.partner.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.playhut.partner.R;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.entity.MyMenuPackEntity;
import com.playhut.partner.widget.PartnerTitleBar;

/**
 *
 */
public class EditPack1Activity extends BaseActivity implements View.OnClickListener{

    private static MyMenuPackEntity.PackInfo mPackInfo;

    public static void actionIntent(Context context, MyMenuPackEntity.PackInfo packInfo){
        mPackInfo = packInfo;
        context.startActivity(new Intent(context, EditPack1Activity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_pack);

        findViewById(R.id.rl_base_info).setOnClickListener(this);
        findViewById(R.id.rl_tip).setOnClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        PartnerTitleBar titleBar = new PartnerTitleBar(this);
        titleBar.setCenterTvVisiable(true);
        titleBar.setCenterTvContent("Edit pack");
        titleBar.setLeftIvVisiable(true);
        titleBar.setLeftIvContent(R.mipmap.back_black);
        titleBar.setLeftIvClickListener(new PartnerTitleBar.TitleBarClickListener() {
            @Override
            public void onClick() {
                EditPack1Activity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLogic() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_base_info:
                AddPackActivity.actionIntent(EditPack1Activity.this, mPackInfo);
                break;
            case R.id.rl_tip:
                EditPack2Activity.actionIntent(EditPack1Activity.this, mPackInfo);
                break;
        }
    }
}
