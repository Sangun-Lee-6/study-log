package poly2.ex1;

public class AnimalSoundMain {
    public static void main(String[] args) {
        Dog dog = new Dog();
        Cat cat = new Cat();
        Cow cow = new Cow();

        System.out.println("\n---동물 소리 테스트 start");
        dog.sound();
        System.out.println("---동물 소리 테스트 end");

        System.out.println("\n---동물 소리 테스트 start");
        cat.sound();
        System.out.println("---동물 소리 테스트 end");

        System.out.println("\n---동물 소리 테스트 start");
        cow.sound();
        System.out.println("---동물 소리 테스트 end");

    }

}
