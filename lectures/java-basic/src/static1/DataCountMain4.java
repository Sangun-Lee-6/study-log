package static1;


public class DataCountMain4 {
    public static void main(String[] args) {
        // 클래스를 통한 접근
        Data3 data1 = new Data3("A");
        System.out.println("A count: " + Data3.count);

        // 인스턴스를 통한 접근
        Data3 data2 = new Data3("B");
        System.out.println("B count: " + data2.count);
    }
}
