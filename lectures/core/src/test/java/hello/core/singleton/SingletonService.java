package hello.core.singleton;

public class SingletonService {

    // 클래스 레벨에 올라가므로 1개만 존재함
    private static final SingletonService instance = new SingletonService();

    // JVM이 객체를 생성해서 static에 참조값을 넣어둠
    public static SingletonService getInstance() {
        return instance;
    }

    // private 생성자 -> 따라서 다른 곳에서 new로 이 인스턴스를 생성 못함
    private SingletonService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }



}
