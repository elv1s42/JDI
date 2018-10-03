package com.epam.jdi.uitests.win.winnium.elements.complex.table;

import com.epam.commons.linqinterfaces.JFuncTTREx;
import com.epam.commons.map.MapArray;
import com.epam.jdi.uitests.core.interfaces.common.IText;
import com.epam.jdi.uitests.core.interfaces.complex.tables.interfaces.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.List;

import static com.epam.commons.LinqUtils.select;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.win.winnium.elements.ElementsUtils.timer;

public class Rows extends TableLine implements IRow {
    public Rows() {
        hasHeader = false;
        elementIndex = ElementIndexType.Nums;
        headersLocator = By.xpath(".//tr/td[1]");
        defaultTemplate = By.xpath(".//tr[%s]/td");
    }

    protected List<WebElement> getHeadersAction() {
        return table.getWebElement().findElements(headersLocator);
    }

    protected List<WebElement> getCrossFirstLine() {
        return ((Columns)table.columns()).getLineAction(1);
    }

    public MapArray<String, MapArray<String, ICell>> get() {
        return new MapArray<>(headers(), key -> key, this::getRow);
    }

    ///

    public List<String> getRowValue(String rowName) {
        try {
            return select(getLineAction(rowName), WebElement::getText);
        } catch (Exception | Error ex) {
            throw throwRowsException(rowName, ex.getMessage());
        }
    }

    public final MapArray<String, String> getRowAsText(String rowName) {
        return getRow(rowName).toMapArray(IText::getText);
    }

    public MapArray<String, ICell> cellsToRow(Collection<ICell> cells) {
        return new MapArray<>(cells,
                cell -> headers().get(cell.columnNum() - 1),
                cell -> cell);
    }

    public MapArray<String, ICell> getRow(int rowNum) {
        if (rowNum <= 0)
            throw exception("Table indexes starts from 1");
        if (count() < 0 || count() < rowNum || rowNum <= 0)
            throw exception("Can't Get Row '%s'. [num] > ColumnsCount(%s).", rowNum, count());
        try {
            int colsCount = table.columns().count();
            List<WebElement> webRow = timer().getResultByCondition(
                    () -> getLineAction(rowNum), els -> els.size() == colsCount);
            return new MapArray<>(colsCount,
                    key -> table.columns().headers().get(key),
                    value -> table.cell(webRow.get(value), new Column(value + 1), new Row(rowNum)));
        } catch (Exception | Error ex) {
            throw throwRowsException(Integer.toString(rowNum) + "", ex.getMessage());
        }
    }

    public List<String> getRowValue(int rowNum) {
        if (rowNum <= 0)
            throw exception("Table indexes starts from 1");
        if (count() < 0 || count() < rowNum || rowNum <= 0)
            throw exception("Can't Get Row '%s'. [num] > ColumnsCount(%s).", rowNum, count());
        try {
            return select(getLineAction(rowNum), WebElement::getText);
        } catch (Exception | Error ex) {
            throw throwRowsException(Integer.toString(rowNum), ex.getMessage());
        }
    }

    public final MapArray<String, String> getRowAsText(int rowNum) {
        return getRow(rowNum).toMapArray(IText::getText);
    }

    private MapArray<String, MapArray<String, ICell>> withValueByRule(
            Column column, JFuncTTREx<String, String, Boolean> func) {
        Collection<String> rowNames = column.hasName()
                ? table.columns().getColumnAsText(column.getName()).where(func).keys()
                : table.columns().getColumnAsText(column.getNum()).where(func).keys();
        return new MapArray<>(rowNames, key -> key, this::getRow);
    }
    public final MapArray<String, MapArray<String, ICell>> withValue(String value, Column column) {
        return withValueByRule(column, (key, val) -> val.equals(value));
    }
    public final MapArray<String, MapArray<String, ICell>> containsValue(String value, Column column) {
        return withValueByRule(column, (key, val) -> val.contains(value));
    }
    public final MapArray<String, MapArray<String, ICell>> matchesRegEx(String regEx, Column column) {
        return withValueByRule(column, (key, val) -> val.matches(regEx));
    }

    public final MapArray<String, ICell> getRow(String rowName) {
        try {
            int colsCount = table.columns().count();
            List<String> headers = table.columns().headers();
            List<WebElement> webRowLine = timer().getResultByCondition(
                    () -> getLineAction(rowName), els -> els.size() == colsCount);
            return new MapArray<>(colsCount,
                    headers::get,
                    value -> table.cell(webRowLine.get(value), new Column(headers.get(value)), new Row(rowName)));
        } catch (Exception | Error ex) {
            throw throwRowsException(rowName, ex.getMessage());
        }
    }

    protected boolean skipFirstColumn() {
        return hasHeader && lineTemplate == null;
    }

    private RuntimeException throwRowsException(String rowName, String ex) {
        return exception("Can't Get Row '%s'. Reason: %s", rowName, ex);
    }
}
