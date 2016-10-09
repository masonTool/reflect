package com.mason.meizu.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * 反射参数构建
 */
public class RParam {
	private final Object[] paramValus;
	private final String paramString;
	private final Class<?>[] paramTypes;
	
	/**
	 * Simple method to create RParam. Be careful to use is. 
	 * <p>If it goes something wrong, use  {@link RParam.Builder } to instead.
	 * @param params
	 * @return
	 */
	public static RParam create(Object... params) {
		if (params == null) {
			throw new IllegalArgumentException("params can't be null");
		}
		Builder builder = new Builder();
		for (Object obj : params) {
			builder.add(obj);
		}
		return builder.create();
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
	public Class<?>[] getTypes() {
		return paramTypes;
	}

	/**
	 * 获取参数值列表
	 */
	public Object[] getValus() {
		return paramValus;
	}

	/**
	 * 获取参数列表String
	 */
	public String getString() {
		return paramString;
	}

	/**
	 * 构建反射参数列表
	 */
	public static class Builder {
		private List<RInstance> list;

		public Builder() {
			list = new ArrayList<RInstance>();
		}

		public Builder add(RClass type, Object value) {
			list.add(new RInstance(type, value));
			return this;
		}

		public Builder add(Class<?> type, Object value) {
			list.add(new RInstance(type, value));
			return this;
		}

		public Builder add(String type, Object value) throws ClassNotFoundException {
			list.add(new RInstance(type, value));
			return this;
		}

		public Builder add(Object value) {
			list.add(new RInstance(value));
			return this;
		}

		public Builder add(RInstance instance) {
			if (instance == null) {
				throw new IllegalArgumentException("instance can't be null");
			}
			list.add(instance);
			return this;
		}

		public RParam create() {
			return new RParam(list);
		}
	}
}
