package com.playhut.partner.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.playhut.partner.entity.MessageDetailEntity;
import com.playhut.partner.ui.MessageDetailChildView;
import com.playhut.partner.utils.DialogUtils;
import com.playhut.partner.utils.ImageLoderOptionUtils;

import java.util.List;

/**
 *
 */
public class MessageDetailAdapter extends BaseAdapter {

    private Context mContext;

    private List<MessageDetailEntity.Message> mList;

    private ImageLoader mImageLoader;

    private DisplayImageOptions mOptions;

    private Dialog mSendMsgDialog;

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

        public void setPosition(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            showReplyDialog();
        }

    }

    private void showReplyDialog() {
        if (mSendMsgDialog == null || !mSendMsgDialog.isShowing()) {
            mSendMsgDialog = DialogUtils.showSendMessageDialog(mContext, R.layout.message_reply_dialog_layout, true, false);
        }
        final ImageView imageView = (ImageView) mSendMsgDialog.findViewById(R.id.iv_avatar);
        final TextView msgTv = (TextView) mSendMsgDialog.findViewById(R.id.tv_name);
        final EditText msgEt = (EditText) mSendMsgDialog.findViewById(R.id.et_msg);
        Button sendBtn = (Button) mSendMsgDialog.findViewById(R.id.btn_send);
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
