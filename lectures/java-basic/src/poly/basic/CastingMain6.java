package poly.basic;

public class CastingMain6 {
    public static void main(String[] args) {

        Parent parent1 = new Parent();
        Parent parent2 = new Child();

        System.out.println(parent1 instanceof Parent); // true
        System.out.println(parent1 instanceof Child); // false

        System.out.println(parent2 instanceof Parent); // true
        System.out.println(parent2 instanceof Child); // true
    }
}
