import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        //tab for recursive
        String tab = "";
        for (int i = 0; i < depth; i++) {
            tab += "\t";
        }
        if (c.isArray()) {
            Class arrayClass = c.getComponentType();
            System.out.println("Component Type: " + arrayClass.getName());

        } else {
            System.out.println(tab + "******* CLASS INFORMATION: " + c.getSimpleName() + " *******");
            //Print Class Name
            System.out.println(tab + "Class: " + c.getSimpleName());

            //Print Super Class Name, if exists
            if (c.getSuperclass() != null) {
                System.out.println(tab + "Super Class: " + c.getSuperclass().getSimpleName());
            } else {
                System.out.println(tab + "Super Class: DOES NOT EXIST");
            }

            //Print all the Interfaces
            System.out.println("\n" + tab + "******* " + c.getSimpleName() + " INTERFACE SECTION *******");
            Class[] allInterfaces = c.getInterfaces();
            for (Class interfaces : allInterfaces) {
                System.out.println(tab + "Interface: " + interfaces);
            }
            System.out.println(tab + "******* " + c.getSimpleName() + " INTERFACE SECTION FINISHED ******* \n");

            System.out.println("\n" + tab + "******* " + c.getSimpleName() + " CONSTRUCTOR SECTION *******");
            //Print all the Constructors
            Constructor[] allConstructors = c.getDeclaredConstructors();
            for (Constructor constructors : allConstructors) {
                System.out.print(tab + "Constructor: ");

                //Print Constructor Modifier
                int modifier = constructors.getModifiers();
                System.out.print(Modifier.toString(modifier) + " ");

                //Print Constructor Name
                System.out.print(constructors.getName() + " ");
                //Print Constructor Parameters
                Class[] allParameters = constructors.getParameterTypes();
                System.out.print("(");
                for (Class parameters : allParameters) {
                    System.out.print(parameters.getName() + " ");
                }
                System.out.print(")");
                System.out.println();
            }
            System.out.println(tab + "******* " + c.getSimpleName() + " CONSTRUCTOR SECTION FINISHED ******* \n");

            System.out.println("\n" + tab + "******* " + c.getSimpleName() + " METHOD SECTION *******");
            Method[] allMethods = c.getDeclaredMethods();
            for (Method methods : allMethods) {
                System.out.print(tab + "Method: ");

                //Print Method Modifier
                int modifier = methods.getModifiers();
                System.out.print(Modifier.toString(modifier) + " ");

                //Print Return Type
                System.out.print(methods.getReturnType().getSimpleName() + " ");

                //Print Method Name
                System.out.print(methods.getName());

                //Print Parameter Types
                Class[] allParameters = methods.getParameterTypes();
                System.out.print("(");
                for (Class parameters : allParameters) {
                    System.out.print(parameters.getSimpleName() + " ");
                }
                System.out.print(")" + "Throws ");
                Class[] allExceptions = methods.getExceptionTypes();
                for (Class exceptions : allExceptions) {
                    System.out.print(exceptions.getSimpleName() + " ");
                }
                System.out.println();
            }
            System.out.println(tab + "******* " + c.getSimpleName() + " METHOD SECTION FINISHED ******* \n");

            System.out.println("\n" + tab + "******* " + c.getSimpleName() + " FIELD SECTION *******");
            Field[] allFields = c.getDeclaredFields();
            for (Field fields : allFields) {
                try {

                    fields.setAccessible(true);
                    System.out.print(tab + "Field: ");

                    //Print Field Modifier
                    int modifier = fields.getModifiers();
                    System.out.print(Modifier.toString(modifier) + " ");

                    //Print Field Type
                    System.out.print(fields.getType().getSimpleName() + " ");

                    //Print Field Name
                    System.out.print(fields.getName() + " = ");

                    Class fieldType = fields.getType();
                    if (fieldType.isArray()) {
                        Object fieldValue = fields.get(obj);
                        int length = Array.getLength(fieldValue);
                        System.out.print("[");
                        for (int i = 0; i < length; i++) {
                            Object arrayElement = Array.get(fieldValue, i);
                            System.out.print(arrayElement);
                            if (i != length - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println("]");
                    } else {
                        //Print Field Value
                        Object value = fields.get(obj);
                        System.out.print(value);
                        System.out.println();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
            System.out.println(tab + "******* " + c.getSimpleName() + " FIELD SECTION FINISHED ******* \n");
            //Recursively call inspectClass on superclass
            if (c.getSuperclass() != null) {
                inspectClass(c.getSuperclass(), obj, recursive, depth + 1);
            }
        }
    }

}
