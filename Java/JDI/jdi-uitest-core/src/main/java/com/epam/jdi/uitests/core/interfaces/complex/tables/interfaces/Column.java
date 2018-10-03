package com.epam.jdi.uitests.core.interfaces.complex.tables.interfaces;
/*
 * Copyright 2004-2016 EPAM Systems
 *
 * This file is part of JDI project.
 *
 * JDI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JDI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDI. If not, see <http://www.gnu.org/licenses/>.
 */


import static com.epam.commons.EnumUtils.getEnumValue;

/**
 * Created by Roman_Iovlev on 7/17/2015.
 */
public class Column extends RowColumn {
    public Column(int num) {
        super(num);
    }

    public Column(String name) {
        super(name);
    }

    public static Column column(int num) {
        return new Column(num);
    }

    public static Column column(String name) {
        return new Column(name);
    }
    public static <TEnum extends Enum> Column column(TEnum name) {
        return column(getEnumValue(name));
    }
    public static Column inColumn(int num) {
        return new Column(num);
    }

    public static Column inColumn(String name) {
        return new Column(name);
    }

    public static <TEnum extends Enum> Column inColumn(TEnum name) {
        return inColumn(getEnumValue(name));
    }
}