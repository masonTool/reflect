# reflect

[中文版更详细哦](README_CH.md)

## Background

  Java reflect operation always confuse me, cause is not so intuitive. And it's low efficiency lead us can not use it very frequently. This reflect tool can help you simplify the operation and cache the reflect results. Hope can help you.

## Library projects

Download [the latest JAR][1] or grab via Maven:

For gradle:

        compile 'com.github.masontool:reflect:2.2.0'

For maven:

        <dependency>
          <groupId>com.github.masontool</groupId>
          <artifactId>reflect</artifactId>
          <version>2.2.0</version>
        </dependency>

## Useful

I can show you the sample , it's simple;

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

We suppose all the classes, parameters, methods should be reflected. You can do like this:

1. Get / Set static value in class.

        RClass clazzA = new RClass("com.mason.meizu.sample.prvclass.ClassA");
        clazzA.setValue("staticString", "static changed!!!!!");
        String staticString = clazzA.getValue("staticString");

2. Get / Set normal value in class.

        RInstance instanceA = clazzA.newWrappedInstance();
        instanceA.setValue("normalString", "normal changed!!!!!");
        String normalString = instanceA.getValue("normalString");

3. Excute static method.

        Integer plusResult = clazzA.execute("plus", Integer.class, 5, Integer.class, 4);

4. Excute normal method.

        long minusResult = instanceA.execute("minus", long.class, 5, long.class, 4);

5. Support nested call. Here is a complex sample

		RClass clazzB = new RClass("com.mason.meizu.sample.prvclass.ClassB");
		RClass clazzC = new RClass("com.mason.meizu.sample.prvclass.ClassC");
		int complexResult1 = clazzA.execute("plus", 
				clazzB, clazzB.newInstance(), 
				clazzC, clazzC.newInstance());

6. Support interface instantiation. 

    * First of all. Define a class which implemented the method of this interface {Listener}. (Of course, it can't be standard. the interface is a hide class. Just use the same method name, and same parameter type.)

            public class Demo {
            
                //If the parameter is a hide type. you can use Object[] to instead. 
                //public int onChange(Object[] i) {
                //  return (int)i[0];
                //}
                
                public int onChange(int i) {
                    return i;
                }
            }

    * Next. Init RInterface and  newInstance with previous class Demo.   

            RInterface in = new RInterface("com.mason.meizu.sample.prvclass.ClassD4Listener$Listener");
            RClass lis = new RClass("com.mason.meizu.sample.prvclass.ClassD4Listener");
            Object interface1 = in.newInstance(new Demo());
            int testInterfaceResult = lis.newWrappedInstance().execute("testInterface", in, interface1, int.class, 8);




## Feedback
   Any question you can contact me with email 307416073@qq.com.

[1]: https://search.maven.org/remote_content?g=com.github.masontool&a=reflect&v=LATEST
