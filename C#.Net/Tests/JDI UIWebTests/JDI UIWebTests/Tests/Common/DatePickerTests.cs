﻿using NUnit.Framework;
using static Epam.JDI.Core.Settings.JDISettings;
using static JDI_UIWebTests.UIObjects.TestSite;
using Epam.JDI.Core.Interfaces.Common;

namespace JDI_UIWebTests.Tests.Common
{
    public class DatePickerTests
    {
        private ITextField _datePicker = Dates.Datepicker;
        private const string DEFAULT_DATE = "09/09/1945";
        private const string CHECK_DATE = "09/05/1945";
        private const string PARTIAL_TEXT_FIRST = "09/";
        private const string PARTIAL_TEXT_LAST = "09/1945";

        [SetUp]
        public void SetUp()
        {
            Logger.Info("Navigating to Metals and Colors page.");
            Dates.Open();
            Dates.CheckTitle();
            Dates.IsOpened();
            Dates.Datepicker.Clear();            
            Logger.Info("Setup method finished");
            Logger.Info("Start test: " + TestContext.CurrentContext.Test.Name);

        }

        [Test]
        public void InputDatePickerTest()
        {
            _datePicker.Input(DEFAULT_DATE);
            Assert.True(_datePicker.GetText.Equals(DEFAULT_DATE));
        }

        [Test]
        public void SendKeysDatePickerTest()
        {
            _datePicker.SendKeys(DEFAULT_DATE);
            Assert.True(_datePicker.GetText.Equals(DEFAULT_DATE));
        }

        [Test]
        public void NewInputDatePickerTest()
        {
            _datePicker.SendKeys(CHECK_DATE);
            _datePicker.NewInput(DEFAULT_DATE);
            Assert.True(_datePicker.GetText.Equals(DEFAULT_DATE));
        }

        [Test]
        public void ClearTest()
        {
            _datePicker.SendKeys(DEFAULT_DATE);
            _datePicker.Clear();
            Assert.True(_datePicker.GetText.Equals(""));
        }

        [Test]
        public void MultiKeyTest()
        {
            foreach (char ch in DEFAULT_DATE.ToCharArray()) 
            {
                _datePicker.SendKeys(ch.ToString());
            }
            Assert.True(_datePicker.GetText.Equals(DEFAULT_DATE));
        }

        //TO_DO
        /*
        [Test]
        public void ElementFocusTest()
        {

        }
        */
    }
}
