package telran.x;

import telran.reflect.BeforeEach;
import telran.reflect.Test;

public class Xtest {
	
	@BeforeEach
	void beforeEach() {
		System.out.println("Before each");
	}
	
	@Test
	void f1() {
		System.out.println("test 1");
	}
	
	@BeforeEach
	void f3 (){
		System.out.println("Before each2");
	}
	
	int substract (int s1, int s2) {
		return s1 - s2;
	}
	
	@Test
	void f2() {
		System.out.println("test 2");
	}
	
}
