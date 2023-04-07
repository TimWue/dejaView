package com.timwue.dejaview

import android.content.res.Configuration
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timwue.dejaview.ui.theme.DejaviewTheme

val message1 =  Message("Tim", "Hallo")
val message2 =  Message("Leo", "Was geht ab?")
val message3 =  Message("Leo", "HALLLOOOHOOOO")

class MainActivityExamples : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DejaviewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                      Conversation(messages = listOf(message1,message2,message3))
                }
            }
        }
    }
}
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)@Composable
fun PreviewConversation(){
    DejaviewTheme {
        Surface(
            color = MaterialTheme.colors.background

        ) {
            Conversation(messages = listOf(message1,message2,message3))

        }
    }
}

@Composable
fun Conversation(messages : List<Message>){
    LazyColumn{
        items(messages){ message -> MessageCard(message = message)}
    }
}

data class Message(val author: String, val message: String)

@Composable
fun MessageCard(message: Message){
    Row {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(text = message.author, color = MaterialTheme.colors.secondary)
            Spacer(modifier = Modifier.height(4.dp))

            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(text = message.message, Modifier.padding(4.dp), style = MaterialTheme.typography.body2)

            }
        }
    }
}