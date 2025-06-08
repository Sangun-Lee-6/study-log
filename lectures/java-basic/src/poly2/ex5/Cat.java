package poly2.ex5;

public class Cat implements InterfaceAnimal {
    @Override
    public void sound() {
        System.out.println("🐱야용");
    }

    @Override
    public void move() {
        System.out.println("🐱고양이가 이동합니다.");

    }
}
