/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package org.milyn.javabean.pojogen;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.IOException;
import java.util.List;

import org.milyn.io.NullWriter;
import org.milyn.io.StreamUtils;
import org.milyn.javabean.DataDecoder;
import org.milyn.javabean.DecodeType;
import org.milyn.profile.BasicProfile;
import org.milyn.profile.Profile;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class PojoGenTest {

	@Test
    public void test_01() throws IOException {
        JClass aClass = new JClass("com.acme", "AClass");
        JClass bClass = new JClass("com.acme", "BClass");

        aClass.setFluentSetters(true);
        bClass.setFluentSetters(false);

        aClass.addBeanProperty(new JNamedType(new JType(int.class), "primVar"));
        aClass.addBeanProperty(new JNamedType(new JType(Double.class), "doubleVar"));
        aClass.addBeanProperty(new JNamedType(new JType(BBBClass.class), "objVar"));
        aClass.addBeanProperty(new JNamedType(new JType(List.class, BBBClass.class), "genericVar"));

        aClass.getImplementTypes().add(new JType(DataDecoder.class));
        aClass.getImplementTypes().add(new JType(Profile.class));

        aClass.getExtendTypes().add(new JType(NullWriter.class));
        aClass.getExtendTypes().add(new JType(BasicProfile.class));

        aClass.getAnnotationTypes().add(new JType(DecodeType.class));

        // Wire AClass into BClass...
        bClass.addBeanProperty(new JNamedType(new JType(BBBClass.class), "bbbVar"));
        bClass.addBeanProperty(new JNamedType(new JType(aClass.getSkeletonClass()), "aClassVar"));

        StringWriter aWriter = new StringWriter();
        StringWriter bWriter = new StringWriter();

        aClass.writeClass(aWriter);
        String aS = aWriter.toString();
        assertEquals(StreamUtils.trimLines(AClass_Expected), StreamUtils.trimLines(aS));

        bClass.writeClass(bWriter);
        String bS = bWriter.toString();
        assertEquals(StreamUtils.trimLines(BClass_Expected), StreamUtils.trimLines(bS));
    }

	@Test
    public void test_duplicateProperty() {
        JClass aClass = new JClass("com.acme", "AClass");

        aClass.addBeanProperty(new JNamedType(new JType(Double.class), "xVar"));
        try {
            aClass.addBeanProperty(new JNamedType(new JType(Integer.class), "xVar"));
            fail("Exected IllegalArgumentException.");
        } catch(IllegalArgumentException e) {
            assertEquals("Property 'xVar' already defined.", e.getMessage());
        }
    }

    @Test
    public void test_DefaultValue() throws IOException {
	JClass aClass = new JClass("com.acme", "AClass");

	aClass.setFluentSetters(true);

	aClass.addBeanProperty(new JNamedType(new JType(int.class), "primVar"));
	JNamedType defaultValue = new JNamedType(new JType(String.class), "defaultValue");
	defaultValue.setDefaultValue("\"123\"");
	aClass.addBeanProperty(defaultValue);

	aClass.getImplementTypes().add(new JType(DataDecoder.class));
	aClass.getImplementTypes().add(new JType(Profile.class));

	aClass.getExtendTypes().add(new JType(NullWriter.class));
	aClass.getExtendTypes().add(new JType(BasicProfile.class));

	aClass.getAnnotationTypes().add(new JType(DecodeType.class));

	StringWriter aWriter = new StringWriter();

	aClass.writeClass(aWriter);
	String aS = aWriter.toString();
	assertEquals(StreamUtils.trimLines(defaultValue_Expected), StreamUtils.trimLines(aS));
    }

    @Test
    public void test_unmodifiable() throws IOException {
	JClass aClass = new JClass("com.acme", "AClass");

	aClass.setFluentSetters(true);

	aClass.addBeanProperty(new JNamedType(new JType(int.class), "primVar"));
	JNamedType defaultValue = new JNamedType(new JType(String.class), "nonModifiableVar");
	defaultValue.setModifiable(false);
	aClass.addBeanProperty(defaultValue);

	aClass.getImplementTypes().add(new JType(DataDecoder.class));
	aClass.getImplementTypes().add(new JType(Profile.class));

	aClass.getExtendTypes().add(new JType(NullWriter.class));
	aClass.getExtendTypes().add(new JType(BasicProfile.class));

	aClass.getAnnotationTypes().add(new JType(DecodeType.class));

	StringWriter aWriter = new StringWriter();

	aClass.writeClass(aWriter);
	String aS = aWriter.toString();
	assertEquals(StreamUtils.trimLines(unmodifiable_Expected), StreamUtils.trimLines(aS));
    }
    @Test
    public void test_documentation_on_class() throws IOException {
	JClass aClass = new JClass("com.acme", "AClass");
	aClass.setDocumentation("This is the documentation of AClass");
	aClass.setFluentSetters(true);

	aClass.addBeanProperty(new JNamedType(new JType(int.class), "primVar"));
	JNamedType documentedVar = new JNamedType(new JType(String.class), "documentedVar");
	documentedVar.setDocumentation("This is a documented field");
	aClass.addBeanProperty(documentedVar);

	aClass.getImplementTypes().add(new JType(DataDecoder.class));
	aClass.getImplementTypes().add(new JType(Profile.class));

	aClass.getExtendTypes().add(new JType(NullWriter.class));
	aClass.getExtendTypes().add(new JType(BasicProfile.class));

	aClass.getAnnotationTypes().add(new JType(DecodeType.class));

	StringWriter aWriter = new StringWriter();

	aClass.writeClass(aWriter);
	String aS = aWriter.toString();
	assertEquals(StreamUtils.trimLines(class_documented_Expected), StreamUtils.trimLines(aS));
    }
    @Test
    public void test_documentation_on_fields() throws IOException {
	JClass aClass = new JClass("com.acme", "AClass");

	aClass.setFluentSetters(true);

	aClass.addBeanProperty(new JNamedType(new JType(int.class), "primVar"));
	JNamedType documentedVar = new JNamedType(new JType(String.class), "documentedVar");
	documentedVar.setDocumentation("This is a documented field");
	aClass.addBeanProperty(documentedVar);

	aClass.getImplementTypes().add(new JType(DataDecoder.class));
	aClass.getImplementTypes().add(new JType(Profile.class));

	aClass.getExtendTypes().add(new JType(NullWriter.class));
	aClass.getExtendTypes().add(new JType(BasicProfile.class));

	aClass.getAnnotationTypes().add(new JType(DecodeType.class));

	StringWriter aWriter = new StringWriter();

	aClass.writeClass(aWriter);
	String aS = aWriter.toString();
	assertEquals(StreamUtils.trimLines(documented_Expected), StreamUtils.trimLines(aS));
    }

    @Test
    public void test_documentation_on_complex_type() throws IOException {
	JClass aClass = new JClass("com.acme", "AClass");
        JClass bClass = new JClass("com.acme", "BClass");

        aClass.setFluentSetters(true);
        bClass.setFluentSetters(false);

        aClass.addBeanProperty(new JNamedType(new JType(int.class), "primVar"));
        aClass.addBeanProperty(new JNamedType(new JType(Double.class), "doubleVar"));
        JNamedType documentedVar = new JNamedType(new JType(BBBClass.class), "documentedVar");
        documentedVar.setDocumentation("This is a documented var");
	aClass.addBeanProperty(documentedVar);
        JNamedType documentedListVar = new JNamedType(new JType(List.class, BBBClass.class), "documentedListVar");
        documentedListVar.setDocumentation("This is a documented list var");
	aClass.addBeanProperty(documentedListVar);

        aClass.getImplementTypes().add(new JType(DataDecoder.class));
        aClass.getImplementTypes().add(new JType(Profile.class));

        aClass.getExtendTypes().add(new JType(NullWriter.class));
        aClass.getExtendTypes().add(new JType(BasicProfile.class));

        aClass.getAnnotationTypes().add(new JType(DecodeType.class));

        // Wire AClass into BClass...
        bClass.addBeanProperty(new JNamedType(new JType(BBBClass.class), "bbbVar"));
        bClass.addBeanProperty(new JNamedType(new JType(aClass.getSkeletonClass()), "aClassVar"));

        StringWriter aWriter = new StringWriter();

        aClass.writeClass(aWriter);
        String aS = aWriter.toString();
        assertEquals(StreamUtils.trimLines(documented_on_complex_var_Expected), StreamUtils.trimLines(aS));
    }

    private static String AClass_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.DataDecoder;    \n" +
            "import org.milyn.profile.Profile;    \n" +
            "import org.milyn.io.NullWriter;    \n" +
            "import org.milyn.profile.BasicProfile;    \n" +
            "import org.milyn.javabean.DecodeType;     \n" +
            "import org.milyn.javabean.pojogen.BBBClass;    \n" +
            "import java.util.List;    \n" +
            "\n" +
            "@DecodeType" +
            "public class AClass implements DataDecoder, Profile extends NullWriter, BasicProfile {\n" +
            "\n" +
            "    private int primVar;\n" +
            "    private Double doubleVar;\n" +
            "    private BBBClass objVar;\n" +
            "    private List<BBBClass> genericVar;\n" +
            "\n" +
            "    public int getPrimVar() {\n" +
            "        return primVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setPrimVar(int primVar) {\n" +
            "        this.primVar = primVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    public Double getDoubleVar() {\n" +
            "        return doubleVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setDoubleVar(Double doubleVar) {\n" +
            "        this.doubleVar = doubleVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    public BBBClass getObjVar() {\n" +
            "        return objVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setObjVar(BBBClass objVar) {\n" +
            "        this.objVar = objVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    public List<BBBClass> getGenericVar() {\n" +
            "        return genericVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setGenericVar(List<BBBClass> genericVar) {\n" +
            "        this.genericVar = genericVar;  return this;\n" +
            "    }\n" +
            "}";

    private static String BClass_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.pojogen.BBBClass;    \n" +
            "\n" +
            "public class BClass {\n" +
            "\n" +
            "    private BBBClass bbbVar;\n" +
            "    private AClass aClassVar;\n" +
            "\n" +
            "    public BBBClass getBbbVar() {\n" +
            "        return bbbVar;\n" +
            "    }\n" +
            "\n" +
            "    public void setBbbVar(BBBClass bbbVar) {\n" +
            "        this.bbbVar = bbbVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass getAClassVar() {\n" +
            "        return aClassVar;\n" +
            "    }\n" +
            "\n" +
            "    public void setAClassVar(AClass aClassVar) {\n" +
            "        this.aClassVar = aClassVar;\n" +
            "    }\n" +
            "}";

    private static String defaultValue_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.DataDecoder;    \n" +
            "import org.milyn.profile.Profile;    \n" +
            "import org.milyn.io.NullWriter;    \n" +
            "import org.milyn.profile.BasicProfile;    \n" +
            "import org.milyn.javabean.DecodeType;     \n" +
            "\n" +
            "@DecodeType" +
            "public class AClass implements DataDecoder, Profile extends NullWriter, BasicProfile {\n" +
            "\n" +
            "    private int primVar;\n" +
            "    private String defaultValue = \"123\";\n" +
            "\n" +
            "    public int getPrimVar() {\n" +
            "        return primVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setPrimVar(int primVar) {\n" +
            "        this.primVar = primVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    public String getDefaultValue() {\n" +
            "        return defaultValue;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setDefaultValue(String defaultValue) {\n" +
            "        this.defaultValue = defaultValue;  return this;\n" +
            "    }\n" +
            "\n" +
            "}";
    private static String unmodifiable_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.DataDecoder;    \n" +
            "import org.milyn.profile.Profile;    \n" +
            "import org.milyn.io.NullWriter;    \n" +
            "import org.milyn.profile.BasicProfile;    \n" +
            "import org.milyn.javabean.DecodeType;     \n" +
            "\n" +
            "@DecodeType" +
            "public class AClass implements DataDecoder, Profile extends NullWriter, BasicProfile {\n" +
            "\n" +
            "    private int primVar;\n" +
            "    private String nonModifiableVar;\n" +
            "\n" +
            "    public int getPrimVar() {\n" +
            "        return primVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setPrimVar(int primVar) {\n" +
            "        this.primVar = primVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    public String getNonModifiableVar() {\n" +
            "        return nonModifiableVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setNonModifiableVar(String nonModifiableVar) {\n" +
            "        this.nonModifiableVar = nonModifiableVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "}";
    private static String class_documented_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.DataDecoder;    \n" +
            "import org.milyn.profile.Profile;    \n" +
            "import org.milyn.io.NullWriter;    \n" +
            "import org.milyn.profile.BasicProfile;    \n" +
            "import org.milyn.javabean.DecodeType;     \n" +
            "\n" +
            " /** This is the documentation of AClass */\n" +
            "@DecodeType" +
            "public class AClass implements DataDecoder, Profile extends NullWriter, BasicProfile {\n" +
            "\n" +
            "    private int primVar;\n" +
            "    private String documentedVar;\n" +
            "\n" +
            "    public int getPrimVar() {\n" +
            "        return primVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setPrimVar(int primVar) {\n" +
            "        this.primVar = primVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    /** {@link #setDocumentedVar(String documentedVar)} */\n"+
            "    public String getDocumentedVar() {\n" +
            "        return documentedVar;\n" +
            "    }\n" +
            "    /** This is a documented field */\n"+
            "    public AClass setDocumentedVar(String documentedVar) {\n" +
            "        this.documentedVar = documentedVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "}";
    private static String documented_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.DataDecoder;    \n" +
            "import org.milyn.profile.Profile;    \n" +
            "import org.milyn.io.NullWriter;    \n" +
            "import org.milyn.profile.BasicProfile;    \n" +
            "import org.milyn.javabean.DecodeType;     \n" +
            "\n" +
            "@DecodeType" +
            "public class AClass implements DataDecoder, Profile extends NullWriter, BasicProfile {\n" +
            "\n" +
            "    private int primVar;\n" +
            "    private String documentedVar;\n" +
            "\n" +
            "    public int getPrimVar() {\n" +
            "        return primVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setPrimVar(int primVar) {\n" +
            "        this.primVar = primVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    /** {@link #setDocumentedVar(String documentedVar)} */\n"+
            "    public String getDocumentedVar() {\n" +
            "        return documentedVar;\n" +
            "    }\n" +
            "    /** This is a documented field */\n"+
            "    public AClass setDocumentedVar(String documentedVar) {\n" +
            "        this.documentedVar = documentedVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "}";

    private static String documented_on_complex_var_Expected = "/**\n" +
            " * This class was generated by Smooks EJC (http://www.smooks.org).\n" +
            " */\n" +
            "package com.acme;\n" +
            "\n" +
            "import org.milyn.javabean.DataDecoder;    \n" +
            "import org.milyn.profile.Profile;    \n" +
            "import org.milyn.io.NullWriter;    \n" +
            "import org.milyn.profile.BasicProfile;    \n" +
            "import org.milyn.javabean.DecodeType;     \n" +
            "import org.milyn.javabean.pojogen.BBBClass;    \n" +
            "import java.util.List;    \n" +
            "\n" +
            "@DecodeType" +
            "public class AClass implements DataDecoder, Profile extends NullWriter, BasicProfile {\n" +
            "\n" +
            "    private int primVar;\n" +
            "    private Double doubleVar;\n" +
            "    private BBBClass documentedVar;\n" +
            "    private List<BBBClass> documentedListVar;\n" +
            "\n" +
            "    public int getPrimVar() {\n" +
            "        return primVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setPrimVar(int primVar) {\n" +
            "        this.primVar = primVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    public Double getDoubleVar() {\n" +
            "        return doubleVar;\n" +
            "    }\n" +
            "\n" +
            "    public AClass setDoubleVar(Double doubleVar) {\n" +
            "        this.doubleVar = doubleVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    /** {@link #setDocumentedVar(BBBClass documentedVar)} */\n"+
            "    public BBBClass getDocumentedVar() {\n" +
            "        return documentedVar;\n" +
            "    }\n" +
            "\n" +
            "    /** This is a documented var */\n"+
            "    public AClass setDocumentedVar(BBBClass documentedVar) {\n" +
            "        this.documentedVar = documentedVar;  return this;\n" +
            "    }\n" +
            "\n" +
            "    /** {@link #setDocumentedListVar(List<BBBClass> documentedListVar)} */\n"+
            "    public List<BBBClass> getDocumentedListVar() {\n" +
            "        return documentedListVar;\n" +
            "    }\n" +
            "\n" +
            "    /** This is a documented list var */\n"+
            "    public AClass setDocumentedListVar(List<BBBClass> documentedListVar) {\n" +
            "        this.documentedListVar = documentedListVar;  return this;\n" +
            "    }\n" +
            "}";
}