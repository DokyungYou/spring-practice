package com.example.springpractice.singleton;


public class SingletonService { // 

    // 1. static 영역에 객체를 생성해둔다.
    private static final SingletonService instance = new SingletonService(); // 미리 만들어둔 객체를 공유해서 사용

    // 2. 생성자를 private로 선언하여 외부에서 해당 클래스 인스턴스를 생성하지 못하도록 한다.
    private SingletonService(){
    }

    // 3. static final로 선언해둔 인스턴스는 해당 static 메서드를 통해서만 접근 가능
    public static SingletonService getInstance(){
        return instance;
    }

    public void logic(){
        System.out.println();
    }
}
