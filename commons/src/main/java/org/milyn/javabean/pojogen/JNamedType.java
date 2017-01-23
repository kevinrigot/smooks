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

import org.milyn.assertion.AssertArgument;

/**
 * Named type for properties and method parameters.
 * @author bardl
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class JNamedType {

    private JType type;
    private String name;
    private String defaultValue;
    private boolean modifiable = true;

    public JNamedType(JType type, String name) {
        AssertArgument.isNotNull(type, "type");
        AssertArgument.isNotNull(name, "name");
        this.type = type;
        this.name = name;
    }

    public JNamedType(JType type, String name, String defaultValue, boolean modifiable) {
        AssertArgument.isNotNull(type, "type");
        AssertArgument.isNotNull(name, "name");
        this.type = type;
        this.name = name;
        this.defaultValue = defaultValue;
        this.modifiable = modifiable;
    }

    public JType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    @Override
    public String toString() {
	return type.toString() + " " + name;
    }
    public String getDefaultValueToString() {
        return (defaultValue!=null) ? " = "+defaultValue : "";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JNamedType) {
            JNamedType namedTypeObj = (JNamedType) obj;

            if(namedTypeObj.getName().equals(name) && namedTypeObj.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (name.hashCode() + type.hashCode());
    }
}