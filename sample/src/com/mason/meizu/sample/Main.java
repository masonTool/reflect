package com.mason.meizu.sample;

import java.lang.reflect.InvocationTargetException;

import com.mason.meizu.reflect.RClass;
import com.mason.meizu.reflect.RInstance;
import com.mason.meizu.reflect.RParam;

public class Main {

	public static void main(String[] args) {
		try {
			// 获取和设置 隐藏类ClassA的隐藏静态变量 staticString
			RClass clazzA = new RClass("com.mason.meizu.sample.prvclass.ClassA");
			clazzA.setValue("staticString", "static changed!!!!!");
			String staticString = (String) clazzA.getValue("staticString");

			// 获取和设置 隐藏类ClassA的实例，并获取实例内变量normalString的值
			Object instance = clazzA.newInstance(null);// 获得实例
			RInstance instanceA = new RInstance(instance);// 实例封装
			instanceA.setValue("normalString", "normal changed!!!!!");
			String normalString = (String) instanceA.getValue("normalString");

			// 执行隐藏类ClassA的静态方法plus
			RParam plusParam = RParam.create(5, 4);
			Integer plusResult = (Integer) clazzA.execute("plus", plusParam);

			// 执行隐藏类ClassA的实例方法minus
			RParam minusParam = new RParam.Builder().add(long.class, 5)// 默认是int，
																					// 此处需名确是long
					.add(long.class, 4)// 默认是int， 此处需名确是long
					.create();
			long minusResult = (long) instanceA.execute("minus", minusParam);// 实例去调用

			// 还支持更复杂的嵌套操作, 所有类都是隐藏的， 依然方便
			RClass clazzB = new RClass("com.mason.meizu.sample.prvclass.ClassB");
			RInstance instanceB = new RInstance(clazzB.newInstance(null));
			RClass clazzC = new RClass("com.mason.meizu.sample.prvclass.ClassC");
			RInstance instanceC = new RInstance(clazzC.newInstance(null));
			RParam complexParam = RParam.create(instanceB, instanceC);
			int complexResult = (int) clazzA.execute("plus", complexParam);
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
