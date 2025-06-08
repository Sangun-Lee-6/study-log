package poly.overriding;

public class OverridingMain {
    public static void main(String[] args) {
        // 자식 변수가 자식 인스턴스 참조
        Child child = new Child();
        System.out.println("\nChild -> Child");
        System.out.println("value: " + child.value);
        child.method();

        // 부모 변수가 부모 인스턴스 참조
        Parent parent = new Parent();
        System.out.println("\nParent -> Parent");
        System.out.println("value: " + parent.value);
        parent.method();

        // 부모 변수가 자식 인스턴스 참조
        Parent poly = new Child();
        System.out.println("\nParent -> Child");
        System.out.println("value: " + poly.value); // parent
        poly.method(); // Child method()
    }
}
