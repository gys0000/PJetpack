package com.gystry.pjetpack.ui.publish;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gystry.libnavannotation.ActivityDestination;
import com.gystry.pjetpack.R;
import com.gystry.pjetpack.databinding.ActivityLayoutPublishBinding;
import com.gystry.pjetpack.model.TagList;
import com.gystry.pjetpack.ui.mine.TagBottomSheetDialogFragment;

@ActivityDestination(pageUrl = "main/tabs/publish", asStarter = false)
public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLayoutPublishBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_layout_publish);
        mBinding.actionClose.setOnClickListener(this);
        mBinding.actionPublish.setOnClickListener(this);
        mBinding.actionDeleteFile.setOnClickListener(this);
        mBinding.actionAddFile.setOnClickListener(this);
        mBinding.actionAddTag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_close:
                //退出时展示退出弹窗
                showExitDialog();
                break;
            case R.id.action_publish:
                break;
            case R.id.action_add_tag:
                TagBottomSheetDialogFragment tagBottomSheetDialogFragment = new TagBottomSheetDialogFragment();
                tagBottomSheetDialogFragment.setOnTagItemSelectedListener(new TagBottomSheetDialogFragment.OnTagItemSelectedListener() {
                    @Override
                    public void onTagItemSelected(TagList tagList) {

                    }
                });
                tagBottomSheetDialogFragment.show(getSupportFragmentManager(),"tag_dialog");
                break;
            case R.id.action_add_file:
                CaptureActivity.startActivityForResult(this);
                break;
            case R.id.action_delete_file:
                mBinding.actionAddFile.setVisibility(View.VISIBLE);
                mBinding.fileContainer.setVisibility(View.GONE);
                mBinding.cover.setImageDrawable(null);

                break;
        }
    }

    private void showExitDialog() {

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.publish_exit_message))
                .setNegativeButton(getString(R.string.publish_exit_action_cancel), null)
                .setPositiveButton(getString(R.string.publish_exit_action_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PublishActivity.this.finish();
                    }
                }).create().show();
    }
}
