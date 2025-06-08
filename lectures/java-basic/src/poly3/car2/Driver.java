package poly3.car2;

public class Driver {

    private Car car;

    public void setCar(Car car) {
        System.out.println("⚙️자동차 설정" + car);
        this.car = car;
    }

    public void driveCar() {
        System.out.println("🚕자동차 운전");
        car.startEngine();
        car.pressAcc();
        car.offEngine();
    }

}
