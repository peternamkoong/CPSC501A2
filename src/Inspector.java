import java.lang.reflect.*;

public class Inspector {

    /**
     * inspect method is the method call in the Driver class which starts the reflection from each passed in object.
     * @param obj           Object
     * @param recursive     boolean which determines if the field types will be recursively checked
     */
    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    /**
     * inspectClass method is the main method which calls other inspect methods to check the details within the class.
     * @param c             The class which is to be inspected
     * @param obj           The object which is to be inspected
     * @param recursive     The boolean which determines if the field types will be recursively checked
     * @param depth         int value which determines how deep the recursion is
     */
    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        String tab = tabDepth(depth); //tabs based on depth

        if (c.isArray()) { //checks if the passed class is an array right away
            Class arrayClass = c.getComponentType();
            System.out.println(tab + "Component Type: " + arrayClass.getName());
            Object[] classValue = (Object[]) obj;
            inspectArray(classValue, recursive, depth,tab);
        }
        else {
            inspectInformation(c,tab);                      //inspect Class Name and Super Class Name
            inspectInterface(c, obj, recursive, tab);       //inspect Interface of the current class
            inspectConstructor(c, obj, recursive, tab);     //inspect Constructors of the current class
            inspectMethod(c, obj, recursive, tab);          //inspect methods of the current class
            inspectField(c, obj, recursive, depth, tab);    //inspect Fields of the current class
            inspectSuperClass(c, obj, recursive, depth);    //recursively inspect superclass, if possible
        }
    }

    /**
     * tabDepth method which creates a string of N tabs, dependent on the depth
     * @param depth         the int value which determines how deep the recursion is
     * @return              String of the N tabs
     */
    public String tabDepth(int depth) {
        String tab = "";
        for (int i = 0; i < depth; i++)
            tab += "\t";
        return tab;
    }

    /**
     * inspectInformation that prints the current class name and it's super class
     * @param c         the current class
     * @param tab       the tab size that is based on the depth
     */
    public void inspectInformation(Class c, String tab) {
        sectionPrintStart(c, tab, "INFORMATION");
        Class superClass = c.getSuperclass();
        String superClassName = "";
        if (superClass != null)
            superClassName = superClass.getName();
        else
            superClassName = "Does not exist.";
        System.out.println(tab + "Class: " + c.getName()); //Print Class Name
        System.out.println(tab + "Super Class: " + superClassName); //Print Super Class Name
        sectionPrintFinish(c, tab, "INFORMATION");
    }

    /**
     * inspectSuperClass method recursively calls the inspectClass method if there is a superclass of the class c
     * @param c             the current class
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param depth         the depth in the recursion
     */
    public void inspectSuperClass(Class c, Object obj, boolean recursive, int depth) {
        if (c.getSuperclass() != null)
            inspectClass(c.getSuperclass(), obj, recursive, depth + 1);
    }

    /**
     * inspectInterface method prints out the interfaces of the current class
     * @param c             the current class
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param tab           the string of tabs based on the depth
     */
    public void inspectInterface(Class c, Object obj, boolean recursive, String tab) {
        sectionPrintStart(c,tab,"INTERFACE");
        Class[] allInterfaces = c.getInterfaces();
        for (Class interfaces : allInterfaces)
            System.out.println(tab + "Interface: " + interfaces.getName());
        sectionPrintFinish(c, tab, "INTERFACE");
    }

    /**
     * inspectConstructor method which prints out all the constructors of the current class
     * @param c             the current class
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param tab           the string of tabs based on the depth
     */
    public void inspectConstructor(Class c, Object obj, boolean recursive, String tab){
        sectionPrintStart(c,tab, "CONSTRUCTOR");
        Constructor[] allConstructors = c.getDeclaredConstructors();
        for (Constructor constructors : allConstructors) {
            int modifier = constructors.getModifiers();
            String modifierName = Modifier.toString(modifier);
            String name = constructors.getName();

            Class[] allParameters = constructors.getParameterTypes();

            System.out.print(tab + "Constructor: " + modifierName + " " + name + " ( ");

            for (Class parameters : allParameters)
                System.out.print(parameters.getName() + " ");
            System.out.print(") \n");
        }
        sectionPrintFinish(c, tab, "CONSTRUCTOR");
    }

    /**
     * the inspectMethod method which prints out all the methods within the current class
     * @param c             the current class
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param tab           the string of tabs based on the depth
     */
    public void inspectMethod(Class c, Object obj, boolean recursive, String tab){
        sectionPrintStart(c, tab, "METHOD");
        Method[] allMethods = c.getDeclaredMethods();
        for (Method methods : allMethods) {

            int modifier = methods.getModifiers();
            String modifierName = Modifier.toString(modifier);
            String returnType = methods.getReturnType().getName();
            String name = methods.getName();
            Class[] allParameters = methods.getParameterTypes();

            System.out.print(tab + "Method: " + modifierName + " " + returnType + " " + name + " ( ");

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

    /**
     * the inspectField method, which prints out each of the fields instantiated in the class
     * @param c             the current class
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param tab           the string of tabs based on the depth
     * @param depth         the int value of how deep the recursion is
     */
    public void inspectField(Class c, Object obj, boolean recursive, int depth, String tab){
        sectionPrintStart(c,tab,"FIELD");
        Field[] allFields = c.getDeclaredFields();
        for (Field fields : allFields) {
            try {
                fields.setAccessible(true);

                int modifier = fields.getModifiers();
                String modifierName = Modifier.toString(modifier);
                String typeName = fields.getType().getSimpleName();
                String name = fields.getName();
                Class fieldType = fields.getType();

                System.out.print(tab + "Field: " + modifierName + " " + typeName + " " + name + " = ");

                if (fieldType.isArray()) {
                    Object fieldValue = fields.get(obj);
                    inspectArray(fieldValue, recursive, depth,tab);
                }

                else {
                    Object value = fields.get(obj);
                    System.out.print(value);
                    System.out.println();
                    if (value!=null)
                        inspectFieldType(fieldType, value, recursive, depth,tab,0);
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sectionPrintFinish(c, tab, "FIELD");
    }

    /**
     * the inspectFieldType method checks to see if the field is primitive, and if it isn't, then calls inspectClass on
     * the field itself.
     * @param c             the current class
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param tab           the string of tabs based on the depth
     * @param depth         the int value of how deep the recursion is
     */
    public void inspectFieldType(Class c,Object obj,boolean recursive, int depth, String tab, int array){
        if ((c.isPrimitive()
                || c == java.lang.String.class
                || c == java.lang.Integer.class
                || c == java.lang.Boolean.class
                || c == java.lang.Long.class
                || c == java.lang.Character.class)) {
            if (array == 1) {
                System.out.print(" " + c.getSimpleName());
            }
        }
        else {
            if (recursive == true) {
                sectionPrintStart(c, tab, "RECURSIVE");
                inspectClass(c, obj, recursive, depth + 1);
                sectionPrintFinish(c, tab, "RECURSIVE");
            }
            else if (recursive == false) {
                System.out.print(" " + c.getName());
            }
        }
    }

    /**
     * the inspectArray method checks each element in the array and
     * @param obj           the current object
     * @param recursive     the recursive boolean value
     * @param tab           the string of tabs based on the depth
     * @param depth         the int value of how deep the recursion is
     */
    public void inspectArray(Object obj, boolean recursive, int depth, String tab) {
        int length = Array.getLength(obj);
        System.out.print(tab+ "[");
        for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(obj, i);
            if (arrayElement != null) {
                Class elementClass = arrayElement.getClass();
                inspectFieldType(elementClass,obj, recursive, depth+1, tab, 1);
            }
            else
                System.out.print(" null ");
        }
        System.out.println("]");
    }

    /**
     * sectionPrintStart prints the start of all the inspection sections
     * @param c             the current class
     * @param tab           the string of tabs based on the depth
     * @param section       the section it is printing
     */
    public void sectionPrintStart(Class c, String tab, String section) {
        System.out.println("\n" + tab + "======================================================");
        System.out.println(tab + section + " SECTION: " + "Class: " + c.getName());
        System.out.println(tab + "======================================================" + "\n");
    }

    /**
     * sectionPrintFinish prints the end of the inspection section
     * @param c             the current class
     * @param tab           the string of tabs based on the depth
     * @param section       the section it is printing
     */
    public void sectionPrintFinish(Class c,String tab, String section) {
        System.out.println("\n" + tab + "======================================================");
        System.out.println(tab + section + " SECTION " + "COMPLETE: " + c.getName());
        System.out.println(tab + "======================================================" + "\n");
    }
}
