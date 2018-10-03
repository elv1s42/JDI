package com.epam.jdi.uitests.web.selenium.elements.composite;
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
import com.epam.jdi.uitests.core.annotations.AnnotationsUtil;
import com.epam.jdi.uitests.core.interfaces.base.IClickable;
import com.epam.jdi.uitests.core.interfaces.complex.IPagination;
import com.epam.jdi.uitests.web.selenium.elements.GetElementType;
import com.epam.jdi.uitests.web.selenium.elements.base.BaseElement;
import com.epam.jdi.uitests.web.selenium.elements.base.Clickable;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

import static com.epam.commons.ReflectionUtils.getFields;
import static com.epam.commons.ReflectionUtils.getValueField;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.web.selenium.driver.WebDriverByUtils.fillByTemplate;
import static java.lang.String.format;

/**
 * Created by Roman_Iovlev on 7/29/2015.
 */

/**
 * @deprecated
 *
 * Should be replaced by com.epam.jdi.uitests.web.selenium.elements.complex.Pagination;
 * {@see} Example of usage in the @JPagination
 *
 */
@Deprecated
public class Pagination extends BaseElement implements IPagination {
    private By nextLocator;
    private By previousLocator;
    private By firstLocator;
    private By lastLocator;

    public Pagination() {
        super();
    }

    public Pagination(By byLocator) {
        super(byLocator);
    }

    public Pagination(By nextLocator, By previousLocator) {
        this(null, nextLocator, previousLocator, null, null);
    }

    public Pagination(By pageTemplateLocator, By nextLocator, By previousLocator) {
        this(pageTemplateLocator, nextLocator, previousLocator, null, null);
    }

    public Pagination(By pageTemplateLocator, By nextLocator, By previousLocator,
                      By firstLocator, By lastLocator) {
        super(pageTemplateLocator);
        this.nextLocator = nextLocator;
        this.previousLocator = previousLocator;
        this.firstLocator = firstLocator;
        this.lastLocator = lastLocator;
    }

    /**
     * Choose Next page
     */
    public void next() {
        invoker.doJAction("Choose Next page", () -> nextAction().click());
    }

    /**
     * Choose Previous page
     */
    public void previous() {
        invoker.doJAction("Choose Previous page", () -> previousAction().click());
    }

    /**
     * hoose First page
     */
    public void first() {
        invoker.doJAction("Choose First page", () -> firstAction().click());
    }

    /**
     * Choose Last page
     */
    public void last() {
        invoker.doJAction("Choose Last page", () -> lastAction().click());
    }

    /**
     * @param index Specify page index
     *              Choose page by index
     */
    public void selectPage(int index) {
        invoker.doJAction(format("Choose '%s' page", index), () -> pageAction(index).click());
    }

    private Clickable getClickable(String name) {
        List<Field> fields = getFields(this, IClickable.class);
        Field result = LinqUtils.first(fields,
                field -> AnnotationsUtil.getElementName(field).toLowerCase().contains(name.toLowerCase()));
        return (Clickable) getValueField(result, this);
    }

    private String cantChooseElementMsg(String actionName, String shortName, String action) {
        return MessageFormat.format("Can't choose {0} page for Element '{3}'. "
                + "Please specify locator for this action using constructor or add Clickable Element "
                + "on pageObject with name '{1}Link' or '{1}Button' or use locator template with parameter '{1}'"
                + "or override {2}() in class", actionName, shortName, action, toString());
    }

    protected Clickable nextAction() {
        String shortName = "next";
        if (nextLocator != null)
            return new GetElementType(nextLocator, this).get(Clickable.class);

        Clickable nextLink = getClickable(shortName);
        if (nextLink != null)
            return nextLink;

        if (getLocator() != null && getLocator().toString().contains("%s"))
            return new GetElementType(fillByTemplate(getLocator(), shortName), this).get(Clickable.class);
        throw exception(cantChooseElementMsg("Next", shortName, "nextAction"));
    }

    private Clickable previousAction() {
        String shortName = "prev";
        if (previousLocator != null)
            return new GetElementType(previousLocator, this).get(Clickable.class);

        Clickable prevLink = getClickable(shortName);
        if (prevLink != null)
            return prevLink;

        if (getLocator() != null && getLocator().toString().contains("%s"))
            return new GetElementType(fillByTemplate(getLocator(), shortName), this).get(Clickable.class);
        throw exception(cantChooseElementMsg("Previous", shortName, "previousAction"));
    }

    private Clickable firstAction() {
        String shortName = "first";
        if (firstLocator != null)
            return new GetElementType(firstLocator, this).get(Clickable.class);

        Clickable firstLink = getClickable(shortName);
        if (firstLink != null)
            return firstLink;

        if (getLocator() != null && getLocator().toString().contains("%s"))
            return new GetElementType(fillByTemplate(getLocator(), shortName), this).get(Clickable.class);
        throw exception(cantChooseElementMsg("First", shortName, "firstAction"));
    }

    private Clickable lastAction() {
        String shortName = "last";
        if (lastLocator != null)
            return new GetElementType(lastLocator, this).get(Clickable.class);

        Clickable lastLink = getClickable(shortName);
        if (lastLink != null)
            return lastLink;

        if (getLocator() != null && getLocator().toString().contains("%s"))
            return new GetElementType(fillByTemplate(getLocator(), shortName), this).get(Clickable.class);
        throw exception(cantChooseElementMsg("Last", shortName, "lastAction"));
    }

    private Clickable pageAction(int index) {
        String shortName = "page";
        if (getLocator() != null && getLocator().toString().contains("%s"))
            return new GetElementType(fillByTemplate(getLocator(), index), this).get(Clickable.class);

        Clickable pageLink = getClickable(shortName);
        if (pageLink != null)
            return pageLink;
        throw exception(cantChooseElementMsg(Integer.toString(index), shortName, "pageAction"));
    }

    public boolean isDisplayed() {
        return false;
    }

    public boolean isHidden() {
        return false;
    }

    public void waitDisplayed() {

    }

    public void waitVanished() {

    }
}