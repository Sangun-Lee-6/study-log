package poly.basic;

public class CastingMain5 {
    public static void main(String[] args) {

        Parent parent1 = new Parent();
        System.out.println("parent1 호출");
        call(parent1);
        Parent parent2 = new Child();
        System.out.println("parent2 호출");
        call(parent2);

        // parent1, parent2가 참조하는 인스턴스는 어떤 타입인가?


    }

    private static void call(Parent parent) {
        parent.parentMethod();
        if (parent instanceof Child) {
            System.out.println("parent_ 변수가 가리키는 것은 Child 인스턴스");
            ((Child) parent).childMethod();
        }
    }
}
