# reflect

## Background

  Java reflect operation always confuse me, cause is not so intuitive. And it's low efficiency lead us can not use it very frequently. This reflect tool can help you simplify the operation and cache the reflect results. Hope can help you.

## Library projects

Download [the latest JAR][1] or grab via Maven:

For gradle:

        compile 'com.github.masontool:reflect:2.0.0'

For maven:

        <dependency>
          <groupId>com.github.masontool</groupId>
          <artifactId>reflect</artifactId>
          <version>2.0.0</version>
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

We suppose all the classes, parameters, methods should be reflected. You can do like this:

1. Get / Set static value in class.

        RClass clazzA = new RClass("com.mason.meizu.sample.prvclass.ClassA");
        clazzA.setValue("staticString", "static changed!!!!!");
        String staticString = (String) clazzA.getValue("staticString");

2. Get / Set normal value in class.

        Object instance = clazzA.newInstance(null);//get instance
        RInstance instanceA = new RInstance(instance); //wrap with RInstance
        instanceA.setValue("normalString", "normal changed!!!!!");
        String normalString = (String) instanceA.getValue("normalString");

3. Excute static method.

        //integer defaul, no need to specify the type
        RParam plusParam = RParam.create(5, 4);
        Integer plusResult = (Integer) clazzA.execute("plus", plusParam);

4. Excute normal method.

        RParam minusParam = new RParam.Builder()
                .add(long.class, 5)
                .add(long.class, 4)
                .create();
        long minusResult = (long) instanceA.execute("minus", minusParam);

5. Support nested call. Here is a complex sample

        RClass clazzB = new RClass("com.mason.meizu.sample.prvclass.ClassB");
        RInstance instanceB = new RInstance(clazzB.newInstance(null));
        RClass clazzC = new RClass("com.mason.meizu.sample.prvclass.ClassC");
        RInstance instanceC = new RInstance(clazzC.newInstance(null));
        RParam complexParam = RParam.create(instanceB, instanceC);
        int complexResult = (int) clazzA.execute("plus", complexParam);


## Feedback
   Any question you can contact me with email 307416073@qq.com.

[1]: https://search.maven.org/remote_content?g=com.github.masontool&a=reflect&v=LATEST
