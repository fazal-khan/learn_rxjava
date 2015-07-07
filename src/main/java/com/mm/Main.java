package com.mm;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fazalkhan on 5/26/2015.
 */

public class Main {

    public static void main(String[] args) {
        test1();
    }

    public static void test5() {
        log("test zip");
        Observable.zip(Observable.from(Arrays.asList(1, 3, 5, 7, 9)),
                Observable.from(Arrays.asList(2,4,6,8,10)), (x,y)-> "(" + x + "," + y + ")").subscribe(Main::print);
        println("");

        log("test zipWith");
        Observable.from(Arrays.asList(1, 3, 5)).zipWith(Observable.from(Arrays.asList(2,4,6,7)), (x,y) -> "("+ x +"," + y +")").subscribe(Main::print);
        println("");


        log("retry stuff");
        AtomicInteger i = new AtomicInteger(0);
        Observable<String> testRetry = Observable.create(subs-> {
            if( i.get() < 3) {
                i.incrementAndGet();
                println("retyring for " + i + " time.");
                subs.onError(new RuntimeException("error 1"));
            }else {
                subs.onNext("hello");
                subs.onCompleted();
            }
        } );
        testRetry.retry().subscribe(Main::print, Main::println);
        println("");

    }

    private static void test4() {
        log("just subscribe to see output ");
        Observable<List<Integer>> listOfInts = Observable.just(Arrays.asList(1, 3, 5, 7), Arrays.asList(2, 4, 6, 8));
        listOfInts.subscribe(x -> System.out.print(x + " "));
        println("");

        log("concatMap");
        listOfInts.concatMap(x -> Observable.from(x)).subscribe(Main::print);
        println("");

        log("flapMap");
        listOfInts.flatMap(listOfIntegers -> {
            return Observable.from(listOfIntegers);
        }).subscribe(Main::print);
        println("");

        Map<String, List<Integer>> m1 = new HashMap<>();
        m1.put("key", Arrays.asList(2, 4, 6, 8, 10));

        Map<String, List<Integer>> m2 = new HashMap<>();
        m2.put("key", Arrays.asList(1, 3, 5, 7, 9));

        log("testing flatMap with 2 maps param");
        Observable.just(m1, m2).flatMap(map -> Observable.from(map.get("key")).map(x -> x)).subscribe(Main::print);
        println("");
    }

    private static void test3() {
        log("simple map");
        Observable.just("Test", "Me", "No").map(x -> x.length()).subscribe(Main::print);
        println("");

        log("map with count");
        Observable.just("Test", "Me", "No").count().subscribe(Main::print);
        println("");

        Observable<Integer> distinctObsv = Observable.just(2, 2, 3, 3, 4, 5, 6, 7, 8, 9).distinct();
        log("distinct & filter ");
        distinctObsv.filter(x -> (x % 2 == 0)).subscribe(Main::print);
        println("");

        log("distinct & filter & skip &  first");
        distinctObsv.filter(x -> (x % 2 == 0)).skip(2).first().subscribe(Main::print);
        println("");

        log("distinct & filter & skip & last");
        distinctObsv.filter(x -> (x % 2 == 0)).skip(2).last().subscribe(Main::print);
        println("");

        log("distinct & filter & take & last");
        distinctObsv.filter(x -> (x % 2 == 0)).take(2).last().subscribe(Main::print);
        println("");

        log("map with reduce to get total length of strings \"Test\", \"Me\", \"No\"");
        Observable.just("Test", "Me", "No").map(x -> x.length()).reduce((x, y) -> x + y).subscribe(Main::print);
        println("");

        log("sum of 1,2,3,4,5 using reduce");
        Observable.just(1, 2, 3, 4, 5).reduce((x, y) -> x + y).subscribe(Main::print);
        println("");

        log("get larget of seq 9,1,8,3,4,5 using reduce");
        Observable.just(9, 1, 8, 3, 4, 5).reduce((x, y) -> x > y ? x : y).subscribe(Main::print);
        println("");
    }

    private static void test2() {
        Observable<String> stringObservable = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onStart();
                subscriber.onNext("Hello World");
                subscriber.onCompleted();
            }
        });
        stringObservable.subscribe(Main::println);
        stringObservable.subscribe(new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                System.out.println("On start.........");
            }

            @Override
            public void onCompleted() {
                System.out.println("completed....");
            }

            @Override
            public void onError(Throwable e) {
                println(e);
            }

            @Override
            public void onNext(String s) {
                println(s);
            }
        });
    }

    private static void test1() {
        rx.Observable<String> hello = rx.Observable.just("Hello");
        hello.subscribe(x -> System.out.println(x));
        hello.subscribe(System.out::println);
    }


    public static <T> void print(T t) {
        System.out.print(t + " ");
    }

    public static <T> void println(T t) {
        System.out.println(t);
    }

    static void log(Object msg) {
        System.out.println("-----------------------> " + msg + " -->");
    }

}
