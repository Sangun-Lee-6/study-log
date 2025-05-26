package construct;

public class MemberConstruct3 {
    String name;
    int age;
    int grade;


    MemberConstruct3(String name, int age) {
        this(name, age, 50);
    }

    // 생성자
    MemberConstruct3(String name, int age, int grade) {
        System.out.println("생성자 호출 name: " + name + " age: " + age + " grade: " + grade);
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
}
