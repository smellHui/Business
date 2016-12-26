package com.bus.business.di.component;

import android.app.Activity;
import android.content.Context;

import com.bus.business.di.module.ActivityModule;
import com.bus.business.di.scope.ContextLife;
import com.bus.business.di.scope.PerActivity;
import com.bus.business.mvp.ui.activities.NewDetailActivity;

import dagger.Component;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/12/24
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewDetailActivity newsDetailActivity);

}
