package com.example.springpractice.singleton;

public class StatefulService {

    private int price; // 상태유지 필드

    public void order(String name, int price){
        System.out.println("name: " + name +  "price: " + price);
        this.price = price; // 문제 발생
    }

    public int getPrice(){
        return price;
    }

    /*
    * 예제는 아주 단순해서 별거 아닌 것 같지만
    * 공유필드는 항상 주의해야한다. (복잡한 코드로 이루어지면 실수하기 쉽고, 파악하기도 어려움)
    *
    * 싱글톤 방식은 무상태로 설계해야함을 항상 잊지말기
    * */
}
