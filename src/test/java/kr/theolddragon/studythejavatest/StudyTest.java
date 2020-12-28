package kr.theolddragon.studythejavatest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;


import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(Lifecycle.PER_CLASS)
class StudyTest {

  int value = 1;

  @FastTest
  @DisplayName("스터디 만들기 fast")
  void create_new_study_fast() {
    System.out.println(this);
    System.out.println("create fast " + value++);
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
    System.out.println(study);
  }

  @SlowTest
  @DisplayName("스터디 만들기 slow")
  void create_new_study_slow() {
    System.out.println(this);
    System.out.println("create slow " + value++);
    Study study = new Study(10);
    assertThat(study.getLimit()).isGreaterThan(0);
    System.out.println(study);
  }

  @DisplayName("스터디 만들기 반복")
  @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
  void repeat_new_study(RepetitionInfo repetitionInfo) {
    System.out.println("test " + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo
        .getTotalRepetitions());
  }

  @DisplayName("스터디 만들기 매개변수")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요"})
  @NullAndEmptySource
  void parameterizedTest(String message) {
    System.out.println(message);
  }

  @DisplayName("스터디 만들기 매개변수 By ints")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @ValueSource(ints = {10, 20, 40})
  void parameterizedTestByInts(@ConvertWith(StudyConverter.class) Study study) {
    System.out.println(study.getLimit());
  }

  static class StudyConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType)
        throws ArgumentConversionException {
      assertEquals(Study.class, targetType, "Can only convert to Study");
      return new Study(Integer.parseInt(source.toString()));
    }
  }

  @DisplayName("스터디 만들기 매개변수 By CSV")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource( {"10, '자바 스터디'", "20, '스프링'"})
  void parameterizedTestByCSV(ArgumentsAccessor argumentsAccessor) {
    final Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
    System.out.println(study);
  }

  @DisplayName("스터디 만들기 매개변수 By CSV And Aggregator")
  @ParameterizedTest(name = "{index} {displayName} message={0}")
  @CsvSource( {"10, '자바 스터디'", "20, '스프링'"})
  void parameterizedTestByCSVAndAggregator(@AggregateWith(StudyAggregator.class) Study study) {
    System.out.println(study);
  }

  static class StudyAggregator implements ArgumentsAggregator {

    @Override
    public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
        throws ArgumentsAggregationException {
      return new Study(accessor.getInteger(0), accessor.getString(1));
    }
  }

  @Test
  @DisplayName("스터디 만들기 🚗")
  void create_new_study() {
    System.out.println("create");
    Study study = new Study(10);
    assertAll(
        () -> assertNotNull(study),
        () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
            () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT + " 상태다."),
        () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
    );
  }

  @Test
  void create_new_study_assume() {
    final String test_env = System.getenv("TEST_ENV");
    System.out.println(test_env);
//    assumeTrue("LOCAL".equalsIgnoreCase(test_env));

    assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> System.out.println("local"));

    assumingThat("keesun".equalsIgnoreCase(test_env), () -> System.out.println("keesun"));
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
  void beforeAll() {
    System.out.println("before all");
  }

  @AfterAll
  void afterAll() {
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