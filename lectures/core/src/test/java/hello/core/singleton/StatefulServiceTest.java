package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    static class TestConfig{
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }

        @Bean
        public StatelessService statelessService() {
            return new StatelessService();
        }
    }

    @Test
    void statuefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // A 사용자가 10,000원을 주문
        statefulService1.order("userA", 10_000);
        // B 사용자가 20,000원을 주문
        statefulService2.order("userB", 20_000);

        //ThreadA : 사용자 A가 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price); // 20,000원이 나옴
        // 같은 인스턴스를 사용하므로 문제

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20_000);

        // 문제의 원인 : StatefulService의 price는 공유되는 필드인데, 특정 클라이언트가 값을 변경할 수 있음
    }


    @Test
    void statelessServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatelessService statelessService1 = ac.getBean(StatelessService.class);
        StatelessService statelessService2 = ac.getBean(StatelessService.class);

        // A 사용자가 10,000원을 주문
        int userAPrice = statelessService1.order("userA", 10_000);
        // B 사용자가 20,000원을 주문
        int userBPrice = statelessService2.order("userB", 20_000);

        //ThreadA : 사용자 A가 주문 금액 조회
        System.out.println("price = " + userAPrice); // 10,000원이 나옴(정상)

        Assertions.assertThat(userAPrice).isEqualTo(10_000);

    }

}