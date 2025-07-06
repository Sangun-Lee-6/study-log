package hello.core.member;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}

/**
 * 회원 도메인 설계의 문제점
 * 이 코드의 설계상 문제점은 무엇일까요?
 * 다른 저장소로 변경할 때 OCP 원칙을 잘 준수할까요?
 * DIP를 잘 지키고 있을까요?
 * **의존관계가 인터페이스 뿐만 아니라 구현까지 모두 의존하는 문제점이 있음
 */