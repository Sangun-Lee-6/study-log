package class1;

public class ClassStart3 {
    public static void main(String[] args) {
        Student student1 = new Student();
        student1.name = "Tom";
        student1.age = 15;
        student1.grade = 90;

        Student student2 = new Student();
        student2.name = "Marry";
        student2.age = 16;
        student2.grade = 80;

        // 참조값 확인
        System.out.println(student1);
        
        System.out.println("이름: " + student1.name + " 나이: " + student1.age + " 성적: " + student1.grade);
        System.out.println("이름: " + student2.name + " 나이: " + student2.age + " 성적: " + student2.grade);


    }
}
