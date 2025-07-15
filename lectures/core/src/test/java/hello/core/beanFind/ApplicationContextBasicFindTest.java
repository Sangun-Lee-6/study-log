package hello.core.beanFind;

import hello.core.AppConfig;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());
        // memberService = hello.core.member.MemberServiceImpl@32fe9d0a
        // memberService.getClass() = class hello.core.member.MemberServiceImpl

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }


    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }
    /**
     * AppConfig에서 memberService() 메서드의 반환 타입이 MemberService지만
     * 반환되는 구체 타입으로 조회 가능
     * 이건 구체에 의존하므로 좋은 코드는 아니지만 필요할 수 있음
     */

    @Test
    @DisplayName("빈 이름으로 조회할 수 없음")
    void canNotFindBeanByName() {
//      // MemberService memberService = ac.getBean("abcd", MemberService.class);
        // ⛔️ No bean named 'abcd' available

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("abcd", MemberService.class));
    }



}
