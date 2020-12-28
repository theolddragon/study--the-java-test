package kr.theolddragon.studythejavatest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;


import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

  @Test
  @DisplayName("스터디 만들기 fast")
  @Tag("fast")
  void create_new_study_fast() {
    System.out.println("create fast");
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }

  @Test
  @DisplayName("스터디 만들기 slow")
  @Tag("slow")
  void create_new_study_slow() {
    System.out.println("create slow");
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
  }


  @Test
  @DisplayName("스터디 만들기 🚗")
  void create_new_study() {
    System.out.println("create");
    Study study = new Study(10);
    assertAll(
        () -> assertNotNull(study),
        () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT + " 상태다."),
        () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
    );
  }

  @Test
  void create_new_study_assume() {
    final String test_env = System.getenv("TEST_ENV");
    System.out.println(test_env);
//    assumeTrue("LOCAL".equalsIgnoreCase(test_env));

    assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
      System.out.println("local");
    });

    assumingThat("keesun".equalsIgnoreCase(test_env), () -> {
      System.out.println("keesun");
    });
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
  void create_new_study_environment_variable() {
    System.out.println("test on test_env is LOCAL");
  }

  @Test
  @EnabledOnJre(JRE.JAVA_11)
  void create_new_study_jre11() {
    System.out.println("test on jre11");
  }

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void create_new_study_window() {
    System.out.println("test on window");
  }

  @Test
  void create_new_study_exception() {
    System.out.println("create exception");
    final IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class, () -> new Study(-10));
    assertEquals("limit은 0보다 커야 한다.", exception.getMessage());
  }

  @Test
  void create_new_study_timeout() {
    System.out.println("create timeout");
    assertTimeout(Duration.ofMillis(100), () -> {
      new Study(10);
      Thread.sleep(50);
    });

    // TODO ThreadLocal
  }

  @BeforeAll
  static void beforeAll() {
    System.out.println("before all");
  }

  @AfterAll
  static void afterAll() {
    System.out.println("after all");
  }

  @BeforeEach
  void beforeEach() {
    System.out.println("before each");
  }

  @AfterEach
  void afterEach() {
    System.out.println("after each");
  }
}