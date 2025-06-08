package poly2.ex5;

public class Dog implements InterfaceAnimal {
    @Override
    public void sound() {
        System.out.println("🐶멍");
    }

    @Override
    public void move() {
        System.out.println("🐶강아지가 이동합니다");

    }
}
