package com.example.calculator.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.model.History
import com.google.android.material.snackbar.Snackbar
/*AIzaSyDfyWTl0Xryj1Elw8pL6zumzTfRJFcShOk*/
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private var text: StringBuilder = StringBuilder()
    private var history: ArrayList<History> = ArrayList()
    private var saveAtIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()
        mBinding.b1.setOnClickListener {
            setPrimaryText("1")
        }
        mBinding.b2.setOnClickListener {
            setPrimaryText("2")
        }
        mBinding.b3.setOnClickListener {
            setPrimaryText("3")
        }
        mBinding.b4.setOnClickListener {
            setPrimaryText("4")
        }
        mBinding.b5.setOnClickListener {
            setPrimaryText("5")
        }
        mBinding.b6.setOnClickListener {
            setPrimaryText("6")
        }
        mBinding.b7.setOnClickListener {
            setPrimaryText("7")
        }
        mBinding.b8.setOnClickListener {
            setPrimaryText("8")
        }
        mBinding.b9.setOnClickListener {
            setPrimaryText("9")
        }
        mBinding.b0.setOnClickListener {
            setPrimaryText("0")
        }
        mBinding.b00.setOnClickListener {
            setPrimaryText("00")
        }
        mBinding.bClear.setOnClickListener {
            text.clear()
            mBinding.idTVPrimary.text = ""
        }
        mBinding.bHistory.setOnClickListener {
            val fragment = HistoryFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("history", history)
            fragment.arguments = bundle
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.main_container, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        mBinding.bBack.setOnClickListener {
            val value = mBinding.idTVPrimary.text.toString()
            if (text.isNotEmpty()) {
                mBinding.idTVPrimary.text = value.substring(0, value.length - 1)
                text.deleteCharAt(value.length - 1)
            }
        }

        mBinding.bMul.setOnClickListener {
            addOperator('*')
        }
        mBinding.bPlus.setOnClickListener {
            addOperator('+')
        }
        mBinding.bDiv.setOnClickListener {
            addOperator('/')
        }
        mBinding.bMinus.setOnClickListener {
            addOperator('-')
        }
        mBinding.bEqual.setOnClickListener {
            if (text.isNotEmpty()) {
                val lastChar = text[text.length - 1]
                if (lastChar != '*' && lastChar != '+' && lastChar != '/' && lastChar != '-') {
                    mBinding.idTVSecondary.text = getResult()
                }
            }
        }
    }


    private fun getResult(): String {
        val operands = ArrayList<String>()
        var operators = ""
        var value = ""
        var count = 0
        if (text[0] == '-') {
            value = "-"
            text = text.deleteCharAt(0)
        }
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
                    operation(operands, indexOfOperator, '*')
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
                    operation(operands, indexOfOperator, '+')
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
                    operation(operands, indexOfOperator, '/')
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
                    operation(operands, indexOfOperator, '-')
                )
                operators = operators.substring(
                    0,
                    indexOfOperator
                ) + operators.substring(indexOfOperator + 1)
            }
        }
        if (history.size < 10)
            history.add(History(input = mBinding.idTVPrimary.text.toString(), result = operands[0]))
        else {
            history[saveAtIndex] =
                History(input = mBinding.idTVPrimary.text.toString(), result = operands[0])
            saveAtIndex++
            if (saveAtIndex >= 10)
                saveAtIndex = 0
        }
        return operands[0]
    }

    private fun addOperator(operator: Char) {
        if (text.isNotEmpty()) {
            val lastChar = text[text.length - 1]
            if (lastChar != operator && lastChar !in '0'..'9') {
                text.setCharAt(text.length - 1, operator)
                mBinding.idTVPrimary.text = text
            } else if (lastChar != operator) {
                setPrimaryText("$operator")
            }
        }
    }

    private fun setPrimaryText(action: String) {
        text = text.append(action)
        mBinding.idTVPrimary.text = text
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

    private fun operation(operands: ArrayList<String>, index: Int, operation: Char): String {
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
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.divide_by_zero),
                        Snackbar.LENGTH_SHORT
                    ).show()
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