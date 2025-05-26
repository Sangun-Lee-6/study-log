package oop1;

public class MusicPlayerMain3 {
    public static void main(String[] args) {

        MusicPlayerData data = new MusicPlayerData();

        // 음악 플레이어 ON
        on(data);

        volumeUp(data);
        volumeUp(data);

        volumeDown(data);

        showStatus(data);

        off(data);
    }

    private static void showStatus(MusicPlayerData data) {
        System.out.println("음악 플레이어 상태 확인");
        if (data.isOn) {
            System.out.println("음악 플레이어 ON, 볼륨: " + data.volume);
        } else {
            System.out.println("음악 플레이어 OFF");
        }
    }

    private static void volumeDown(MusicPlayerData data) {
        data.volume--;
        System.out.println("볼륨 1 감소");
        System.out.println("음악 플레이어 볼륨: " + data.volume);
    }

    private static void volumeUp(MusicPlayerData data) {
        data.volume++;
        System.out.println("볼륨 1 증가");
        System.out.println("음악 플레이어 볼륨: " + data.volume);
    }

    private static void off(MusicPlayerData data) {
        data.isOn = false;
        System.out.println("--음악 플레이어 종료--");
    }

    private static void on(MusicPlayerData data) {
        data.isOn = true;
        System.out.println("--음악 플레이어 시작--");
    }
}
