package poly3.car2;

public class K3Car implements Car{
    @Override
    public void startEngine() {
        System.out.println("K3Car.startEngine");
    }

    @Override
    public void offEngine() {
        System.out.println("K3Car.offEngine");
    }

    @Override
    public void pressAcc() {
        System.out.println("K3Car.pressAcc");
    }
}
