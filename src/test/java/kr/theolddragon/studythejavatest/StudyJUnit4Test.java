package kr.theolddragon.studythejavatest;

import org.junit.Before;
import org.junit.Test;

public class StudyJUnit4Test {

  @Before
  public void before() {
    System.out.println("before");
  }

  @Test
  public void createTest() {
    System.out.println("test");
  }
}
