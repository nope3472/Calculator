import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.tutorials.calculator.ui.theme.CalculatorTheme

@Composable
fun Calculator() {
    var display by remember { mutableStateOf("0") }
    var currentInput by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("") }
    var previousValue by remember { mutableStateOf(0.0) }

    val handleButtonClick: (String) -> Unit = { buttonText ->
        when (buttonText) {
            in "0123456789." -> handleDigitInput(buttonText, currentInput, { currentInput = it }, { display = it })
            in "+-*/" -> handleOperator(buttonText, currentInput.toDouble(), { currentInput = it }, { operator = it }, { previousValue = it },
                { display=it } )
            "=" -> calculateResult(currentInput.toDouble(), previousValue, operator, { display = it }, { currentInput = it }, { previousValue = it })
            "C" -> clear({ display = it }, { currentInput = it }, { operator = it }, { previousValue = it })
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = display,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            color = Color.White,
            textAlign = TextAlign.End
        )

        CalculatorButtonRow(
            buttons = listOf("7", "8", "9", "/"),
            onClick = handleButtonClick,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CalculatorButtonRow(
            buttons = listOf("4", "5", "6", "*"),
            onClick = handleButtonClick,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CalculatorButtonRow(
            buttons = listOf("1", "2", "3", "-"),
            onClick = handleButtonClick,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CalculatorButtonRow(
            buttons = listOf("0", ".", "=", "+"),
            onClick = handleButtonClick,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        CalculatorButtonRow(
            buttons = listOf("C"),
            onClick = handleButtonClick,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
fun CalculatorButtonRow(buttons: List<String>, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        buttons.forEach { text ->
            CalculatorButton(
                text = text,
                onClick = { onClick(text) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (text == "=") Color.Green else Color.DarkGray,
            contentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun handleDigitInput(digit: String, currentInput: String, updateCurrentInput: (String) -> Unit, updateDisplay: (String) -> Unit) {
    fun clear(
        updateDisplay: (String) -> Unit,
        updateCurrentInput: (String) -> Unit,
        updateOperator: (String) -> Unit,
        updatePreviousValue: (Double) -> Unit
    ) {
        updateDisplay("0")
        updateCurrentInput("")
        updateOperator("")
        updatePreviousValue(0.0)
    }

    val newInput = if (currentInput == "0" && digit != ".") {
        digit
    } else {
        currentInput + digit
    }
    updateCurrentInput(newInput)
    updateDisplay(newInput)
}

fun handleOperator(op: String, currentValue: Double, updateCurrentInput: (String) -> Unit, updateOperator: (String) -> Unit, updatePreviousValue: (Double) -> Unit,updateDisplay: (String) -> Unit) {
    updateOperator(op)
    updatePreviousValue(currentValue)
    updateCurrentInput( "")
    updateDisplay("$op")
}

fun calculateResult(currentValue: Double, previousValue: Double, operator: String, updateDisplay: (String) -> Unit, updateCurrentInput: (String) -> Unit, updatePreviousValue: (Double) -> Unit) {
    val result = when (operator) {
        "+" -> previousValue + currentValue
        "-" -> previousValue - currentValue
        "*" -> previousValue * currentValue
        "/" -> if (currentValue == 0.0) {
            Double.NaN
        } else {
            previousValue / currentValue
        }
        else -> return
    }

    val resultText = if (result.isNaN()) "Error" else result.toString()
    updateDisplay(resultText)
    updateCurrentInput(resultText)
    updatePreviousValue(0.0)

}

fun clear(updateDisplay: (String) -> Unit, updateCurrentInput: (String) -> Unit, updateOperator: (String) -> Unit, updatePreviousValue: (Double) -> Unit) {
    updateDisplay("0")
    updateCurrentInput("")
    updateOperator("")
    updatePreviousValue(0.0)
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        Calculator()
    }
}
