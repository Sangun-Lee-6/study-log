package ref;

public class MethodChange2 {
    public static void main(String[] args) {

        Data dataA = new Data();
        dataA.value = 10;

        System.out.println("메서도 호출 전: dataA = " + dataA.value); // 10
        changReference(dataA);
        System.out.println("메서도 호출 후: dataA = " + dataA.value); // 20
    }

    static void changReference(Data dataX) {
        dataX.value = 20;
    }
}