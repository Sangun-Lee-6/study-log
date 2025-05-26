package construct;

public class ConstructMain2 {

    public static void main(String[] args) {
        MemberConstruct2 member1 = new MemberConstruct2("Tom", 15, 90);
        MemberConstruct2 member2 = new MemberConstruct2("Marry", 16);

        MemberConstruct2[] members = {member1, member2};

        for (MemberConstruct2 m : members) {
            System.out.println("이름: " + m.name + " 나이: " + m.age + " 성적: " + m.grade);
        }
    }
}
