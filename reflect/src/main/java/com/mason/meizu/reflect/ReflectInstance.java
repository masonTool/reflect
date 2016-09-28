package com.mason.meizu.reflect;

/**
 * 反射实例类
 * 
 * @author mason 20160309
 */
public class ReflectInstance extends ReflectExecutor {

	protected ReflectClass reflectClass;
	protected Object instance;

	/**
	 * 构造函数, 构造反射实例
	 * 
	 * @param reflectClass
	 *            当前实例对应的类。
	 * @param instance
	 *            当前实例
	 */
	public ReflectInstance(ReflectClass reflectClass, Object instance) {
		if (reflectClass == null) {
			throw new IllegalArgumentException("reflectClass can't be null");
		}
		if (instance == null) {
			throw new IllegalArgumentException("instance can't be null");
		}
		this.reflectClass = reflectClass;
		this.instance = instance;
	}

	/**
	 * 构造函数， 构造反射实例
	 * 
	 * @param type
	 *            类型
	 * @param instance
	 *            值
	 */
	public ReflectInstance(Class<?> type, Object instance) {
		this(new ReflectClass(type), instance);
	}

	/**
	 * 构造函数, 构造反射实例
	 * 
	 * @param className
	 * @param loader
	 * @param instance
	 * @throws ClassNotFoundException
	 */
	public ReflectInstance(String className, ClassLoader loader, Object instance) throws ClassNotFoundException {
		this(new ReflectClass(className, loader), instance);
	}

	/**
	 * 构造函数， 构造反射实例
	 * 
	 * @param className
	 *            类型名
	 * @param value
	 *            值
	 * @throws ClassNotFoundException
	 */
	public ReflectInstance(String className, Object value) throws ClassNotFoundException {
		this(className, null, value);
	}

	/**
	 * 构造函数, 构造反射实例
	 * 
	 * @param instance
	 */
	public ReflectInstance(Object instance) {
		if (instance == null) {
			throw new IllegalArgumentException("instance can't be null");
		}
		this.reflectClass = new ReflectClass(instance.getClass());
		this.instance = instance;
	}

	@Override
	protected ReflectClass getReflectClass() {
		return reflectClass;
	}

	@Override
	protected Object getInstance() {
		return instance;
	}
}
