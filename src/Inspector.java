import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        //tab for recursive
        String tab = tabDepth(depth);

        if (c.isArray()) {
            Class arrayClass = c.getComponentType();
            System.out.println("Component Type: " + arrayClass.getName());
            Object[] classValue = (Object[]) obj;
            inspectArray(classValue, recursive, depth,tab);
        }
        else {
            sectionPrintStart(c, tab, "INFORMATION");
            //Print Class Name
            System.out.println(tab + "Class: " + c.getName());

            //Print Super Class Name, if exists
            if (c.getSuperclass() != null)
                System.out.println(tab + "Super Class: " + c.getSuperclass().getName());
            else
                System.out.println(tab + "Super Class: DOES NOT EXIST");

            //Print all the Interfaces
            inspectInterface(c, obj, recursive, tab);
            inspectConstructor(c, obj, recursive, tab);
            inspectMethod(c, obj, recursive, tab);
            inspectField(c, obj, recursive, depth, tab);
            inspectSuperClass(c, obj, recursive, depth);
            sectionPrintFinish(c, tab, "INFORMATION");
        }
    }

    public String tabDepth(int depth) {
        String tab = "";
        for (int i = 0; i < depth; i++)
            tab += "\t";
        return tab;
    }

    public void inspectSuperClass(Class c, Object obj, boolean recursive, int depth) {
        if (c.getSuperclass() != null)
            inspectClass(c.getSuperclass(), obj, recursive, depth + 1);
    }

    public void inspectInterface(Class c, Object obj, boolean recursive, String tab) {
        sectionPrintStart(c,tab,"INTERFACE");
        Class[] allInterfaces = c.getInterfaces();
        for (Class interfaces : allInterfaces)
            System.out.println(tab + "Interface: " + interfaces);
        sectionPrintFinish(c, tab, "INTERFACE");
    }

    public void inspectConstructor(Class c, Object obj, boolean recursive, String tab){
        sectionPrintStart(c,tab, "CONSTRUCTOR");
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
            System.out.print("( ");
            for (Class parameters : allParameters)
                System.out.print(parameters.getName() + " ");
            System.out.print(") \n");
        }
        sectionPrintFinish(c, tab, "CONSTRUCTOR");
    }

    public void inspectMethod(Class c, Object obj, boolean recursive, String tab){
        sectionPrintStart(c, tab, "METHOD");
        Method[] allMethods = c.getDeclaredMethods();
        for (Method methods : allMethods) {
            System.out.print(tab + "Method: ");

            //Print Method Modifier
            int modifier = methods.getModifiers();
            System.out.print(Modifier.toString(modifier) + " ");

            //Print Return Type
            System.out.print(methods.getReturnType().getName() + " ");

            //Print Method Name
            System.out.print(methods.getName());

            //Print Parameter Types
            Class[] allParameters = methods.getParameterTypes();
            System.out.print("( ");

            for (Class parameters : allParameters)
                System.out.print(parameters.getName() + " ");

            System.out.print(")");
            Class[] allExceptions = methods.getExceptionTypes();
            for (Class exceptions : allExceptions)
                System.out.print(" Throws " + exceptions.getName() + " ");
            System.out.println();
        }
        sectionPrintFinish(c, tab, "METHOD");
    }

    public void inspectField(Class c, Object obj, boolean recursive, int depth, String tab){
        sectionPrintStart(c,tab,"FIELD");
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
                    inspectArray(fieldValue, recursive, depth,tab);
                }
                else {
                    Object value = fields.get(obj);
                    System.out.print(value);
                    System.out.println();
                    if (value!=null) {
                        //System.out.println("RECURSIVE");
                        inspectFieldType(fieldType, value, recursive, depth,tab);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sectionPrintFinish(c, tab, "FIELD");
    }

    public void inspectFieldType(Class c,Object obj,boolean recursive, int depth, String tab){
        if (c.isPrimitive()
                || c == java.lang.String.class
                || c == java.lang.Integer.class
                || c == java.lang.Boolean.class
                || c == java.lang.Long.class
                || c == java.lang.Character.class){
            //System.out.print(c.getName());
        }
        else{
            if (recursive == true) {
                sectionPrintStart(c, tab, "RECURSIVE");
                inspectClass(c, obj, recursive, depth +1);
                sectionPrintFinish(c, tab, "RECURSIVE");
            }
        }
    }

    public void inspectArray(Object obj, boolean recursive, int depth, String tab) {
        int length = Array.getLength(obj);
        System.out.print("[");
        for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(obj, i);
            if (arrayElement != null) {
                Class elementClass = arrayElement.getClass();
                inspectFieldType(elementClass,obj, recursive, depth+1, tab);
            }
            else
                System.out.print(" null ");
        }
        System.out.println("]");
    }


    public void sectionPrintStart(Class c, String tab, String section) {
        System.out.println("\n" + tab + "======================================================");
        System.out.println(tab + section + " SECTION: " + "Class: " + c.getName());
        System.out.println(tab + "======================================================" + "\n");
    }

    public void sectionPrintFinish(Class c,String tab, String section) {
        System.out.println("\n" + tab + "======================================================");
        System.out.println(tab + section + " SECTION " + "COMPLETE: " + c.getName());
        System.out.println(tab + "======================================================" + "\n");
    }
}
