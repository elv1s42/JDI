package com.epam.jdi.uitests.mobile.appium.elements.complex.table;

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

import com.epam.commons.LinqUtils;
import com.epam.commons.ReflectionUtils;
import com.epam.commons.linqinterfaces.JFuncTREx;
import com.epam.commons.map.MapArray;
import com.epam.jdi.uitests.core.interfaces.base.IBaseElement;
import com.epam.jdi.uitests.core.interfaces.complex.tables.interfaces.Column;
import com.epam.jdi.uitests.core.interfaces.complex.tables.interfaces.ICell;
import com.epam.jdi.uitests.core.interfaces.complex.tables.interfaces.IEntityTable;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.epam.commons.LinqUtils.select;
import static com.epam.commons.LinqUtils.where;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.core.settings.JDISettings.logger;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;

/**
 * Created by Sergey_Mishanin on 11/18/16.
 */
public class EntityTable<E, R> extends Table implements IEntityTable<E,R> {
    private Class<E> entityClass;
    private Class<R> rowClass;

    public EntityTable(Class<E> entityClass){
        if (entityClass == null){
            throw new IllegalArgumentException("Entity type was not specified");
        }

        this.entityClass = entityClass;
        List<String> headers = Arrays.stream(entityClass.getFields())
                .map(Field::getName).collect(Collectors.toList());
        hasColumnHeaders(headers);
    }

    public EntityTable(Class<E> entityClass, Class<R> rowClass){
        this(entityClass);
        this.rowClass = rowClass;
    }

    private R newRow(){
        if (rowClass == null){
            throw new UnsupportedOperationException("Row class was not specified");
        }
        try {
            return rowClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private R castToRow(MapArray<String, ICell> row)
    {
        R newRow = newRow();
        row.pairs.forEach(pair ->
                setRowField(newRow, newRow.getClass().getFields(), pair.key, pair.value));
        return newRow;
    }

    private void setRowField(R row, Field[] fields, String fieldName, ICell cell)
    {
        setField(row, fields, fieldName, field -> {
            Class clazz = field.getType();
            if (clazz == null) return null;
            IBaseElement value;
            try {
                value = cell.get(clazz);
            } catch (Exception e) {
                throw exception("Can't Instantiate row element: " + fieldName);
            }
            return value;
        });
    }

    private void setField(Object row, Field[] fields, String fieldName,
                          Function<Field, Object> valueFunc)
    {
        Field field = LinqUtils.first(fields, f -> f.getName().equalsIgnoreCase(fieldName));

        if (field == null) return;
        try {
            writeField(field, row, valueFunc.apply(field), true);
        } catch (IllegalAccessException e) {
            throw exception("Can't write field with name: " + fieldName);
        }
    }
    public List<R> getRows() {
        return select(rows().get(), row -> castToRow(row.value));
    }

    public R firstRow(JFuncTREx<R, Boolean> colNames) {
        return getRows(colNames).get(0);
    }

    public List<R> getRows(JFuncTREx<R, Boolean> colNames)
    {
        return where(getRows(), colNames);
    }

    public R getRow(String value, Column column)
    {
        return castToRow(super.row(value, column));
    }

    public R getRow(int rowNum)
    {
        return castToRow(super.row(rowNum));
    }

    public R getRow(String rowName)
    {
        return castToRow(super.row(rowName));
    }

    private E newEntity(){
        try {
            return entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private E rowToEntity(MapArray<String, ICell> row) {
        E entity = newEntity();
        if (row == null){
            return entity;
        }

        Field[] fields = entity.getClass().getFields();

        row.pairs
            .forEach(entry -> setEntityField(entity, fields, entry.key, entry.value.getText()));

        return entity;
    }

    public List<E> entities(String... colNames){
        List<E> entities = new ArrayList<>();
        for (int i=1; i<=size(); i++){
            MapArray<String, ICell> row = new MapArray<>();
            for (String colName: colNames){
                row.add(columns.getColumn(colName).get(i));
            }
            entities.add(rowToEntity(row));
        }
        return entities;
    }


    public List<R> getRows(String... colNames) {
        return select(colNames, colName
                -> castToRow(columns.getColumn(colName)));
    }
    public List<E> entities(JFuncTREx<E, Boolean> rule) {
        List<E> entities = where(entities(), rule);
        if (rows.size() == 0)
            logger.info("Can't find any rows that meat criterias");
        return entities;
    }

    public E entity(JFuncTREx<E, Boolean> rule) {
        List<E> rows = entities(rule);
        return rows.size() > 0
                ? rows.get(0)
                : null;
    }
    public E entity(int rowNum){
        return rowToEntity(rows.getRow(rowNum));
    }

    public E entity(String value, Column column){
        return rowToEntity(row(value, column));
    }

    public E entity(String rowName){
        return rowToEntity(rows.getRow(rowName));
    }

    public List<E> all(){
        return rows.get().values().stream()
                .map(this::rowToEntity).collect(Collectors.toList());
    }

    private void setEntityField(E entity, Field[] fields, String fieldName, String value)
    {
        Optional<Field> opt = Arrays.stream(fields)
                .filter(f -> f.getName().equalsIgnoreCase(fieldName)).findFirst();

        if (!opt.isPresent()){
            return;
        }

        Field field = opt.get();

        try {
            FieldUtils.writeField(field, entity, ReflectionUtils.convertStringToType(value, field), true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int size() {
        return rows.count();
    }

    public boolean contains(Object o) {
        return allCells.contains(o);
    }

    public Iterator<E> iterator() {
        return all().iterator();
    }

    public Object[] toArray() {
        return all().toArray();
    }

    public <T1> T1[] toArray(T1[] a) {
        return all().toArray(a);
    }

    public boolean add(E t) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        return all().containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public E get(int index) {
        return entity(index);
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object o) {
        return all().indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return all().lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return all().listIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return all().listIterator(index);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return all().subList(fromIndex, toIndex);
    }
}
