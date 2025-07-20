package com.example.first_app.ChatBot.Aichatbot

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.first_app.R
import com.example.first_app.presentation.nvGraph.RouteNS
import com.example.first_app.ui.theme.Purple200
import com.example.first_app.ui.theme.Teal200
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.ImagePart
import com.google.firebase.ai.type.TextPart
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseAiLoginChatScreen(
    navController: NavController,
    chatViewModel: FirebaseAiLogicChatViewModel = viewModel<FirebaseAiLogicChatViewModel>()
) {
    val messages: List<Content> by chatViewModel.message.collectAsStateWithLifecycle()
    val isLoading: Boolean by chatViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage: String? by chatViewModel.errorMessage.collectAsStateWithLifecycle()
    val attachments: List<Attachment> by chatViewModel.attachments.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    OnBackClickStateSaver(navController)

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    ) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            // Back button
//                            IconButton(onClick = { navController.popBackStack() }) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_back_arrow),
//                                    contentDescription = "Back",
//                                    tint = Color.White
//                                )
//                            }
//
//                            Text(
//                                text = "GrabBot Ai",
//                                color = Color.White,
//                                fontSize = 22.sp,
//                                fontWeight = FontWeight.Bold,
//                                modifier = Modifier.weight(1f),
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.width(48.dp))
//                        }
//                    }
//
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            )
//        }
        topBar = {
            // Since the standard TopAppBar doesn't accept a Brush, we create our own.
            // We use a Box as the main container for our custom app bar.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // This is where we apply the gradient. It will cover the entire
                    // Box, which will be the size of the app bar plus the status bar.
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Purple200, Teal200)
                        )
                    )
            ) {
                // We use a Row for the content, just like in your previous example.
                // The layout is an icon, a weighted title, and a spacer.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        // This is the most important modifier here.
                        // `windowInsetsPadding(WindowInsets.statusBars)` automatically adds
                        // top padding to this Row, pushing its content down by exactly the
                        // height of the system status bar. This prevents your UI elements
                        // from being drawn behind the system clock or notch.
                        .windowInsetsPadding(WindowInsets.statusBars)
                        // We still give it a standard height for the tappable area.
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "GrabBot Ai",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            //Main Content
            ChatList(
                messages, listState, modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
            )
            Box(
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                ) {
                    if (isLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    errorMessage?.let {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    AttachmentList(
                        attachments
                    )
                    val context = LocalContext.current
                    val contentResolver = context.contentResolver
                    val scope = rememberCoroutineScope()
                    BottomInput(
                        initialMessage = "",
                        onSendMessage = {
                            chatViewModel.sendMessage(it)
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        onFileAttached = { uri ->
                            val mimeType = contentResolver.getType(uri).orEmpty()
                            var fileName: String? = null
                            contentResolver.query(
                                uri, null, null, null, null
                            )?.use { cursor ->
                                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                                cursor.moveToLast()

                                val humanReadableSize =
                                    Formatter.formatShortFileSize(
                                        context,
                                        cursor.getLong(sizeIndex)
                                    )
                                fileName = "${cursor.getString(nameIndex)} ($humanReadableSize)"
                            }
                            contentResolver.openInputStream(uri)?.use { stream ->
                                val bytes = stream.readBytes()
                                chatViewModel.addAttachment(
                                    bytes, mimeType, fileName
                                )
                            }
                        },
                        isLoading = isLoading

                    )
                }
            }
        }
    }
}

@Composable
fun BottomInput(
    initialMessage: String = "",
    onSendMessage: (String) -> Unit,
    onFileAttached: (Uri) -> Unit,
    isLoading: Boolean = false
) {
    var userMessage by rememberSaveable {
        mutableStateOf(initialMessage)
    }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = userMessage,
            label = {
                Text("Enter Message")
            },
            onValueChange = {
                userMessage = it
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 4.dp)
                .fillMaxWidth()
                .weight(1f)

        )
        //Add attachment
        AttachmentMenu(
            modifier = Modifier.align(Alignment.CenterVertically),
            onFileAttached = onFileAttached
        )
        IconButton(
            onClick = {
                if (userMessage.isNotBlank()) {
                    onSendMessage(userMessage)
                    userMessage = ""
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
                .background(
                    color = if (isLoading) {
                        IconButtonDefaults.iconButtonColors().disabledContentColor
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )

        ) {
            Icon(
                Icons.AutoMirrored.Default.Send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun AttachmentList(
    attachments: List<Attachment>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(attachments) { attachment ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp, vertical = 4.dp
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.attachment),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(
                            Alignment.CenterVertically
                        )
                )
                attachment.image?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = attachment.fileName,
                        modifier = Modifier.align(
                            Alignment.CenterVertically
                        )
                    )
                }
                Text(
                    text = attachment.fileName ?: " ",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
fun AttachmentMenu(
    modifier: Modifier = Modifier,
    onFileAttached: (Uri) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            onFileAttached(it)
        }
    }
    Box(modifier = modifier.padding(end = 4.dp)) {
        IconButton(onClick = {
            expanded = !expanded
        }) {
            Icon(
                painter = painterResource(R.drawable.attachfile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Text(
                "Attach",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            DropdownMenuItem(
                text = {
                    Text("Image")
                },
                onClick = {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    expanded = !expanded
                }
            )
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ChatItem(
    chatMessage: Content
) {
    val isModelMessage = chatMessage.role == "model"

    val horizontalAlignment = if (isModelMessage) Alignment.Start else Alignment.End

    val bubbleShape = if (isModelMessage) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }
    val bgColor = if (isModelMessage) {
        MaterialTheme.colorScheme.surfaceContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val textColor = if (isModelMessage) {
        Color.Unspecified
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            )
            .fillMaxWidth()
    ) {
        Row {
            BoxWithConstraints {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = bgColor
                    ),
                    shape = bubbleShape,
                    modifier = Modifier.requiredWidthIn(0.dp, maxWidth * 0.9f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        chatMessage.parts.forEach { part ->
                            when (part) {
                                is TextPart -> {
                                    Text(
                                        text = part.text,
                                        modifier = Modifier.fillMaxWidth(),
                                        color = textColor
                                    )
                                }

                                is ImagePart -> {
                                    Image(
                                        bitmap = part.image.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatList(
    messages: List<Content>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {

        if(messages.isEmpty()){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier=Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.85f)
        ) {

            // Wrapping Icon and Lottie animation in a Box to stack them
            Box(
                contentAlignment = Alignment.Center,  // Align both items at the center
                modifier = Modifier.size(200.dp)
            ) {


                // Lottie animation on top
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(
                        resId = R.raw.chat_homescreen
                    )
                )
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(200.dp),
                )
                Text(text = "Start the conversation",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(top = 150.dp ,start =10.dp )
                )
            }

        }
    }else{
            LazyColumn(
                reverseLayout = true,
                state = listState,
                modifier = modifier
            ) {
                items(messages.reversed(), key = { Random.nextInt() }) { chatMessage ->
                    ChatItem(chatMessage)
                }

            }
    }
}

@Composable
fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(enabled = true) {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()  // Go back to the previous screen
        } else {
            navController.navigate(RouteNS.HomeScreen.route)  { // If no previous screen, go to home
                popUpTo(RouteNS.HomeScreen.route) { inclusive = true } // Ensure clean navigation
            }
        }
    }
}