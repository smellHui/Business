package com.bus.business.mvp.interactor.impl;

import com.bus.business.listener.RequestCallBack;
import com.bus.business.mvp.entity.response.RspTopicsBean;
import com.bus.business.mvp.interactor.NewsInteractor;
import com.bus.business.repository.network.RetrofitManager;
import com.bus.business.utils.MyUtils;
import com.bus.business.utils.TransformUtils;
import com.socks.library.KLog;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/22
 */
public class TopicsInteractorImpl implements NewsInteractor<RspTopicsBean> {

    @Inject
    public TopicsInteractorImpl() {

    }

    @Override
    public Subscription loadNews(final RequestCallBack<RspTopicsBean> listener, int pageNum, int numPerPage,String title) {

        return RetrofitManager.getInstance(1).getTopicListObservable(pageNum, numPerPage,title)
                .compose(TransformUtils.<RspTopicsBean>defaultSchedulers())
                .subscribe(new Subscriber<RspTopicsBean>() {
                    @Override
                    public void onCompleted() {
                        KLog.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        listener.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(RspTopicsBean newsBean) {
                        KLog.d(newsBean.toString());
                        listener.success(newsBean);
                    }
                });
    }
}
