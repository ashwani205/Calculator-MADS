package com.example.calculator.test

import org.junit.Assert
import org.junit.Test

internal class CalculatorValidatorTest {

    @Test
    fun additionTest() {
        val number1 = 2
        val number2 = 3
        val expectedResult = number1 + number2
        val result = CalculatorValidator.additionTest(number1, number2)
        Assert.assertEquals(expectedResult, result)
//        assertThat(result).isEqualTo(true)
    }

    @Test
    fun firstResultTest() {
        val exp = StringBuilder("56+92-23")
        val expResult = "125"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)

    }

    @Test
    fun firstResultFailureTest() {
        val exp = StringBuilder("56+92-23")
        val expResult = "124"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun secondResultTest() {
        val exp = StringBuilder("50+20/10")
        val expResult = "7"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun secondResultFailureTest() {
        val exp = StringBuilder("50+20/10")
        val expResult = "17"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun thirdResultTest() {
        val exp = StringBuilder("50/20+5")
        val expResult = "2"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun thirdResultFailureTest() {
        val exp = StringBuilder("50/20+5")
        val expResult = "12"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun fourthResultTest() {
        val exp = StringBuilder("25-2*10")
        val expResult = "5"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun fourthResultFailureTest() {
        val exp = StringBuilder("25-2*10")
        val expResult = "15"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun fifthResultTest() {
        val exp = StringBuilder("10/2-20")
        val expResult = "-15"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun fifthResultFailureTest() {
        val exp = StringBuilder("10/2-20")
        val expResult = "-5"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun sixthResultTest() {
        val exp = StringBuilder("10-2-3")
        val expResult = "5"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun sixthResultFailureTest() {
        val exp = StringBuilder("10-2-3")
        val expResult = "15"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun seventhResultTest() {
        val exp = StringBuilder("10/2/5")
        val expResult = "1"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun seventhResultFailureTest() {
        val exp = StringBuilder("10/2/5")
        val expResult = "11"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }

    @Test
    fun eightResultTest() {
        val exp = StringBuilder("10/2/4+1")
        val expResult = "1"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertEquals(result, expResult)
    }

    @Test
    fun eightResultFailureTest() {
        val exp = StringBuilder("10/2/4+1")
        val expResult = "11"
        val result = CalculatorValidator.getResult(exp)
        Assert.assertNotEquals(result, expResult)
    }


}