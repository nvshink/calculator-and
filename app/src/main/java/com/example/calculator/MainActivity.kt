package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import java.math.RoundingMode
import android.content.Intent
import android.os.Handler


class MainActivity : AppCompatActivity() {
    var number: String = ""
    var isNotPoint: Boolean = true
    var isLastOperator: Boolean = true
    var isNotFirstZero: Boolean = true
    var isError: Boolean = false
    var expressionValueString: MutableList<String> = mutableListOf()
    var expressionValuePriority: MutableList<Byte> = mutableListOf()
    lateinit var outText: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_Calculator)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = ""
        supportActionBar!!.setElevation(0F)
        outText = findViewById(R.id.outText)
        outText.setShowSoftInputOnFocus(false)
    }




    fun setOutText(str: String, b: Byte) {
        if (isError) {
            outText.setText("")
            isError = false
        }
        if (b == 0.toByte()) {
            number += str
            outText.setText(outText.text.toString() + str)
        } else {
            expressionValueAdd(number, 0)
            number = ""
            expressionValueAdd(str, b)
            outText.setText(outText.text.toString() + " " + str + " ")
        }
        outText.text?.let { outText.setSelection(it.length) }
    }

    fun onNumber(v: View) {
        if (isNotFirstZero) {
            when (v.id) {
                R.id.zeroButton -> {
                    setOutText("0", 0)
                    if (isNotPoint && isLastOperator) isNotFirstZero = false
                }
                R.id.oneButton -> setOutText("1", 0)
                R.id.twoButton -> setOutText("2", 0)
                R.id.threeButton -> setOutText("3", 0)
                R.id.fourButton -> setOutText("4", 0)
                R.id.fiveButton -> setOutText("5", 0)
                R.id.sixButton -> setOutText("6", 0)
                R.id.sevenButton -> setOutText("7", 0)
                R.id.eightButton -> setOutText("8", 0)
                R.id.nineButton -> setOutText("9", 0)
            }
            isLastOperator = false
        }

    }

    fun onPoint(v: View) {
        if (isNotPoint && !isLastOperator) {
            setOutText(".", 0)
            isNotPoint = false
            isNotFirstZero = true
        }
    }

    fun onOperator(v: View) {
        if (!isLastOperator) {
            when (v.id) {
                R.id.plusButton -> setOutText("+", 1)
                R.id.minusButton -> setOutText("-", 1)
                R.id.multiplicationButton -> setOutText("×", 2)
                R.id.divideButton -> setOutText("÷", 2)
            }
            isNotPoint = true
            isNotFirstZero = true
            isLastOperator = true
        }
    }

    fun onDelete(v: View) {
        if (number.isNotEmpty() || expressionValueString.isNotEmpty()) {
            if (!isLastOperator) {
                    if (number.length == 1) {
                        isLastOperator = true
                        isNotFirstZero = true
                        isNotPoint = true
                    } else  if (number.last() == '.') {
                        isNotPoint = true
                        if (number.first() == '0') isNotFirstZero = false
                    }
                number = number.dropLast(1)
                outText.setText(outText.text.toString().dropLast(1))
            } else {
                try {
                    expressionValueRemove(expressionValueString.lastIndex)
                    isNotFirstZero = expressionValueString.last().first() != '0'
                    isNotPoint = !expressionValueString.last().contains('.', false)
                    isLastOperator = false
                    outText.setText(outText.text.toString().dropLast(3))
                    number = expressionValueString.last()
                    expressionValueRemove(expressionValueString.lastIndex)
                } catch (e: Exception) {
                    outText.setText(R.string.error_message)
                }
            }
        }
        outText.text?.let { outText.setSelection(it.length) }
    }

    fun onClear(v: View) {
        expressionValueClear()
        outText.setText("")
        number = ""
        isNotPoint = true
        isNotFirstZero = true
        isLastOperator = true
        isError = false
    }

    fun onResult(v: View) {
        var i: Int = 0
        expressionValueAdd(number, 0)
        try {
            while (expressionValuePriority.maxOrNull() != 0.toByte()) {
                when (expressionValueString.get(i)) {
                    "+" -> {
                        expressionValueString.set(
                            i,
                            expressionValueString.get(i - 1).toBigDecimal()
                                .plus(expressionValueString.get(i + 1).toBigDecimal())
                                .toString()
                        )
                        expressionValuePriority.set(i, 0)
                        expressionValueRemoveRight(i)
                        expressionValueRemoveLeft(i)
                        i--
                    }
                    "-" -> {
                        expressionValueString.set(
                            i,
                            expressionValueString.get(i - 1).toBigDecimal()
                                .minus(expressionValueString.get(i + 1).toBigDecimal())
                                .toString()
                        )
                        expressionValuePriority.set(i, 0)
                        expressionValueRemoveRight(i)
                        expressionValueRemoveLeft(i)
                        i--
                    }
                    "×" -> {
                        expressionValueString.set(
                            i,
                            expressionValueString.get(i - 1).toBigDecimal()
                                .multiply(expressionValueString.get(i + 1).toBigDecimal())
                                .toString()
                        )
                        expressionValuePriority.set(i, 0)
                        expressionValueRemoveRight(i)
                        expressionValueRemoveLeft(i)
                        i--
                    }
                    "÷" -> {
                        expressionValueString.set(
                            i,
                            expressionValueString.get(i - 1).toBigDecimal().divide(
                                expressionValueString.get(i + 1).toBigDecimal(),
                                10,
                                RoundingMode.HALF_UP
                            ).toString()
                        )
                        expressionValuePriority.set(i, 0)
                        expressionValueRemoveRight(i)
                        expressionValueRemoveLeft(i)
                        i--
                    }
                }
                i++
            }
            number = expressionValueString.get(0)
            outText.setText(
                expressionValueString.get(0).toBigDecimal().stripTrailingZeros().toPlainString()
            )
            expressionValueClear()
            if (number == "0") isNotFirstZero = false
        } catch (e: Exception) {
            expressionValueClear()
            outText.setText(R.string.error_message)
            isError = true
        }
    }

    fun expressionValueAdd(str: String, b: Byte) {
        expressionValueString.add(str)
        expressionValuePriority.add(b)
    }

    fun expressionValueClear() {
        expressionValueString.clear()
        expressionValuePriority.clear()
    }

    fun expressionValueRemoveRight(i: Int) {
        expressionValuePriority.removeAt(i + 1)
        expressionValueString.removeAt(i + 1)
    }

    fun expressionValueRemove(i: Int) {
        expressionValuePriority.removeAt(i)
        expressionValueString.removeAt(i)
    }

    fun expressionValueRemoveLeft(i: Int) {
        expressionValuePriority.removeAt(i - 1)
        expressionValueString.removeAt(i - 1)
    }
}