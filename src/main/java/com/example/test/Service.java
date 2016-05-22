package com.example.test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Service {

    public static final class CallToRemoteServiceA implements Callable<String> {
        @Override
        public String call() throws Exception {
            // simulate fetching data from remote service
            Thread.sleep(100);
            return "responseA";
        }
    }

    public static Observable<String> CallToRemoteServiceAObservable() {
        return Observable.just("responseA").delay(100, TimeUnit.MILLISECONDS);
    }

    public static final class CallToRemoteServiceB implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            // simulate fetching data from remote service
            Thread.sleep(40);
            return 100;
        }
    }

    public static Observable<Integer> CallToRemoteServiceBObservable() {
        return Observable.just(100).delay(40, TimeUnit.MILLISECONDS);
    }

    public static final class CallToRemoteServiceC implements Callable<String> {

        private final String dependencyFromA;

        public CallToRemoteServiceC(String dependencyFromA) {
            this.dependencyFromA = dependencyFromA;
        }

        @Override
        public String call() throws Exception {
            // simulate fetching data from remote service
            Thread.sleep(60);
            return "responseB_" + dependencyFromA;
        }
    }

    public static Observable<String> CallToRemoteServiceCObservable(String dependencyFromA) {
        return Observable.just("responseB_"+ dependencyFromA).delay(60, TimeUnit.MILLISECONDS);
    }

    public static final class CallToRemoteServiceD implements Callable<Integer> {

        private final Integer dependencyFromB;

        public CallToRemoteServiceD(Integer dependencyFromB) {
            this.dependencyFromB = dependencyFromB;
        }

        @Override
        public Integer call() throws Exception {
            // simulate fetching data from remote service
            Thread.sleep(140);
            return 40 + dependencyFromB;
        }
    }

    public static Observable<Integer> CallToRemoteServiceDObservable(Integer dependencyFromB) {
        return Observable.just(40+dependencyFromB).delay(140, TimeUnit.MILLISECONDS);
    }

    public static final class CallToRemoteServiceE implements Callable<Integer> {

        private final Integer dependencyFromB;

        public CallToRemoteServiceE(Integer dependencyFromB) {
            this.dependencyFromB = dependencyFromB;
        }

        @Override
        public Integer call() throws Exception {
            // simulate fetching data from remote service
            Thread.sleep(55);
            return 5000 + dependencyFromB;
        }
    }

    public static Observable<Integer> CallToRemoteServiceEObservable(Integer dependencyFromB) {
        return Observable.just(5000+dependencyFromB).delay(55, TimeUnit.MILLISECONDS);
    }
}
