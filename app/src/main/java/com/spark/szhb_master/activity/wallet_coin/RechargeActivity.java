package com.spark.szhb_master.activity.wallet_coin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.RechargeAddress;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 充币
 */
public class RechargeActivity extends BaseActivity implements CoinContract.rechargeView {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.rlhead)
    RelativeLayout rlhead;

    @BindView(R.id.rlChain)
    RelativeLayout rlChain;

    @BindView(R.id.ivAddress)
    ImageView ivAddress;

    @BindView(R.id.llAlbum)
    LinearLayout llAlbum;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvCopy)
    TextView tvCopy;

    @BindView(R.id.tvChainName)
    TextView tvChainName;

    private CoinContract.rechargePresenter rechargePresenter;

    @Override
    public void setPresenter(CoinContract.rechargePresenter presenter) {
        rechargePresenter = presenter;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        setImmersionBar(rlhead);
    }

    private Bitmap saveBitmap;

    @Override
    protected void initData() {
        super.initData();
        new RechargePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @OnClick({R.id.ar_iv_close,R.id.tvCopy, R.id.llAlbum,R.id.rlChain})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.tvCopy:
                copy();
                break;
            case R.id.llAlbum:
                if (!PermissionUtils.isCanUseStorage(activity))
                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                else try {
                    save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlChain:
                actionSheetDialogNoTitle();
                break;
        }
    }

    private int chainType = 0;
    private RechargeAddress rechargeAddress;
    @Override
    public void getRechargeAddressSuccess(RechargeAddress rechargeAddress) {
        this.rechargeAddress = rechargeAddress;

        ivAddress.post(new Runnable() {
            @Override
            public void run() {
                if (rechargeAddress == null)
                    return;

                saveBitmap = createQRCode(getUrl(), Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                ivAddress.setImageBitmap(saveBitmap);
            }
        });

        tvAddress.setText(getUrl());
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this,code,toastMessage);
    }

    private String getUrl(){
        if (chainType == 0)
            return rechargeAddress.getErc_address();
        else
            return rechargeAddress.getOmni_address();
    }

    /**
     */
    private void actionSheetDialogNoTitle() {
        final String[] stringItems = {"USDT-ERC20", "USDT-Omni"};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        chainType = 0;
                        tvChainName.setText("USDT-ERC20");

                        if (rechargeAddress != null){
                            saveBitmap = createQRCode(getUrl(), Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                            ivAddress.setImageBitmap(saveBitmap);
                            tvAddress.setText(getUrl());
                        }

                        break;
                    case 1:
                        chainType = 1;
                        tvChainName.setText("USDT-Omni");

                        if (rechargeAddress != null){
                            saveBitmap = createQRCode(getUrl(), Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                            ivAddress.setImageBitmap(saveBitmap);
                            tvAddress.setText(getUrl());
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
    }


    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(activity).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    try {
                        save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    ToastUtils.showToast(getString(R.string.storage_permission));
                    break;
            }
        }
    };

    /**
     * 保存到相册
     */
    private void save() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cayman/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (saveBitmap != null) try {
            BitmapUtils.saveBitmapToFile(saveBitmap, file, 100);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showToast(getString(R.string.savesuccess));
        }
    }

    /**
     * 复制
     */
    private void copy() {
        CommonUtils.copyText(this, tvAddress.getText().toString());
        ToastUtils.showToast(R.string.copy_success);
    }


    @Override
    protected void loadData() {
        rechargePresenter.getRechargeAddress();
    }

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);   //设置白边大小 取值为 0- 4 越大白边越大
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
