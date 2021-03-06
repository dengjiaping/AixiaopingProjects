package com.weslide.lovesmallscreen.activitys.sellerinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.weslide.lovesmallscreen.Constants;
import com.weslide.lovesmallscreen.R;
import com.weslide.lovesmallscreen.URIResolve;
import com.weslide.lovesmallscreen.activitys.HomeActivity;
import com.weslide.lovesmallscreen.activitys.UnlockActivity;
import com.weslide.lovesmallscreen.activitys.mall.SellerActivity;
import com.weslide.lovesmallscreen.activitys.withdrawals.CashActivity;
import com.weslide.lovesmallscreen.core.BaseActivity;
import com.weslide.lovesmallscreen.models.Goods;
import com.weslide.lovesmallscreen.models.RedPaper;
import com.weslide.lovesmallscreen.utils.AppUtils;
import com.weslide.lovesmallscreen.utils.StringUtils;
import com.weslide.lovesmallscreen.views.custom.SuperGridView;

import net.aixiaoping.unlock.views.UnlockView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/12/10.
 * 红包详情
 */
public class RedPaperDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_seller_img)
    ImageView ivSellerImg;
    @BindView(R.id.tv_seller_name)
    TextView tvSellerName;
    @BindView(R.id.tv_seller_message)
    TextView tvSellerMessage;
    @BindView(R.id.tv_seller_money)
    TextView tvSellerMoney;
    @BindView(R.id.supergride)
    SuperGridView supergride;
    @BindView(R.id.tv_yuan)
    TextView tvYuan;
    @BindView(R.id.iv_come)
    ImageView ivCome;
    @BindView(R.id.red_status2)
    LinearLayout red_status2;
    @BindView(R.id.red_status1)
    LinearLayout red_status1;
    private int seetingId;
    private String sellerId, URL;
    List<Goods> data = new ArrayList<>();
    private Thread thread;
    private boolean exit = false;
    private int status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newredpaper_detail);
        ButterKnife.bind(this);
