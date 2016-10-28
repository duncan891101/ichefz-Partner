package com.playhut.partner.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.playhut.partner.R;
import com.playhut.partner.activity.MessageDetailActivity;
import com.playhut.partner.base.BaseActivity;
import com.playhut.partner.base.PartnerApplication;
import com.playhut.partner.entity.MessageDetailEntity;
import com.playhut.partner.mvp.presenter.IReplyPresent;
import com.playhut.partner.mvp.presenter.impl.ReplyPresent;
import com.playhut.partner.mvp.view.ReplyView;
import com.playhut.partner.network.IchefzException;
import com.playhut.partner.ui.MessageDetailChildView;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ImageLoderOptionUtils;
import com.playhut.partner.utils.ToastUtils;

import java.util.List;

/**
 *
 */
public class MessageDetailAdapter extends BaseAdapter {

    private Context mContext;

    private List<MessageDetailEntity.Message> mList;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private Dialog mReplyDialog;

    public MessageDetailAdapter(Context context, List<MessageDetailEntity.Message> list) {
        this.mContext = context;
        this.mList = list;
        mImageLoader = ImageLoader.getInstance();
        mOptions = ImageLoderOptionUtils.setOptions(R.drawable.loading_image_shape, 0, R.drawable.loading_image_shape);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        AllCheckListener allCheckListener = null;
        ReplyClickListener replyClickListener = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.message_detail_item_layout, null);
            holder = new Holder();

            holder.mAllCb = (CheckBox) convertView.findViewById(R.id.cb_all);
            allCheckListener = new AllCheckListener();
            holder.mAllCb.setOnCheckedChangeListener(allCheckListener);

            holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.tv_time);
            holder.mContentTv = (TextView) convertView.findViewById(R.id.tv_content);

            holder.mReplyTv = (TextView) convertView.findViewById(R.id.tv_reply);
            replyClickListener = new ReplyClickListener();
            holder.mReplyTv.setOnClickListener(replyClickListener);

            holder.mContainerLayout = (LinearLayout) convertView.findViewById(R.id.ll_container);
            holder.mLineView = convertView.findViewById(R.id.view_line);

            convertView.setTag(holder);
            convertView.setTag(holder.mAllCb.getId(), allCheckListener);
            convertView.setTag(holder.mReplyTv.getId(), replyClickListener);
        } else {
            holder = (Holder) convertView.getTag();
            allCheckListener = (AllCheckListener) convertView.getTag(holder.mAllCb.getId());
            replyClickListener = (ReplyClickListener) convertView.getTag(holder.mReplyTv.getId());
        }

        allCheckListener.setPosition(position);
        replyClickListener.setPosition(position);

        MessageDetailEntity.Message entity = mList.get(position);

        if (entity.sender_id.equals("0")) {
            // 系统消息
            holder.mReplyTv.setVisibility(View.GONE);
        } else {
            holder.mReplyTv.setVisibility(View.VISIBLE);
        }

        holder.mAllCb.setVisibility(entity.isShow ? View.VISIBLE : View.GONE);
        holder.mAllCb.setChecked(entity.isCheck);

        holder.mNameTv.setText(entity.first_name + " " + entity.last_name);

        holder.mTimeTv.setText(entity.time);

        holder.mContentTv.setText(entity.content);

        holder.mContainerLayout.removeAllViews();
        List<MessageDetailEntity.Child> childList = entity.child;
        if (childList != null && childList.size() > 0) {
            holder.mLineView.setVisibility(View.VISIBLE);
            addChildToLayout(holder.mContainerLayout, entity, childList, entity.isShow);
        } else {
            holder.mLineView.setVisibility(View.GONE);
        }

        mImageLoader.displayImage(entity.profile_picture, holder.mImageView, mOptions);

        return convertView;
    }

    private void addChildToLayout(LinearLayout containerLayout,
                                  final MessageDetailEntity.Message entity, final List<MessageDetailEntity.Child> childList, boolean isShow) {
        for (final MessageDetailEntity.Child child : childList) {
            MessageDetailChildView childView = new MessageDetailChildView(mContext);
            childView.setCheckBoxVisible(isShow);
            childView.setCheckBoxState(child.isCheck);
            childView.setImageView(child.profile_picture);
            childView.setInfo(child.first_name + " " + child.last_name, child.time, child.content);
            childView.setChildCheckListener(new MessageDetailChildView.ChildCheckListener() {
                @Override
                public void onCheckChange(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        // 过滤了setCheck代码调用时执行该回调
                        child.isCheck = isChecked;

                        if (isAllChildCheck(childList)) {
                            entity.isCheck = true;
                        } else {
                            entity.isCheck = false;
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            containerLayout.addView(childView);
        }
    }

    private boolean isAllChildCheck(List<MessageDetailEntity.Child> childList) {
        int count = 0;
        for (int i = 0; i < childList.size(); i++) {
            MessageDetailEntity.Child child = childList.get(i);
            if (child.isCheck) {
                count++;
            }
        }
        return count == childList.size();
    }

    private class AllCheckListener implements CompoundButton.OnCheckedChangeListener {

        private int mPosition;

        public void setPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                // 过滤了setCheck代码调用时执行该回调
                MessageDetailEntity.Message entity = mList.get(mPosition);
                entity.isCheck = isChecked;
                List<MessageDetailEntity.Child> childList = entity.child;
                if (childList != null && childList.size() > 0) {
                    for (MessageDetailEntity.Child child : childList) {
                        child.isCheck = isChecked;
                    }
                }
                notifyDataSetChanged();
            }
        }

    }

    private class ReplyClickListener implements View.OnClickListener {

        private int mPosition;

        public void setPosition(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            showReplyDialog(mPosition);
        }

    }

    private void showReplyDialog(final int position) {
        if (mReplyDialog == null || !mReplyDialog.isShowing()) {
            mReplyDialog = DialogUtils.showRelayDialog(mContext, R.layout.reply_dialog_layout, true, true);
        }

        final EditText replyEt = (EditText) mReplyDialog.findViewById(R.id.et_reply);
        replyEt.setFocusable(true);
        replyEt.requestFocus();

        mReplyDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(replyEt, 0);
            }
        });

        mReplyDialog.show();

        final Button reply = (Button) mReplyDialog.findViewById(R.id.btn_reply);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissReplyDialog();
                String inputText = replyEt.getText().toString().trim();
                if (!TextUtils.isEmpty(inputText)) {
                    String senderId = PartnerApplication.mAccount.getChef_id();
                    String parentId = mList.get(position).message_id;
                    toReply(senderId, inputText, parentId);
                } else {
                    ToastUtils.show("Input text cannot be empty");
                }
            }
        });
    }

    private void dismissReplyDialog() {
        if (mReplyDialog != null && mReplyDialog.isShowing()) {
            mReplyDialog.dismiss();
            mReplyDialog = null;
        }
    }

    private void toReply(String senderId, String content, String parentId) {
        IReplyPresent present = new ReplyPresent(mContext, new ReplyView() {
            @Override
            public void startLoading() {
                BaseActivity activity = (BaseActivity) mContext;
                activity.showLoadingDialog(mContext.getString(R.string.loading_dialog_loading), true);
            }

            @Override
            public void loadSuccess() {
                ToastUtils.show("Send message successfully");
                MessageDetailActivity activity = (MessageDetailActivity) mContext;
                activity.doRefresh();
            }

            @Override
            public void finishLoading() {
                BaseActivity activity = (BaseActivity) mContext;
                activity.dismissLoadingDialog();
            }

            @Override
            public void loadFailure(IchefzException exception) {
                ToastUtils.show(exception.getErrorMsg());
            }
        });
        present.reply(senderId, content, parentId);
    }

    static class Holder {
        private CheckBox mAllCb;
        private ImageView mImageView;
        private TextView mNameTv;
        private TextView mTimeTv;
        private TextView mContentTv;
        private TextView mReplyTv;
        private LinearLayout mContainerLayout;
        private View mLineView;
    }

}
