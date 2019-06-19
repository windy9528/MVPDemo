package bwie.com.weidumall.model;

import bwie.com.weidumall.api.OrderApi;
import bwie.com.weidumall.constract.ImplView;
import bwie.com.weidumall.entity.Result;
import bwie.com.weidumall.util.RetrofitUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * date:2019/6/19
 * name:windy
 * function:
 */
public abstract class OrderModel {

    private ImplView implView;
    private Disposable disposable;

    public OrderModel(ImplView implView) {
        this.implView = implView;
    }

    /**
     * retrofit动态代理去访问网络请求数据
     * rxJava线程调度拿到数据
     *
     * @param args
     */
    public void requestData(final Object... args) {

        OrderApi orderApi = RetrofitUtil.getHttpUtil().create(OrderApi.class);

        disposable = getModel(orderApi, args)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, Result>() {
                    @Override
                    public Result apply(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        return new Result();
                    }
                })
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        if ("0000".equals(result.getStatus())) {
                            implView.success(result, args);
                        } else {
                            implView.fail(result, args);
                        }
                    }
                });
    }

    /**
     * p层继承该类 重写此方法
     *
     * @param orderApi
     * @param args
     * @return
     */
    protected abstract Observable getModel(OrderApi orderApi, Object... args);

    /**
     * 避免内存泄露
     */
    public void unBind() {
        if (disposable != null) {
            disposable = null;
        }
        if (implView != null){
            implView = null;
        }
    }
}