//        seetingId = getIntent().getIntExtra("settingId", 0);
        status = getIntent().getIntExtra("status", 0);
        openAdminRedPaper();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                if (!exit) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toStore();
                            RedPaperDetailsActivity.this.finish();
                        }
                    });
                }
            }
        });
        if (status == 1) {
            thread.start();
        }
    }

    @OnClick({R.id.tv_back, R.id.tv_wallet, R.id.iv_come, R.id.iv_come2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                exit = true;
                AppUtils.toActivity(this, UnlockActivity.class);
                this.finish();
                break;
            case R.id.tv_wallet:
                exit = true;
                // URIResolve.resolve(RedPaperDetailsActivity.this, HTTP.URL_MONEY_LINK_LIST + HTTP.formatJSONData(new Request()));
                AppUtils.toActivity(RedPaperDetailsActivity.this, CashActivity.class);
                this.finish();
                break;
            case R.id.iv_come:
                exit = true;
                toStore();
                this.finish();
                break;
            case R.id.iv_come2:
                AppUtils.toActivity(this, HomeActivity.class);
                this.finish();
                break;
        }
    }

    private void toStore() {
        if (!StringUtils.isBlank("sellerId")) {
            if (sellerId.equals("0")) {
                if (StringUtils.isBlank(URL)) {
                    //进入首页
                    AppUtils.toActivity(this, HomeActivity.class);
                } else {
                    //查看网页
                    URIResolve.resolve(RedPaperDetailsActivity.this, URL);

                }
            } else {
                //进店逛逛
                Intent intent = new Intent(this, SellerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("seller_id", sellerId);
                bundle.putString("back_type", Constants.SELLER_ACTIVITY_HOME_BACK);
                intent.putExtras(bundle);
                startActivity(intent);
//                AppUtils.toSeller(this, sellerId);
            }
        }
    }

    private void openAdminRedPaper() {
        /*Request<RedPaper> request = new Request<>();
        RedPaper redPaper = new RedPaper();
        redPaper.setSettingId(seetingId);
        redPaper.setType("6");
        request.setData(redPaper);
        RXUtils.request(RedPaperDetailsActivity.this, request, "openAdminRedPaper", new SupportSubscriber<Response<RedPaper>>() {

            @Override
            public void onNext(Response<RedPaper> redPaperResponse) {
                Log.d("雨落无痕丶", "onNext: ss");
                if (redPaperResponse.getStatus() == 1) {//正常获取到红包
                    thread.start();
                    red_status1.setVisibility(View.VISIBLE);
                    red_status2.setVisibility(View.GONE);
                    setData(redPaperResponse.getData());
                } else if (redPaperResponse.getStatus() == -3) {//红包金额被领完了
                    red_status1.setVisibility(View.GONE);
                    red_status2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onResponseError(Response response) {
                if (response.getStatus() == -3) {//红包金额被领完了
                    red_status1.setVisibility(View.GONE);
                    red_status2.setVisibility(View.VISIBLE);
                }
            }
        });*/
        if (status == 1) {
            red_status1.setVisibility(View.VISIBLE);
            red_status2.setVisibility(View.GONE);
            if (UnlockView.mRedPaper != null) {
                setData(UnlockView.mRedPaper);
            }
        } else if (status == -3) {
            red_status1.setVisibility(View.GONE);
            red_status2.setVisibility(View.VISIBLE);
        }
    }

    private void setData(RedPaper redPaper) {
        URL = redPaper.getUri();
        if (redPaper.getGoodsList() != null && redPaper.getGoodsList().size() > 0) {
            for (int i = 0; i < redPaper.getGoodsList().size(); i++) {
                Goods goods = new Goods();
                goods.setGoodsId(redPaper.getGoodsList().get(i).getGoodsId());
                goods.setCoverPic(redPaper.getGoodsList().get(i).getCoverPic());
                goods.setPrice(redPaper.getGoodsList().get(i).getPrice());
                goods.setScore(redPaper.getGoodsList().get(i).getScore());
                data.add(goods);
            }
            supergride.setAdapter(redperBaseAdapter);
        }
        if (StringUtils.isBlank(redPaper.getHeadImg())) {

            ivSellerImg.setImageResource(R.drawable.icon_defult);

        } else {

            Glide.with(RedPaperDetailsActivity.this).load(redPaper.getHeadImg()).asBitmap().placeholder(R.drawable.icon_defult).error(R.drawable.icon_defult).centerCrop().into(new BitmapImageViewTarget(ivSellerImg) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(RedPaperDetailsActivity.this.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivSellerImg.setImageDrawable(circularBitmapDrawable);

                }
            });
        }
        tvSellerMessage.setText(redPaper.getDescription());
        DecimalFormat df = new DecimalFormat("###.00");
        double d = Double.parseDouble(redPaper.getMoney());
        String results = df.format(d);
        Double ds = new Double(results);
        tvSellerMoney.setText(ds.toString());
        tvSellerName.setText(redPaper.getName());
        sellerId = redPaper.getSellerId();
        if (!StringUtils.isBlank("sellerId")) {
            if (sellerId.equals("0")) {
                if (StringUtils.isBlank(URL)) {
                    //进入首页
                    ivCome.setImageResource(R.drawable.btn_ljgw);
                } else {
                    //查看网页
                    ivCome.setImageResource(R.drawable.btn_ckxq);
                }
            } else {
                //进店逛逛
                ivCome.setImageResource(R.drawable.icon_redpaper_btn);
            }
        }
    }

    private BaseAdapter redperBaseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater()
                        .inflate(R.layout.layout_redpaper_goodlist, viewGroup, false);
            }
            ImageView imageview = (ImageView) view
                    .findViewById(R.id.iv_goods_icon);
            Glide.with(RedPaperDetailsActivity.this).load(data.get(i).getCoverPic()).into(imageview);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.toGoods(RedPaperDetailsActivity.this, data.get(i).getGoodsId());
                    RedPaperDetailsActivity.this.finish();
                }
            });
            TextView textView = (TextView) view
                    .findViewById(R.id.tv_money);
            textView.setText("¥" + data.get(i).getPrice());

            TextView textView1 = (TextView) view.findViewById(R.id.tv_score);
            if (data.get(i).getScore() != null) {
                textView1.setText("（或" + data.get(i).getScore() + "积分兑换）");
            }
            return view;
        }
    };
}
