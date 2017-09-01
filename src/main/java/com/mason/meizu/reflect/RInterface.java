package com.mason.meizu.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.mason.meizu.reflect.RClass;
import com.mason.meizu.reflect.RInstance;

/**
 * Used for handling interface reflect. 
 * <p> The method {@link #newInstance(Object...)} can get a class instance which implements this interface.
 */
public class RInterface extends RClass {

	public RInterface(String className, ClassLoader loader) throws ClassNotFoundException {
		super(className, loader);
	}
	
	public RInterface(String className) throws ClassNotFoundException {
		super(className);
	}

	public RInterface(Class<?> classObj) {
		super(classObj);
	}
	
	/**
	 * get a class instance which implements the interface.
	 * 
	 * @param params A object which implements the interface. Though don't use @Override annotation(You can't though you want to). 
	 * <p>You can define a class with same methods with the interface. If the methods parameter has hide type, use Object[] to instead.
	 * <p>eg:  public void onChange(Object[] objects). 
	 * <p>Attention that there should be only one parameter. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T newInstance(Object... params) {
		if (params == null || params.length != 1) {
			throw new IllegalArgumentException("This parameter accept only one");
		}
		return (T)Proxy.newProxyInstance(classObj.getClassLoader(),
                new Class[]{classObj}, new ProxyHandler(params[0]));
	}

	/**
	 * Perhaps no need. All the operation with the interface instance, can be instead by the parameter object.
	 */
	@Override
	public RInstance newWrappedInstance(Object... params) {
		return new RInstance(this, newInstance(params));
	}

	private static class ProxyHandler implements InvocationHandler {
		
		private Object interfaceObj; 
		
		ProxyHandler(Object interfaceObj) {
			this.interfaceObj = interfaceObj;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			RInstance instance = new RInstance(interfaceObj);
			Method newMethod;
			try {
				newMethod = instance.getMethod(method.getName(), method.getParameterTypes());
				return newMethod.invoke(interfaceObj, args);
			} catch (NoSuchMethodException exception) {
				//Your interfaceObj class can't implements some interface method, cause the parameter has hide type. 
				//Then your method should use a parameter with type Object[].
				newMethod = instance.getMethod(method.getName(), Object[].class);
				return newMethod.invoke(interfaceObj, new Object[]{args});
			}
		}
		
	}
}
