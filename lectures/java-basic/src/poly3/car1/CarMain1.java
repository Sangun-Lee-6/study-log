package poly3.car1;

    public class CarMain1 {
    public static void main(String[] args) {

        Driver driver = new Driver();
        K3Car k3Car = new K3Car();

        driver.setK3Car(k3Car);
        driver.drive();

        Model3Car model3Car = new Model3Car();
        driver.setK3Car(null);
        driver.setModel3Car(model3Car);
        driver.drive();

    }
}
