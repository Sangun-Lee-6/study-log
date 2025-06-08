package final1;

public class ConstructInit {

    /**
     * final 멤버변수 : 생성자를 통해 초기화되면 그 이후에 해당 값은 변경 불가
     */
    final int value;

    public ConstructInit(int value) {
        this.value = value;
    }
}
