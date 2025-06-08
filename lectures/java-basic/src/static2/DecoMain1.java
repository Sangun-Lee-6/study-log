package static2;

public class DecoMain1 {
    public static void main(String[] args) {
        String input = "hello java";
        DecoUtil1 utils = new DecoUtil1();
        String deco = utils.deco(input);

        System.out.println("before: " + input);
        System.out.println("after: " + deco);
    }
}
