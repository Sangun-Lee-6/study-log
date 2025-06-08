package static1;

public class DataCountMain1 {
    public static void main(String[] args) {
        Data1 data1 = new Data1("A");
        System.out.println("A count: " + data1.count); // 1

        Data1 data2 = new Data1("A");
        System.out.println("A count: " + data2.count); // 1
    }
}
