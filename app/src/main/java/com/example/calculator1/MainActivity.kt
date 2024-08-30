package com.example.calculator1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvInput: TextView
    private lateinit var tvResult: TextView

    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
        tvResult = findViewById(R.id.tvResult)

        tvInput.text = "" // Mengosongkan input saat aplikasi dimulai

        setNumberOnClickListener()
        setOperatorOnClickListener()
        setBackspaceOnClickListener()
    }

    private fun setNumberOnClickListener() {
        val buttons = listOf(
            findViewById<Button>(R.id.btn0), findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2), findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4), findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6), findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8), findViewById<Button>(R.id.btn9)
        )

        for (button in buttons) {
            button.setOnClickListener {
                tvInput.append((it as Button).text)
                lastNumeric = true
            }
        }
    }

    private fun setOperatorOnClickListener() {
        val operators = listOf(
            findViewById<Button>(R.id.btnAdd), findViewById<Button>(R.id.btnSubtract),
            findViewById<Button>(R.id.btnMultiply), findViewById<Button>(R.id.btnDivide)
        )

        for (button in operators) {
            button.setOnClickListener {
                if (lastNumeric && !isOperatorAdded(tvInput.text.toString())) {
                    tvInput.append((it as Button).text)
                    lastNumeric = false
                    lastDot = false
                }
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            if (lastNumeric) {
                val result = calculateResult()
                if (result.isNotEmpty()) {
                    tvResult.text = result
                    Toast.makeText(this, "Hasil: $result", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            tvInput.text = ""
            tvResult.text = ""
            lastNumeric = false
            lastDot = false
        }
    }

    private fun setBackspaceOnClickListener() {
        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            val length = tvInput.text.length
            if (length > 0) {
                tvInput.text = tvInput.text.subSequence(0, length - 1)
                lastNumeric = tvInput.text.isNotEmpty() && tvInput.text.last().isDigit()
            }
        }
    }

    private fun calculateResult(): String {
        val input = tvInput.text.toString()

        try {
            var result = ""
            if (input.contains("-")) {
                val split = input.split("-")
                val one = split[0].toDouble()
                val two = split[1].toDouble()
                result = (one - two).toString()
            } else if (input.contains("+")) {
                val split = input.split("+")
                val one = split[0].toDouble()
                val two = split[1].toDouble()
                result = (one + two).toString()
            } else if (input.contains("*")) {
                val split = input.split("*")
                val one = split[0].toDouble()
                val two = split[1].toDouble()
                result = (one * two).toString()
            } else if (input.contains("/")) {
                val split = input.split("/")
                val one = split[0].toDouble()
                val two = split[1].toDouble()
                result = (one / two).toString()
            }
            return if (result.endsWith(".0")) result.substring(0, result.length - 2) else result
        } catch (e: ArithmeticException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("/") || value.contains("*") ||
                    value.contains("+") || value.contains("-")
        }
    }
}
