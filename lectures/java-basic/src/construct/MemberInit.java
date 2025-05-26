package construct;

public class MemberInit {
    String name;
    int age;
    int grade;

    // 추가
    void initMember(String name, int age, int grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
}

// this.name을 하면 해당 인스턴스의 name을 가리킴

