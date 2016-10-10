package com.mason.meizu.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * 用于处理反射Class类
 */
public class RClass extends RExecutor {

	private static final HashMap<String, Class<?>> sClassMap = new HashMap<String, Class<?>>();
	private static final HashMap<String, Constructor<?>> sConstructorMap = new HashMap<String, Constructor<?>>();

	private String className;
	private Class<?> classObj;

	/**
	 * 构造函数, 构造反射类
	 * 
	 * @param className
	 * @param loader
	 * @throws ClassNotFoundException
	 */
	public RClass(String className, ClassLoader loader) throws ClassNotFoundException {
		if (className == null) {
			throw new IllegalArgumentException("className can't be empty");
		}
		Class<?> clazz = sClassMap.get(className);
		if (clazz == null) {
			if (sClassMap.containsKey(className)) {
				throw new ClassNotFoundException(className);
			} else {
				try {
					if (loader == null) {
						clazz = Class.forName(className);
					} else {
						clazz = Class.forName(className, true, loader);
					}
				} finally {
					sClassMap.put(className, clazz);
				}
			}
		}
		this.className = className;
		this.classObj = clazz;
	}

	/**
	 * 构造函数, 构造反射类
	 * 
	 * @param className
	 * @throws ClassNotFoundException
	 */
	public RClass(String className) throws ClassNotFoundException {
		this(className, null);
	}

	/**
	 * 构造函数, 构造反射类
	 * 
	 * @param classObj
	 */
	public RClass(Class<?> classObj) {
		if (classObj == null) {
			throw new IllegalArgumentException("classObj can't be null");
		}
		this.classObj = classObj;
		this.className = classObj.getName();
		sClassMap.put(className, classObj);
	}
	
	/**
	 * Create instance object of this RClass
	 * @param params The format like  (Class1, value1, Class2, value2, Class3, value3...)
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public Object newInstance(Object... params)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		RParam rParam = params == null ? null : RParam.create(params);
		Class<?>[] paramsTypes = rParam == null ? null : rParam.getTypes();
		String paramString = rParam == null ? "" : rParam.getString();
		Object[] paramValus = rParam == null ? null : rParam.getValus();

		String key = className + "(" + paramString + ")";
		Constructor<?> constructor = sConstructorMap.get(key);
		if (constructor == null) {
			if (sConstructorMap.containsKey(key)) {
				throw new NoSuchMethodException(key);
			} else {
				try {
					constructor = classObj.getDeclaredConstructor(paramsTypes);
					constructor.setAccessible(true);
				} finally {
					sConstructorMap.put(key, constructor);
				}
			}
		}
		return constructor.newInstance(paramValus);
	}
	
	
	/**
	 * Create instance object of this RClass.   Wrapped with RInstance.
	 * @param params
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public RInstance newWrappedInstance(Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return new RInstance(newInstance(params));
	}

	@Override
	public RClass getReflectClass() {
		return this;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	public String getClassName() {
		return className;
	}

	public Class<?> getClassObj() {
		return classObj;
	}
}
