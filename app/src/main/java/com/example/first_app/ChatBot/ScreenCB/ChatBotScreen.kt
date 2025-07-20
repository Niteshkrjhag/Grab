//package com.example.first_app.ChatBot.ScreenCB
//
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.selection.SelectionContainer
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.Send
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.airbnb.lottie.compose.LottieAnimation
//import com.airbnb.lottie.compose.LottieCompositionSpec
//import com.airbnb.lottie.compose.LottieConstants
//import com.airbnb.lottie.compose.rememberLottieComposition
//import com.example.first_app.ChatBot.ChatViewModel.ChatViewModel
//import com.example.first_app.ChatBot.ChatViewModel.MessageModel
//import com.example.first_app.R
//import com.example.first_app.presentation.nvGraph.RouteNS
//import com.example.first_app.ui.theme.Blue
//import com.example.first_app.ui.theme.Purple200
//import com.example.first_app.ui.theme.Teal200
//
//
//// Sample mock ViewModel for preview
//class MockChatViewModel : ChatViewModel() {
//    init {
//        messageList.addAll(
//            listOf(
////                MessageModel("Hello", "user"),
////                MessageModel("How can i help you today?", "model")
//            )
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewChatBotScreen() {
//    val navController = rememberNavController()
//    val chatViewModel = MockChatViewModel()
//
//    ChatBotScreen(navController, chatViewModel)
//}
//
//@Composable
//fun ChatBotScreen(navController: NavController,chatViewModel: ChatViewModel){
//    OnBackClickStateSaver(navController)
//    Column(
//    ){
//        Header(navController)
//        MessageList(modifier = Modifier.weight(
//            1f
//        ),
//            messageList = chatViewModel.messageList)
//        messageInput(
//            onMessageSend = {
//                chatViewModel.sendMessage(it)
//            }
//        )
//    }
//}
//
//@Composable
//fun Header(navController: NavController){
//    Box(
//        modifier = Modifier
//        .fillMaxWidth()
//        .background(brush = Brush.horizontalGradient(
//            listOf(Purple200, Teal200) // Gradient background
//        ))
//    ){
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(8.dp)
//        ) {
//            // Back button
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_back_arrow),
//                    contentDescription = "Back",
//                    tint = Color.White
//                )
//            }
//
//            // Title
//            Text(
//                text = "GrabBot Ai",
//                color = Color.White,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(start = 8.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun MessageList(modifier: Modifier=Modifier,messageList:List<MessageModel>){
//
//    if(messageList.isEmpty()){
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier=Modifier
//                .fillMaxSize(0.9f)
//        ) {
//
//            // Wrapping Icon and Lottie animation in a Box to stack them
//            Box(
//                contentAlignment = Alignment.Center,  // Align both items at the center
//                modifier = Modifier.size(200.dp)
//            ) {
//
//
//                // Lottie animation on top
//                val composition by rememberLottieComposition(
//                    LottieCompositionSpec.RawRes(
//                        resId = R.raw.chat_homescreen
//                    )
//                )
//                LottieAnimation(
//                    composition = composition,
//                    iterations = LottieConstants.IterateForever,
//                    modifier = Modifier.size(200.dp),
//                )
//                Text(text = "Start the conversation",
//                    fontSize = 12.sp,
//                    color = Color.Gray,
//                    modifier = Modifier
//                        .padding(top = 150.dp ,start =10.dp )
//                )
//            }
//
//        }
//    }else{
//        LazyColumn(
//            modifier=modifier,
//            reverseLayout = true
//        ) {
//            items(messageList.reversed()){
//                MessageRow(messageModel = it)
//            }
//        }
//    }
//}
//
//@Composable
//fun MessageRow(messageModel:MessageModel){
//    val isModel = messageModel.Role == "model"
//
//    Row(
//       verticalAlignment = Alignment.CenterVertically
//    ){
//        Box(
//            modifier = Modifier.fillMaxWidth()
//        ){
//            Box(
//                modifier = Modifier
//                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
//                    .padding(
//                        start = if (isModel) 8.dp else 70.dp,
//                        end = if (isModel) 70.dp else 8.dp,
//                        top = 8.dp,
//                        bottom = 8.dp
//                    )
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(if (isModel) Blue else Teal200) // Soft colors with opacity
//                    .padding(16.dp)
//
//            ){
//                SelectionContainer {
//                    Text(
//                        text = messageModel.message,
//                        fontWeight = FontWeight.W500,
//                        color = Color.White
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun messageInput(onMessageSend: (String) -> Unit){
//    var message by remember{ mutableStateOf("") }
//    Row(
//        modifier = Modifier
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ){
//        OutlinedTextField(
//            value = message ,
//            onValueChange = {
//                message = it
//            },
//            placeholder = {
//                Text(
//                    text = "Type Your Message Here",
//                    color = Color.Gray,
//                    fontSize = 12.sp
//                )
//            },
//            modifier = Modifier
//                .weight(1f)
//                .background(Color.White)
//            ,
//
//        )
//        IconButton(onClick = {
//            if(message.isNotEmpty()){
//                onMessageSend(message)
//                message = ""
//            }
//        })
//        {
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.Send,
//                contentDescription = "Send",
//                tint = Blue
//            )
//        }
//
//    }
//}
//@Composable
//fun OnBackClickStateSaver(navController: NavController) {
//    BackHandler(enabled = true) {
//        if (navController.previousBackStackEntry != null) {
//            navController.popBackStack()  // Go back to the previous screen
//        } else {
//            navController.navigate(RouteNS.HomeScreen.route)  { // If no previous screen, go to home
//                popUpTo(RouteNS.HomeScreen.route) { inclusive = true } // Ensure clean navigation
//            }
//        }
//    }
//}
