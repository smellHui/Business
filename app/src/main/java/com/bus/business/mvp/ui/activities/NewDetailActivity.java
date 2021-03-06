package com.bus.business.mvp.ui.activities;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bus.business.R;
import com.bus.business.common.Constants;
import com.bus.business.mvp.entity.BusDetailBean;
import com.bus.business.mvp.entity.response.RspBusDetailBean;
import com.bus.business.mvp.entity.response.RspNewDetailBean;
import com.bus.business.mvp.ui.activities.base.BaseActivity;
import com.bus.business.repository.network.RetrofitManager;
import com.bus.business.utils.DateUtil;
import com.bus.business.utils.TransformUtils;
import com.bus.business.widget.URLImageGetter;
import com.socks.library.KLog;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscriber;

public class NewDetailActivity extends BaseActivity {

    private String newsId;
    private int newsType;

    @Inject
    Activity mActivity;

    @BindView(R.id.news_detail_title_tv)
    TextView mTitle;
    @BindView(R.id.news_detail_from_tv)
    TextView mFrom;
    @BindView(R.id.news_detail_body_tv)
    TextView mNewsDetailBodyTv;
    @BindView(R.id.news_detail_fund_tv)
    TextView mFundTv;
    @BindView(R.id.news_detail_date_tv)
    TextView mDateTv;
    @BindView(R.id.tv_phone)
    TextView mPhone;
    @BindView(R.id.tv_zuo_phone)
    TextView mZPhone;
    @BindView(R.id.tv_tag)
    TextView mTag;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private URLImageGetter mUrlImageGetter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_new_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        newsId = getIntent().getStringExtra(Constants.NEWS_POST_ID);
        newsType = getIntent().getIntExtra(Constants.NEWS_TYPE,0);

        setCustomTitle(setTitle());
        showOrGoneSearchRl(View.GONE);
        mFundTv.setVisibility(setContentTitleVisible() ? View.GONE : View.VISIBLE);
        mPhone.setVisibility(setContentTitleVisible() ? View.GONE : View.VISIBLE);
        mTag.setVisibility(setContentTitleVisible() ? View.GONE : View.VISIBLE);
        mZPhone.setVisibility(setContentTitleVisible() ? View.GONE : View.VISIBLE);
        loadNewDetail();
    }

    private String setTitle(){
        switch (newsType){
            case Constants.DETAIL_XUN_TYPE:
                return "新闻详情";
            case Constants.DETAIL_XIE_TYPE:
                return "商讯详情";
            case Constants.DETAIL_TOP_TYPE:
                return "专题详情";

        }
        return "";
    }

    private boolean setContentTitleVisible(){
        return newsType == Constants.DETAIL_XUN_TYPE||
                newsType ==Constants.DETAIL_TOP_TYPE;
    }

    private void loadNewDetail() {
        switch (newsType){
            case Constants.DETAIL_XUN_TYPE:
                RetrofitManager.getInstance(1).getNewDetailObservable(newsId)
                        .compose(TransformUtils.<RspNewDetailBean>defaultSchedulers())
                        .subscribe(new Subscriber<RspNewDetailBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e.toString());
                            }

                            @Override
                            public void onNext(RspNewDetailBean rspNewDetailBean) {
                                mProgressBar.setVisibility(View.GONE);
                                KLog.d(rspNewDetailBean.toString());
                                fillData(rspNewDetailBean.getBody().getNews());
                            }
                        });
                break;
            case Constants.DETAIL_XIE_TYPE:
                RetrofitManager.getInstance(1).getBusDetailObservable(newsId)
                        .compose(TransformUtils.<RspBusDetailBean>defaultSchedulers())
                        .subscribe(new Subscriber<RspBusDetailBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e.toString());
                            }

                            @Override
                            public void onNext(RspBusDetailBean rspNewDetailBean) {
                                mProgressBar.setVisibility(View.GONE);
                                KLog.d(rspNewDetailBean.toString());
                                fillData(rspNewDetailBean.getBody().getBusiness());
                            }
                        });
                break;
            case Constants.DETAIL_TOP_TYPE:
                RetrofitManager.getInstance(1).getTopicDetailObservable(newsId)
                        .compose(TransformUtils.<RspNewDetailBean>defaultSchedulers())
                        .subscribe(new Subscriber<RspNewDetailBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                KLog.d(e.toString());
                            }

                            @Override
                            public void onNext(RspNewDetailBean rspNewDetailBean) {
                                mProgressBar.setVisibility(View.GONE);
                                KLog.d(rspNewDetailBean.toString());
                                fillData(rspNewDetailBean.getBody().getNews());
                            }
                        });

                break;

        }
    }

    private void fillData(BusDetailBean bean) {
        mTitle.setText(bean.getTitle());
        mDateTv.setText("发表时间 : " + DateUtil.getCurGroupDay(bean.getCtime()));
        mFrom.setText("北京工商联");
        mFundTv.setText("项目总投资" + formAmount(bean.getInAmount()) + "元");
        mPhone.setText("联系电话 : " + bean.getPhoneNo());
        mZPhone.setText("座机电话 : "+bean.getPlane());
        mUrlImageGetter = new URLImageGetter(mNewsDetailBodyTv, bean.getContentS(), 2);
        mNewsDetailBodyTv.setText(Html.fromHtml(bean.getContentS(), mUrlImageGetter, null));
    }

    private String formAmount(double num) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,###");
        return myformat.format(num);
    }
}
