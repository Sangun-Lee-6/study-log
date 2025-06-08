package static2;

public class DecoData {
    private int instanceValue;
    private static int staticValue;

    public static void staticCall() {
        staticValue++; // 정적 변수 접근 가능
        staticMethod();
        // 인스턴스 변수,메서드에는 접근 불가
//        instanceValue++;
//        instanceCall();
    }

    public void instanceCall() {
        System.out.println("instanceValue: " + instanceValue);
        instanceValue++;

        // 정적 변수,메서드 접근 가능
        staticValue++;
        staticMethod();
    }

    private static void staticMethod() {
        System.out.println("staticValue: " + staticValue);
    }

    public static void staticCall(DecoData data) {
        data.instanceValue++;
        data.instanceCall();
    }
}
