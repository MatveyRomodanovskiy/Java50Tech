package telran.test;

import java.lang.reflect.Method;

import telran.reflect.BeforeEach;
import telran.reflect.Test;

public class TestLibrary {
	public static void launchTest(Object testObj) throws Exception {
		Method [] methods = testObj.getClass().getDeclaredMethods();
		Method beforeEachMethod = null;
		for(Method m: methods) {
			if (m.isAnnotationPresent(BeforeEach.class)) {
				beforeEachMethod = m;
				beforeEachMethod.setAccessible(true);
			}
		}
		for(Method m: methods) {
			if (m.isAnnotationPresent(Test.class)) {
				if (beforeEachMethod != null) {
					beforeEachMethod.invoke(testObj);
				}
				m.setAccessible(true);
				m.invoke(testObj);
			}
		}
	}
}
