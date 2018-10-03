package com.epam.jdi.uitests.win.winnium.elements.complex;

import com.codeborne.selenide.Condition;
import com.epam.jdi.uitests.core.interfaces.base.IBaseElement;
import com.epam.jdi.uitests.core.interfaces.complex.IRadioButtons;
import com.epam.jdi.uitests.core.settings.JDISettings;
import com.epam.jdi.uitests.win.winnium.elements.BaseElement;
import com.epam.jdi.uitests.win.winnium.elements.ElementsUtils;
import com.epam.jdi.uitests.win.winnium.elements.common.Button;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.commons.EnumUtils.getEnumValue;
import static com.epam.jdi.uitests.core.settings.JDISettings.exception;
import static com.epam.jdi.uitests.win.winnium.driver.WebDriverByUtils.fillByTemplate;
import static com.epam.jdi.uitests.win.winnium.driver.WebDriverByUtils.getByLocator;

public class Selector<TEnum extends Enum> extends BaseElement implements IRadioButtons<TEnum> {

    @Override
    public void select(String name) {
        invoker.doJAction(String.format("Select '%s'", name), () -> selectByNameAction(name), toString());
    }

    private void selectByNameAction(String name) {
        WebElement element = getByLocator(getLocator()).contains("%s")
            ? new Button(fillByTemplate(getLocator(), name)).getWebElement()
            : getAvatar().getElements().stream().filter(el -> el.getAttribute("Name").equals(name)).findFirst().
                    orElseThrow(() -> exception("Can't find option '%s'.", name));
        element.click();
    }

    @Override
    public void select(TEnum name) {
        invoker.doJAction(String.format("Select '%s'", getEnumValue(name)), () -> selectByNameAction(getEnumValue(name)), toString());
    }

    @Override
    public void select(int index) {
        invoker.doJAction(String.format("Select '%d' button", index), () -> selectByIndexAction(index), toString());
    }

    private void selectByIndexAction(int index) {
        List<WebElement> elements = getAvatar().getElements().stream().collect(Collectors.toList());

        if (index <= 0)
            throw exception("Can't get option with num '%s'. Number should be 1 or more", index);
        if (elements.size() < index)
            throw exception("Can't find option with num '%s'. Find only '%s' options", index, elements.size());

        elements.get(index - 1).click();
    }

    @Override
    public String getSelected() {
        WebElement selectedElement =
                getAvatar().getElements().stream().filter(WebElement::isSelected).findFirst().
                        orElseThrow(() -> JDISettings.exception("Can't find selected element"));

        return selectedElement.getAttribute("Name");
    }

    @Override
    public int getSelectedIndex() {
        Integer i = getIndex(getSelected());
        if (i == null)
            throw JDISettings.exception("Can't find selected index");

        return i;
    }

    private Integer getIndex(String str) {
        List<String> options = getOptions();
        for (int i = 0; i < options.size(); i++)
            if (options.get(i).equals(str))
                return i + 1;

        return null;
    }

    @Override
    public boolean isSelected(String name) {
        return name.equals(getSelected());
    }

    @Override
    public boolean isSelected(TEnum name) {
        return getEnumValue(name).equals(getSelected());
    }

    @Override
    public void waitSelected(String name) {
        ElementsUtils.waitByCondition(this::getSelected, name::equals, "Radiobutton %s wasn't selected", name);
    }

    @Override
    public void waitSelected(TEnum name) {
        ElementsUtils.waitByCondition(this::getSelected, s -> getEnumValue(name).equals(s),
                "Radiobutton %s wasn't selected", name);
    }

    @Override
    public List<String> getOptions() {
        List<WebElement> elements = getAvatar().getElements();

        return elements.stream().map(el -> el.getAttribute("Name")).collect(Collectors.toList());
    }

    @Override
    public void setValue(String value) {
        Integer index = getIndex(value);
        if (index != null)
            select(index);
    }

    @Override
    public String getValue() {
        return getSelected();
    }

    @Override
    public IBaseElement should(Condition... condition) {
        return null;
    }

    @Override
    public IBaseElement shouldHave(Condition... condition) {
        return null;
    }

    @Override
    public IBaseElement shouldBe(Condition... condition) {
        return null;
    }

    @Override
    public IBaseElement shouldNot(Condition... condition) {
        return null;
    }

    @Override
    public IBaseElement shouldNotHave(Condition... condition) {
        return null;
    }

    @Override
    public IBaseElement shouldNotBe(Condition... condition) {
        return null;
    }
}
