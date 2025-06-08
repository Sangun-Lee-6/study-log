package poly3.car2;

public class CarMain2 {
    public static void main(String[] args) {

        Driver driver = new Driver();

        K3Car k3Car = new K3Car();
        driver.setCar(k3Car);
        driver.driveCar();

        Model3Car model3Car = new Model3Car();
        driver.setCar(model3Car);
        driver.driveCar();
    }
}
