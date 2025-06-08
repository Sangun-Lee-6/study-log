package final1;

public class FinalLocalMain {
    public static void main(String[] args) {

        /**
         * 예제 : final 지역변수
         */
        final int data1 = 10; // final은 1번만 할당 가능
//        data1 = 20; // 컴파일 에러

        /**
         * 예제 : final 매개변수
         */
        sampleMethod(10); // 매개변수에 인자를 넣는 것은 값을 할당한 것 -> 따라서 메서드 내부에서 재할당 불가
    }



    static void sampleMethod(final int param){
//        param = 20; // 컴파일 에러
        System.out.println("param: " + param);
    }




}
