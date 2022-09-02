package com.example.calculator.test

object CalculatorValidator {

    fun additionTest(number1: Int, number2: Int): Int {
        return number1 + number2
    }

    fun getResult(text: StringBuilder): String {
        val operands = ArrayList<String>()
        var operators = ""
        var value = ""
        var count = 0
        text.forEach {
            if (it in '0'..'9' || it == '.') {
                value += it
            } else {
                operands.add(value)
                value = ""
                operators += it
                count++
            }
        }
        operands.add(value)
        value = ""

        while (operators.isNotEmpty()) {
            if (operators.contains("*")) {
                val indexOfOperator = operators.indexOf("*")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(text,operands, indexOfOperator, '*')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            } else if (operators.contains("+")) {
                val indexOfOperator = operators.indexOf("+")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(text,operands, indexOfOperator, '+')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            } else if (operators.contains("/")) {
                val indexOfOperator = operators.indexOf("/")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(text,operands, indexOfOperator, '/')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            } else if (operators.contains("-")) {
                val indexOfOperator = operators.indexOf("-")
                reSetOperandArray(
                    operands,
                    indexOfOperator,
                    operation(text,operands, indexOfOperator, '-')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            }
        }
        return operands[0]
    }
    private fun reSetOperandArray(array: ArrayList<String>, index: Int, operationResult: String) {
        array[index] = operationResult
        if (array.size > index + 1) {
            for (i in index + 1 until array.size - 1) {
                array[i] = array[i + 1]
            }
            array[array.size - 1] = "0"
        }
    }
    private fun operation(text: StringBuilder, operands: ArrayList<String>, index: Int, operation: Char): String {
        val firstOperand = operands[index].toDouble()
        val secondOperand = operands[index + 1].toDouble()
        var result = 0.0
        when (operation) {
            '*' -> {
                result = firstOperand * secondOperand
            }
            '+' -> {
                result = firstOperand + secondOperand
            }
            '/' -> {
                if (secondOperand == 0.0) {
                    return ""
                } else
                    result = firstOperand / secondOperand
            }
            '-' -> {
                result = firstOperand - secondOperand
            }
        }
        return if (result % 1 == 0.0) {
            text.clear()
            text.append(result.toInt())
            result.toInt().toString()
        } else {
            text.clear()
            text.append(result)
            result.toString()
        }
    }

}