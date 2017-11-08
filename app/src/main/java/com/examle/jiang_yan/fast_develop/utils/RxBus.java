package com.examle.jiang_yan.fast_develop.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by jiang_yan on 2016/9/22.
 *
 * rxBus事件总线工具类
 */

public class RxBus {

    private RxBus() {
    }

    private static class SingleHolder {
        public static RxBus mRxBus = new RxBus();
    }

    public static RxBus getInstance() {
        return SingleHolder.mRxBus;
    }

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }
}
