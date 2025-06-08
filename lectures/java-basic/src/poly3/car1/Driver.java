package poly3.car1;

public class Driver {

    private K3Car k3Car;
    private Model3Car model3Car;

    /**
     * [K3] 자동차 탑승
     */
    public void setK3Car(K3Car k3Car) {
        this.k3Car = k3Car;
    }

    public void setModel3Car(Model3Car model3Car) {
        this.model3Car = model3Car;
    }

    /**
     * 자동차 운전
     */
    public void drive() {
        System.out.println("자동차 운전");
        if(k3Car != null){
            k3Car.startEngine();
            k3Car.pressAcc();
            k3Car.offEngine();
        } else if (model3Car != null) {
            model3Car.startEngine();
            model3Car.pressAcc();
            model3Car.offEngine();
        }

    }


}
