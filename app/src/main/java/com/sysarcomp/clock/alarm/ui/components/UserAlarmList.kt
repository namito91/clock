package com.sysarcomp.clock.alarm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sysarcomp.clock.alarm.model.UserAlarm
import com.sysarcomp.clock.alarm.logic.cancelAlarm

@Composable
fun UserAlarmList(userAlarms: MutableList<UserAlarm>) {

    val context = LocalContext.current

    LazyColumn(

        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        items(userAlarms) { alarm ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "${alarm.hour.toString().padStart(2, '0')} : ${
                        alarm.minute.toString().padStart(2, '0')
                    }",
                    fontSize = 20.sp
                )

                Button(
                    onClick = {
                        userAlarms.remove(alarm)
                        cancelAlarm(context, alarm.id,userAlarms)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Del", color = Color.White)
                }
            }

        }
    }
}


