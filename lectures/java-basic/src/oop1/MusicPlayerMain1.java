package oop1;

public class MusicPlayerMain1 {
    public static void main(String[] args) {
        int volume = 0;
        boolean isOn = false;

        // 음악 플레이어 ON
        isOn = true;
        System.out.println("--음악 플레이어 시작--");

        volume++;
        System.out.println("볼륨 1 증가");
        System.out.println("음악 플레이어 볼륨: " + volume);

        volume++;
        System.out.println("볼륨 1 증가");
        System.out.println("음악 플레이어 볼륨: " + volume);

        volume--;
        System.out.println("볼륨 1 감소");
        System.out.println("음악 플레이어 볼륨: " + volume);

        System.out.println("음악 플레이어 상태 확인");
        if (isOn) {
            System.out.println("음악 플레이어 ON, 볼륨: " + volume);
        } else {
            System.out.println("음악 플레이어 OFF");
        }

        isOn = false;
        System.out.println("--음악 플레이어 종료--");
    }
}
