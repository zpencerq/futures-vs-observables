package com.example.test;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.concurrent.*;

public class ObservablesB {

    public static Observable<String> run() {
        return Observable.zip(
                Service.CallToRemoteServiceAObservable()
                        .flatMap(Service::CallToRemoteServiceCObservable),

                Service.CallToRemoteServiceBObservable()
                        .flatMap(result -> Observable.zip(
                                        Service.CallToRemoteServiceDObservable(result),
                                        Service.CallToRemoteServiceEObservable(result),
                                        (f4, f5) -> f4 * f5)
                        ),

                (start, result) -> "" + start + " => " + result
        );
    }

    public static Observable<Object> run4() {
        return Observable.merge(new Observable[] {
                Service.CallToRemoteServiceAObservable(),
                Service.CallToRemoteServiceBObservable(),
                Service.CallToRemoteServiceCObservable("A"),
                Service.CallToRemoteServiceCObservable("B"),
                Service.CallToRemoteServiceCObservable("C"),
                Service.CallToRemoteServiceDObservable(1),
                Service.CallToRemoteServiceEObservable(2),
                Service.CallToRemoteServiceEObservable(3),
                Service.CallToRemoteServiceEObservable(4),
                Service.CallToRemoteServiceEObservable(5)
        });
    }

    private static class DoMoreWork implements Action1<Object> {

        @Override
        public void call(Object s) {
            System.out.println("do more work => " + s);
        }
    }

    public static void main(String args[]) {
        Scheduler scheduler = Schedulers.from(Executors.newCachedThreadPool(r -> {
            Thread result = new Thread(r);
            //result.setDaemon(true);
            return result;
        })); // this is done so we don't get daemon threads so the program
            // will wait until all of them have completed


        long start = System.currentTimeMillis();
        run().subscribeOn(scheduler).subscribe(result -> {
            System.out.println(result);
            System.out.println("Finished in: " + (System.currentTimeMillis() - start) + "ms");

            run4().subscribeOn(scheduler).subscribe(
                    new DoMoreWork(),
                    Throwable::printStackTrace,
                    () -> System.out.println("DONE!"));
        }, Throwable::printStackTrace);
    }
}