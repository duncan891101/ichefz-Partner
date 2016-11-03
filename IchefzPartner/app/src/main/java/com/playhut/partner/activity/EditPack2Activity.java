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
public class EditPack2Activity extends BaseActivity implements View.OnClickListener {

    private static MyMenuPackEntity.PackInfo mPackInfo;

    public static void actionIntent(Context context, MyMenuPackEntity.PackInfo packInfo) {
        mPackInfo = packInfo;
        context.startActivity(new Intent(context, EditPack2Activity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_pack2);

        findViewById(R.id.rl_tip).setOnClickListener(this);
        findViewById(R.id.rl_ingredient).setOnClickListener(this);
        findViewById(R.id.rl_instruction).setOnClickListener(this);
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
                EditPack2Activity.this.finish();
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
        switch (v.getId()) {
            case R.id.rl_tip:
                Intent tipIntent = new Intent(EditPack2Activity.this, EditTipActivity.class);
                tipIntent.putExtra(EditTipActivity.MENU_ID_INTENT, mPackInfo.pack_id);
                tipIntent.putExtra(EditTipActivity.ADD_TYPE_INTENT, EditTipActivity.ADD_TIPS_TYPE);
                startActivity(tipIntent);
                break;
            case R.id.rl_ingredient:
                Intent ingredientIntent = new Intent(EditPack2Activity.this, EditTipActivity.class);
                ingredientIntent.putExtra(EditTipActivity.MENU_ID_INTENT, mPackInfo.pack_id);
                ingredientIntent.putExtra(EditTipActivity.ADD_TYPE_INTENT, EditTipActivity.ADD_INGREDIENT_TYPE);
                startActivity(ingredientIntent);
                break;
            case R.id.rl_instruction:
                Intent instructionIntent = new Intent(EditPack2Activity.this, EditTipActivity.class);
                instructionIntent.putExtra(EditTipActivity.MENU_ID_INTENT, mPackInfo.pack_id);
                instructionIntent.putExtra(EditTipActivity.ADD_TYPE_INTENT, EditTipActivity.ADD_INSTRUCTION_TYPE);
                startActivity(instructionIntent);
                break;
        }
    }
}
