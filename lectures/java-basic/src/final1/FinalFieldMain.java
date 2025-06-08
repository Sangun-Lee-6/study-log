package final1;

public class FinalFieldMain {
    public static void main(String[] args) {
        System.out.println("---final 필드를 생성자로 초기화");
        ConstructInit constructInit1 = new ConstructInit(10);
        ConstructInit constructInit2 = new ConstructInit(20);
        System.out.println(constructInit1.value);
        System.out.println(constructInit2.value);

        System.out.println("---final 필드를 클래스에서 선언하면서 초기화 : 항상 초기화된 값");
        FieldInit fieldInit1 = new FieldInit();
        FieldInit fieldInit2 = new FieldInit();
        System.out.println(fieldInit1.value);
        System.out.println(fieldInit2.value);

        System.out.println("---상수");
        System.out.println(FieldInit.CONST_VALUE);
    }
}
