# 反射工具

## 背景

  Java的反射是一个麻烦事儿。 因为java反射的封装设计面对用户是不太友好的。 同时在使用的时候又有些反人类， 没法使开发者按照正常的思维去使用。同时效率低下也是一个问题。为了解决这些问题， 我决定对反射做一些必要的封装， 同时cache住反射的结果， 以便于提高效率。 希望这个工具可以使你的生活变的美好一点。。

## 获取

你可以下载最新的 [JAR包][1] 或者通过以下方式引入工程 :

通过 gradle:

        compile 'com.github.masontool:reflect:2.2.0'

通过 maven:

        <dependency>
          <groupId>com.github.masontool</groupId>
          <artifactId>reflect</artifactId>
          <version>2.2.0</version>
        </dependency>

## 使用

本工具的使用非常直观和简单。 先展示几个示例类。 后面我们将这些类的所有类， 方法， 参数等进行反射;

ClassA:

        package com.mason.meizu.sample.prvclass;
        class ClassA {
            protected static String staticString = "HELLO";
            private String normalString = "WORLD";
            private static Integer plus(Integer a, Integer b) {
                return a+b;
            }
            private long minus(long a, long b) {
                return a - b;
            }
            private static int plus(ClassB b, ClassC c) {
                return b.value + c.value;
            }
        }

ClassB:

        package com.mason.meizu.sample.prvclass;
        class ClassB {
            int value = 5;
        }

ClassC:

        package com.mason.meizu.sample.prvclass;
        class ClassC {
            int value = 4;
        }

ClassD4Listener:

        package com.mason.meizu.sample.prvclass;
        class ClassD4Listener {
    
            public int testInterface(Listener listener, int i) {
                return listener.onChange(i);
            }
            
            private static interface Listener {
                int onChange(int i);
            }
        }

我们认为上面的类都是隐藏的。 对使用者来说完全不可见。 如果要使用他们只能进行反射。 下面是反射的过程.

1. 读取/设置 类的静态变量.

        RClass clazzA = new RClass("com.mason.meizu.sample.prvclass.ClassA");
        clazzA.setValue("staticString", "static changed!!!!!");
        String staticString = clazzA.getValue("staticString");

2. 读取/设置 类的内部变量. 

        RInstance instanceA = clazzA.newWrappedInstance();//或者你也可以这样 RInstance instanceA = new RInstance(clazzA.newInstance());
        instanceA.setValue("normalString", "normal changed!!!!!");
        String normalString = instanceA.getValue("normalString");
        //这里可以看到端倪了吧， RClass就是类， RInstance就是实例。发挥你的想象， 实例也是可以读取/设置静态变量的对不对？
        //所以  instanceA.getValue("staticString");    instanceA.getValue("staticString");  这两个操作也是OK的。

3. 执行静态方法. (符合你的调用习惯)
		
        Integer plusResult = clazzA.execute("plus", Integer.class, 5, Integer.class, 4);

4. 执行类方法.

        long minusResult = instanceA.execute("minus", long.class, 5, long.class, 4);
        //其实instanceA 执行静态方法也没有问题。

5. 工具最大的优势，可以嵌套调用。复杂度丝毫没有增加.

		RClass clazzB = new RClass("com.mason.meizu.sample.prvclass.ClassB");
		RClass clazzC = new RClass("com.mason.meizu.sample.prvclass.ClassC");
		int complexResult1 = clazzA.execute("plus", 
				clazzB, clazzB.newInstance(), 
				clazzC, clazzC.newInstance());
		//顺便提醒一下。 execute方法的参数类型是很灵活的。 
		//可以是RClass就像本例
		//可以是Class<?>参见3. 4. 
		//还可以是类名String. 比如本例还可以写成  		
		//		int complexResult1 = clazzA.execute("plus", 
		//		"com.mason.meizu.sample.prvclass.ClassB", clazzB.newInstance(), 
		//		"com.mason.meizu.sample.prvclass.ClassC", clazzC.newInstance());

6. 支持接口实例化 

    * 首先. 定义一个实现了接口(本例中是ClassD4Listener$Listener)的类. (当然不是标准的实现，因为接口是隐藏的。 只要保证做到方法名， 参数都一样就可以了) 。

    		//类名随意定义即可
            public class Demo {
            
                //接口参数中如果出现了隐藏类型， 我们还可以这样定义
                //public int onChange(Object[] i) {
                //  return (int)i[0];
                //}
                
                //实现了ClassD4Listener$Listener的接口方法
                public int onChange(int i) {
                    return i * 2;
                }
            }

    * 然后. 实例化接口。

    		//定义接口
            RInterface in = new RInterface("com.mason.meizu.sample.prvclass.ClassD4Listener$Listener");
            //实例化接口(.....就是这么简单)
            Object interface1 = in.newInstance(new Demo());

            //下面验证一下
            RClass lis = new RClass("com.mason.meizu.sample.prvclass.ClassD4Listener");
            int testInterfaceResult = lis.newWrappedInstance().execute("testInterface", in, interface1, int.class, 8);
            //因为我们的实现中，是返回  i*2, 所以结果就是16

    * 最后说明一下。你可以认为本例中定义的demo就是此接口的实例。 通过newInstance生成的实例是此demo实例的代理。所以对获得的interface1执行的所有操作和调用， 都是调用了demo的方法。 



## 反馈
   有任何问题你可以在github上提问， 或者联系我邮箱 email 307416073@qq.com.

[1]: https://search.maven.org/remote_content?g=com.github.masontool&a=reflect&v=LATEST
