package oop1;

public class ValueDataMain {
    public static void main(String[] args) {
        ValueData valueData = new ValueData();
        addValue(valueData);
        addValue(valueData);
        addValue(valueData);

        System.out.println("최종 숫자: " + valueData.value);
    }

    private static void addValue(ValueData valueData) {
        valueData.value++;
        System.out.println("숫자 증가, value: "+ valueData.value);
    }
}
