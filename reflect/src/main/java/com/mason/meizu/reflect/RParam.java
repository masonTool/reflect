package com.mason.meizu.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * 反射参数构建
 */
class RParam {
	private final Object[] paramValus;
	private final String paramString;
	private final Class<?>[] paramTypes;
	
	/**
	 * Simple method to create RParam. Just like (Class1, value1, Class2, value2, Class3, value3...)
	 * <p>If it goes something wrong, use  {@link RParam.Builder } to instead.
	 * @param params
	 * @return
	 */
	static RParam create(Object... params) {
		if (params == null || params.length % 2 != 0) {
			throw new IllegalArgumentException("params can't be null or length odd");
		}
		List<RInstance> list = new ArrayList<RInstance>();
		for (int index = 0; index < params.length; index +=2) {
			Object type = params[index];
			Object value = params[index + 1];
			
			if (type instanceof RClass) {
				list.add(new RInstance((RClass)type, value));
			} else if (type instanceof Class<?>) {
				list.add(new RInstance(( Class<?>)type, value));
			} else if (type instanceof String){
				try {
					list.add(new RInstance((String)type, value));
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException("params error type, in " + index + " of params" );
				}
			} else {
				throw new IllegalArgumentException("params error type, in " + index + " of params" );
			}
		}
		return new RParam(list);
	}
	
	private RParam(List<RInstance> list) {
		if (list == null) {
			paramValus = null;
			paramString = "";
			paramTypes = null;
		} else {
			int length = list.size();
			paramValus = new Object[length];
			String str = null;
			paramTypes = new Class[length];
			for (int i = 0; i < length; ++i) {
				RInstance instance = list.get(i);
				paramValus[i] = instance.getInstance();
				paramTypes[i] = instance.getReflectClass().getClassObj();
				String name = instance.getReflectClass().getClassName();
				if (str == null) {
					str = name;
				} else {
					str = str + ", " + name;
				}
			}
			paramString = str;
		}
	}

	/**
	 * 获取参数的类型列表
	 */
	Class<?>[] getTypes() {
		return paramTypes;
	}

	/**
	 * 获取参数值列表
	 */
	Object[] getValus() {
		return paramValus;
	}

	/**
	 * 获取参数列表String
	 */
	String getString() {
		return paramString;
	}
}
