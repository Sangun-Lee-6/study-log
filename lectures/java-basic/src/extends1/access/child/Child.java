package extends1.access.child;

import extends1.access.parent.Parent;

public class Child extends Parent {
    public void call() {
        publicValue = 1;
        protectedValue = 1; // 다른 패키지여도 상속관계라 가능
//        defaultValue = 1; // 다른 패키지라서 호출 불가
//        privateValue = 1; // 다른 클래스에서 접근 불가

        publicMethod();
        protectedMethod();
//        defaultMethod();
//        privateMethod();

        /**
         * printParent는 public이므로 가능
         * 그 안에서 default, private 호출 가능
         */
        printParent();
    }
}
