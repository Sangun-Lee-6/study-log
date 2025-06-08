package extends1.overriding;

public class ElectricCar extends Car {
    public void charge() {
        System.out.println("--자동차 충전");
    }

    @Override
    public void move() {
        System.out.println("---⚡️전기자동차 이동");
    }
}
