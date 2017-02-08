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

	protected String className;
	protected Class<?> classObj;

	/**
	 * 构造函数, 构造反射类
	 * 
	 * @param className className
	 * @param loader loader
	 * @throws ClassNotFoundException ClassNotFoundException
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
	 * @param className className
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public RClass(String className) throws ClassNotFoundException {
		this(className, null);
	}

	/**
	 * 构造函数, 构造反射类
	 * 
	 * @param classObj classObj
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
	 * @return return
	 * @throws NoSuchMethodException NoSuchMethodException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws InvocationTargetException InvocationTargetException
	 * @throws InstantiationException InstantiationException
	 */
	@SuppressWarnings("unchecked")
	public <T> T newInstance(Object... params)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		RParam rParam = params == null ? null : RParam.create(params);
		Class<?>[] paramsTypes = rParam == null ? null : rParam.getTypes();
		Object[] paramValus = rParam == null ? null : rParam.getValus();
		Constructor<?> constructor = getConstructor(paramsTypes);
		return (T) constructor.newInstance(paramValus);
	}
	
	
	/**
	 * Create instance object of this RClass.   Wrapped with RInstance.
	 * @param params params
	 * @return
	 * @throws NoSuchMethodException NoSuchMethodException
	 * @throws IllegalAccessException IllegalAccessException
	 * @throws InvocationTargetException InvocationTargetException
	 * @throws InstantiationException InstantiationException
	 */
	public RInstance newWrappedInstance(Object... params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return new RInstance(newInstance(params));
	}

	@Override
	RClass getReflectClass() {
		return this;
	}

	@Override
	Object getInstance() {
		return null;
	}

	public String getClassName() {
		return className;
	}

	/**
	 * Get the real class, it should be cached
	 * @return result
	 */
	public Class<?> getClassObj() {
		return classObj;
	}
	
	/**
	 * Get the Constructor from cache or reflect
	 * @param parameterTypes parameterTypes
	 * @return result
	 * @throws NoSuchMethodException NoSuchMethodException
	 */
	public Constructor<?> getConstructor(Class<?>... parameterTypes) throws NoSuchMethodException {
		String key = className + "(" + RParam.typeToString(parameterTypes) + ")";
		Constructor<?> constructor = sConstructorMap.get(key);
		if (constructor == null) {
			if (sConstructorMap.containsKey(key)) {
				throw new NoSuchMethodException(key);
			} else {
				try {
					constructor = classObj.getDeclaredConstructor(parameterTypes);
					constructor.setAccessible(true);
				} finally {
					sConstructorMap.put(key, constructor);
				}
			}
		}
		return constructor;
	}
}
