package com.mason.meizu.sample;

import java.lang.reflect.InvocationTargetException;

import com.mason.meizu.reflect.RClass;
import com.mason.meizu.reflect.RInstance;

public class Main {

	public static void main(String[] args) {
		try {
			// 获取和设置 隐藏类ClassA的隐藏静态变量 staticString
			RClass clazzA = new RClass("com.mason.meizu.sample.prvclass.ClassA");
			clazzA.setValue("staticString", "static changed!!!!!");
			String staticString = clazzA.getValue("staticString");

			// 获取和设置 隐藏类ClassA的实例，并获取实例内变量normalString的值
			RInstance instanceA = clazzA.newWrappedInstance();// 获得实例
			instanceA.setValue("normalString", "normal changed!!!!!");
			String normalString = instanceA.getValue("normalString");

			// 执行隐藏类ClassA的静态方法plus
			Integer plusResult = clazzA.execute("plus", Integer.class, 5, Integer.class, 4);
			long minusResult = instanceA.execute("minus", long.class, 5, long.class, 4);

			// 还支持更复杂的嵌套操作, 所有类都是隐藏的， 依然方便
			RClass clazzB = new RClass("com.mason.meizu.sample.prvclass.ClassB");
			RClass clazzC = new RClass("com.mason.meizu.sample.prvclass.ClassC");
			int complexResult1 = clazzA.execute("plus", 
					clazzB, clazzB.newInstance(), 
					clazzC, clazzC.newInstance());
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

}
