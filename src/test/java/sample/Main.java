package sample;

import com.mason.meizu.reflect.RClass;
import com.mason.meizu.reflect.RInstance;
import com.mason.meizu.reflect.RInterface;

import java.lang.reflect.InvocationTargetException;

public class Main {
	
	public static class Demo {
		
//		If the parameter is a hide type. you can use this method. Be careful for the type handler.
//		public int onChange(Object[] i) {
//			return (int)i[0];
//		}
		
		//The common define
		public int onChange(int i) {
			return i;
		}
	}
	
	public static void main(String[] args) {
		
		try {
			
			// 获取和设置 隐藏类ClassA的隐藏静态变量 staticString
			RClass clazzA = new RClass("com.mason.meizu.sample.prvclass.ClassA");
			clazzA.setValue("staticString", "static changed!!!!!");
			String staticString = clazzA.getValue("staticString");
			
			clazzA.setValue("staticInt", 100);
			int value = clazzA.getValue("staticInt");

			// 获取和设置 隐藏类ClassA的实例，并获取实例内变量normalString的值
			RInstance instanceA = clazzA.newWrappedInstance();// 获得实例
			instanceA.setValue("normalString", "normal changed!!!!!");
			String normalString = instanceA.getValue("normalString");
			int vvv = instanceA.getValue("staticInt");

			// 执行隐藏类ClassA的静态方法plus
			Integer plusResult = clazzA.execute("plus", Integer.class, 5, Integer.class, 4);
			long minusResult = instanceA.execute("minus", long.class, 5, long.class, 4);

			// 还支持更复杂的嵌套操作, 所有类都是隐藏的， 依然方便
			RClass clazzB = new RClass("com.mason.meizu.sample.prvclass.ClassB");
			RClass clazzC = new RClass("com.mason.meizu.sample.prvclass.ClassC");
			int complexResult1 = clazzA.execute("plus", 
					clazzB, clazzB.newInstance(), 
					clazzC, clazzC.newInstance());
			
			//interface 
			RInterface in = new RInterface("com.mason.meizu.sample.prvclass.ClassD4Listener$Listener");
			RClass lis = new RClass("com.mason.meizu.sample.prvclass.ClassD4Listener");
			Object interface1 = in.newInstance(new Demo());
			int testInterfaceResult = lis.newWrappedInstance().execute("testInterface", in, interface1, int.class, 8);
			
			System.out.println(testInterfaceResult);
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
