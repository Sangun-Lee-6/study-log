package access.b;

import access.a.AccessData;

public class AccessOuterMain {
    public static void main(String[] args) {
        AccessData data = new AccessData();

        // public 호출 가능
        data.publicField = 1;
        data.publicMethod();

        // default 호출 불가 : 다른 패키기니까
//        data.defaultField = 1;
//        data.defaultMethod();

        // private 호출 불가
//        data.privateField = 1;
//        data.privateMethod();

        // innerAccess는 public이므로 호출 가능
        // 그 이후로 innerAccess에서 private를 호출하는 것은 가능
        data.innerAccess();
    }
}
