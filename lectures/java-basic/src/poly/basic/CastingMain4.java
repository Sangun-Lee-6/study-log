package poly.basic;

public class CastingMain4 {
    public static void main(String[] args) {

        /**
         * 다운캐스팅이 가능한 경우
         */
        Parent parent1 = new Child();
        Child child1 = (Child) parent1; // 문제 없음
        child1.childMethod();


        Parent parent2 = new Parent();
        Child child2 = (Child) parent2;
        child2.childMethod();

    }
}
