package kr.theolddragon.studythejavatest;

import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

public class FindSlowTestExtension implements BeforeTestExecutionCallback,
    AfterTestExecutionCallback {

  private long THRESHOLD;

  public FindSlowTestExtension(long THRESHOLD) {
    this.THRESHOLD = THRESHOLD;
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    final Store store = getStore(context);
    store.put("START_TIME", System.currentTimeMillis());
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    final Method requiredTestMethod = context.getRequiredTestMethod();
    final SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);
    final String testMethodName = requiredTestMethod.getName();
    final Store store = getStore(context);
    final Long startTime = store.remove("START_TIME", long.class);
    final long duration = System.currentTimeMillis() - startTime;
    if (duration > THRESHOLD && annotation == null) {
      System.out.printf("Please consider mark method [%s] with @SlowTest.\n", testMethodName);
    }
  }

  private Store getStore(ExtensionContext context) {
    final String testClassName = context.getRequiredTestClass().getName();
    final String testMethodName = context.getRequiredTestMethod().getName();
    return context.getStore(Namespace.create(testClassName, testMethodName));
  }
}
