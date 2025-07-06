package hello.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl();
    @Test
    void join() {
        // given
        Member member = new Member(1L, "mamberA", Grade.VIP);

        // then
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        // when
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
