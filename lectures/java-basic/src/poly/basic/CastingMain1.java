package poly.basic;

public class CastingMain1 {
    public static void main(String[] args) {
        Parent polyParent = new Child();
//        poly.childMethod();

        /**
         * 다운캐스팅
         */
        Child polyChild = (Child) polyParent;
        polyChild.childMethod();

    }
}
