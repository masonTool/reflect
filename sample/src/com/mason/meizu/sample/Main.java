package com.mason.meizu.sample;

import java.lang.reflect.InvocationTargetException;

import com.mason.meizu.reflect.ReflectClass;
import com.mason.meizu.reflect.ReflectInstance;
import com.mason.meizu.reflect.ReflectParam;

public class Main {

	public static void main(String[] args) {
		try {
			// 获取和设置 隐藏类ClassA的隐藏静态变量 staticString
			ReflectClass clazzA = new ReflectClass("com.mason.meizu.sample.prvclass.ClassA");
			clazzA.setValue("staticString", "static changed!!!!!");
			String staticString = (String) clazzA.getValue("staticString");

			// 获取和设置 隐藏类ClassA的实例，并获取实例内变量normalString的值
			Object instance = clazzA.newInstance(null);// 获得实例
			ReflectInstance instanceA = new ReflectInstance(instance);// 实例封装
			instanceA.setValue("normalString", "normal changed!!!!!");
			String normalString = (String) instanceA.getValue("normalString");

			// 执行隐藏类ClassA的静态方法plus
			ReflectParam plusParam = new ReflectParam.Builder().add(5).add(4).create();
			Integer plusResult = (Integer) clazzA.execute("plus", plusParam);

			// 执行隐藏类ClassA的实例方法minus
			ReflectParam minusParam = new ReflectParam.Builder().add(long.class, 5)// 默认是int，
																					// 此处需名确是long
					.add(long.class, 4)// 默认是int， 此处需名确是long
					.create();
			long minusResult = (long) instanceA.execute("minus", minusParam);// 实例去调用

			// 还支持更复杂的嵌套操作, 所有类都是隐藏的， 依然方便
			ReflectClass clazzB = new ReflectClass("com.mason.meizu.sample.prvclass.ClassB");
			ReflectInstance instanceB = new ReflectInstance(clazzB.newInstance(null));
			ReflectClass clazzC = new ReflectClass("com.mason.meizu.sample.prvclass.ClassC");
			ReflectInstance instanceC = new ReflectInstance(clazzC.newInstance(null));
			ReflectParam complexParam = new ReflectParam.Builder().add(instanceB).add(instanceC).create();
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
