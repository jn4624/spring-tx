package hello.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InitTxTest {
    @Autowired
    Hello hello;

    @Test
    void go() {
        /**
         * @PostConstruct는
         * 초기화 시점에 호출되기 때문에 트랜잭션이 적용되지 않는다.
         *
         * @EventListener(ApplicationReadyEvent.class)는
         * 스프링 컨테이너가 완전히 생성된 후 호출되기 때문에 트랜잭션이 적용된다.
         */
    }

    @TestConfiguration
    static class InitTxConfig {
        @Bean
        Hello hello() {
            return new Hello();
        }
    }

    static class Hello {
        @PostConstruct
        @Transactional
        public void initV1() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx active = {}", isActive);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init ApplicationReadyEvent tx active = {}", isActive);
        }
    }
}
