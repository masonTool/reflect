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
	 * 创建实例
	 * 
	 * @param param
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public Object newInstance(RParam param)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?>[] paramsTypes = param == null ? null : param.getTypes();
		String paramString = param == null ? "" : param.getString();
		Object[] paramValus = param == null ? null : param.getValus();

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
	
	public Object newInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return newInstance(null);
	}
	
	/**
	 * Create RInstance instance
	 * @param param
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public RInstance newRInstance(RParam param) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return new RInstance(newInstance(param));
	}
	
	public RInstance newRInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return newRInstance(null);
	}

	@Override
	protected RClass getReflectClass() {
		return this;
	}

	@Override
	protected Object getInstance() {
		return null;
	}

	public String getClassName() {
		return className;
	}

	public Class<?> getClassObj() {
		return classObj;
	}
}
