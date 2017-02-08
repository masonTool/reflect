package com.mason.meizu.reflect;

/**
 * 反射实例类
 * 
 * @author mason 20160309
 */
public class RInstance extends RExecutor {

	protected RClass reflectClass;
	protected Object instance;

	/**
	 * 构造函数, 构造反射实例
	 * 
	 * @param reflectClass
	 *            当前实例对应的类。
	 * @param instance
	 *            当前实例
	 */
	public RInstance(RClass reflectClass, Object instance) {
		if (reflectClass == null) {
			throw new IllegalArgumentException("reflectClass can't be null");
		}
		this.reflectClass = reflectClass;
		this.instance = instance;
	}

	/**
	 * 构造函数， 构造反射实例
	 * 
	 * @param type 类型
	 * @param instance 值
	 */
	public RInstance(Class<?> type, Object instance) {
		this(new RClass(type), instance);
	}

	/**
	 * 构造函数, 构造反射实例
	 * 
	 * @param className className
	 * @param loader loader
	 * @param instance instance
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public RInstance(String className, ClassLoader loader, Object instance) throws ClassNotFoundException {
		this(new RClass(className, loader), instance);
	}

	/**
	 * 构造函数， 构造反射实例
	 * 
	 * @param className 类型名
	 * @param value 值
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	public RInstance(String className, Object value) throws ClassNotFoundException {
		this(className, null, value);
	}

	/**
	 * Be careful to be used. Attention to it's Class 
	 * @param instance instance
	 */
	public RInstance(Object instance) {
		if (instance == null) {
			throw new IllegalArgumentException("instance can't be null");
		}
		this.reflectClass = new RClass(instance.getClass());
		this.instance = instance;
	}

	@Override
	RClass getReflectClass() {
		return reflectClass;
	}

	@Override
	Object getInstance() {
		return instance;
	}
}
