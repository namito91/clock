package com.sysarcomp.clock.alarm.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimePickerList(
    label: String,
    range: IntRange,
    selectedValue: Int,
    onValueChange: (Int) -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = label)

        LazyColumn(
            modifier = Modifier
                .height(140.dp) // Altura para mostrar algunos elementos visibles a la vez
                .width(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(range.toList()) { value ->

                Text(
                    text = value.toString().padStart(
                        2,
                        '0'
                    ), //  Asegura que los números tengan siempre 2 dígitos, agregando ceros a la izquierda si es necesario.
                    fontSize = 18.sp,
                    fontWeight = if (value == selectedValue) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onValueChange(value) },
                    color = if (value == selectedValue) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )

            }
        }
    }
}
