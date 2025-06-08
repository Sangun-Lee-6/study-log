package static2;

public class DecoMain2 {
    public static void main(String[] args) {
        String input = "hello java";
        String deco = DecoUtil2.deco(input);

        System.out.println("before: " + input);
        System.out.println("after: " + deco);
    }
}
