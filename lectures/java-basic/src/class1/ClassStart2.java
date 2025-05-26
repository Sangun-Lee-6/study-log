package class1;

/**
 * 학생의 이름, 나이, 성적을 출력하는 프로그램
 */
public class ClassStart2 {
    public static void main(String[] args) {

        String[] studentNames = {"Tom", "Marry"};
        int[] studentAges = {15, 16};
        int[] studentGrade = {90, 80};

        for (int i = 0; i < studentNames.length; i++) {
            System.out.println("이름: " + studentNames[i] + " 나이: " + studentAges[i] + " 성적: " + studentGrade[i]);
        }
    }
}
