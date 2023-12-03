package telran.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import telran.reflect.BeforeEach;
import telran.reflect.Test;

public class TestLibrary {
	public static void launchTest(Object testObj) throws Exception {
		Method [] methods = testObj.getClass().getDeclaredMethods();
		ArrayList<Method> beforeEachMethods = new ArrayList<Method>();
		for(Method m: methods) {
			if (m.isAnnotationPresent(BeforeEach.class)) {
				m.setAccessible(true);
				beforeEachMethods.add(m);
			}
		}
		for(Method m: methods) {
			if (m.isAnnotationPresent(Test.class)) {
				invokeBeforeEach(testObj, beforeEachMethods);
				m.setAccessible(true);
				m.invoke(testObj);
			}
		}
	}

	private static void invokeBeforeEach(Object testObj, ArrayList<Method> beforeEachMethods)
			throws IllegalAccessException, InvocationTargetException {
			for(Method b: beforeEachMethods) {
					b.invoke(testObj);
		}
	}
}
