package com.mason.meizu.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 公共执行类， 提供方法供反射类{@link RClass}和反射实例{@link RInstance}使用
 *
 * @author mason 20160309
 */
abstract class RExecutor {

	private static final HashMap<String, Method> sMethodMap = new HashMap<String, Method>();
	private static final HashMap<String, Field> sFieldMap = new HashMap<String, Field>();

	/**
	 * 执行一个方法.
	 * <p>
	 * 反射类{@link RClass}调用时，是执行静态方法。反射实例{@link RInstance}调用时，
	 * 是执行实例方法。
	 * <p>
	 * 如果执行的方法非当前类定义的方法. 需设置target指定目标类。没有指定目标类，
	 * 调用{@link #execute(String, RParam)}即可。 这种情况会从当前类中查找。
	 * 
	 * @param target 目标类.
	 * @param methodName 方法名
	 * @param params 方法参数 The format like  (Class1, value1, Class2, value2, Class3, value3...)
	 * @return 执行结果
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public <T> T execute(RClass target, String methodName, Object... params)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		if (target == null) {
			target = getReflectClass();
		}
		RParam rParam = params == null ? null : RParam.create(params);
		Class<?>[] paramsTypes = rParam == null ? null : rParam.getTypes();
		String paramString = rParam == null ? "" : rParam.getString();
		Object[] paramValus = rParam == null ? null : rParam.getValus();

		String key = target.getClassName() + "." + methodName + "(" + paramString + ")";
		Method method = sMethodMap.get(key);
		if (method == null) {
			if (sMethodMap.containsKey(key)) {
				throw new NoSuchMethodException(key);
			} else {
				try {
					method = target.getClassObj().getDeclaredMethod(methodName, paramsTypes);
					method.setAccessible(true);
				} finally {
					sMethodMap.put(key, method);
				}
			}
		}
		return (T) method.invoke(getInstance(), paramValus);
	}

	public <T> T execute(String methodName, Object... params)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return execute(null, methodName, params);
	}
	
	/**
	 * 为一个参数赋值。
	 * <p>
	 * 反射类{@link RClass}调用时，是为静态参数赋值。反射实例{@link RInstance}调用时，
	 * 是为实例参数或静态参数(如果确定是静态参数)赋值。
	 * <p>
	 * 如果参数非当前类定义. 需设置target指定目标类。没有指定目标类，
	 * 调用{@link #setValue(String, Object)}即可。 这种情况会从当前类中查找参数。
	 * 
	 * @param target
	 *            目标类.
	 * @param fieldName
	 *            参数名
	 * @param value
	 *            赋值
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public void setValue(RClass target, String fieldName, Object value)
			throws NoSuchFieldException, IllegalAccessException {
		if (target == null) {
			target = getReflectClass();
		}

		String key = target.getClassName() + "." + fieldName;
		Field field = sFieldMap.get(key);
		if (field == null) {
			if (sFieldMap.containsKey(key)) {
				throw new NoSuchFieldException(key);
			} else {
				try {
					field = target.getClassObj().getDeclaredField(fieldName);
					field.setAccessible(true);
				} finally {
					sFieldMap.put(key, field);
				}
			}
		}
		field.set(getInstance(), value);
	}

	/**
	 * 为参数赋值
	 * 
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public void setValue(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
		setValue(null, fieldName, value);
	}

	/**
	 * 获取参数值
	 * <p>
	 * 反射类{@link RClass}调用时，是为获取静态参数值。反射实例{@link RInstance}调用时，
	 * 是获取实例参数值。
	 * <p>
	 * 如果参数非当前类定义. 需设置target指定目标类。没有指定目标类， 调用{@link #getValue(String)}即可。
	 * 这种情况会从当前类中查找参数。
	 * 
	 * @param target
	 *            目标类.
	 * @param fieldName
	 *            参数名
	 * @return 参数值
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(RClass target, String fieldName) throws IllegalAccessException, NoSuchFieldException {
		if (target == null) {
			target = getReflectClass();
		}

		String key = target.getClassName() + "." + fieldName;
		Field field = sFieldMap.get(key);
		if (field == null) {
			if (sFieldMap.containsKey(key)) {
				throw new NoSuchFieldException(key);
			} else {
				try {
					field = target.getClassObj().getDeclaredField(fieldName);
					field.setAccessible(true);
				} finally {
					sFieldMap.put(key, field);
				}
			}
		}
		return (T) field.get(getInstance());
	}

	/**
	 * 获取参数值
	 * 
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public <T> T getValue(String fieldName) throws NoSuchFieldException, IllegalAccessException {
		return getValue(null, fieldName);
	}

	public abstract RClass getReflectClass();

	public abstract Object getInstance();

}
