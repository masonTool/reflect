package com.mason.meizu.sample.prvclass;

/**
 * Created by mapeiyu on 16-9-27.
 */
class ClassA {
	protected static String staticString = "HELLO";
	private String normalString = "WORLD";
	
	protected static int staticInt = 0;

	// 有返回值有参数
	private static Integer plus(Integer a, Integer b) {
		return a + b;
	}

	// 无返回， 有参数
	private long minus(long a, long b) {
		return a - b;
	}

	// 嵌套的操作
	private static int plus(ClassB b, ClassC c) {
		return b.value + c.value;
	}
}
