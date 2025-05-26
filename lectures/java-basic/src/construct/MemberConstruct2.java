package construct;

public class MemberConstruct2 {
    String name;
    int age;
    int grade;


    MemberConstruct2(String name, int age) {
        this.name = name;
        this.age = age;
        this.grade = 50;
    }

    // 생성자
    MemberConstruct2(String name, int age, int grade) {
        System.out.println("생성자 호출 name: " + name + " age: " + age + " grade: " + grade);
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
}
