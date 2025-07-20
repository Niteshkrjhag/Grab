package com.example.first_app.Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.first_app.Login_auth.AuthState
import com.example.first_app.Login_auth.LoginViewModel
import com.example.first_app.Login_auth.ProfileViewModel
import com.example.first_app.R
import com.example.first_app.googleSignIn.GoogleSignInViewModel
import com.example.first_app.navigation.Routes
import com.example.first_app.ui.theme.LightPrimaryColor
import com.example.first_app.ui.theme.Poppins
import com.example.first_app.ui.theme.PrimaryColor
import com.example.first_app.ui.theme.SecondaryColor
import com.example.first_app.ui.theme.Shapes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Preview(showSystemUi = true)
@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel,
    googleSignInViewModel: GoogleSignInViewModel
) {
    val userEmail = profileViewModel.userEmail
    val scrollState = rememberScrollState()
    Log.d("SignOut", "inside setting screen")

    Column(modifier = Modifier.fillMaxSize()) {
        // Fixed content at the top (non-scrollable)
        HeaderText()
        ProfileCardUI(userEmail = userEmail, googleSignInViewModel)

        // Scrollable content below
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            GeneralOptionsUI()
            SupportOptionsUI(navController, authViewModel)

            // Add some padding at the bottom for better scrolling experience
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeaderText() {
    Text(
        text = "Settings",
        fontFamily = Poppins,
        color = SecondaryColor,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 10.dp),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp
    )
}

//@Composable
//fun ProfileCardUI(userEmail: State<String?>,googleSignInViewModel: GoogleSignInViewModel) {
//
//    val user by googleSignInViewModel.user.observeAsState()
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(150.dp)
//            .padding(10.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color.White,
//            contentColor = Color.Black,
//        ),
//
//        elevation = CardDefaults.cardElevation(),
//        shape = Shapes.large
//    ) {
//        Row(
//            modifier = Modifier.padding(20.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column() {
//                Text(
//                    text = user?.name ?: "Check Your Profile",
//                    fontFamily = Poppins,
//                    color = SecondaryColor,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//
//                Text(
//                    text =  userEmail.value ?: "No email available",
//                    fontFamily = Poppins,
//                    color = Color.Gray,
//                    fontSize = 10.sp,
//                    fontWeight = FontWeight.SemiBold,
//                )
//
//                Button(
//                    modifier = Modifier.padding(top = 10.dp),
//                    onClick = {},
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = PrimaryColor
//                    ),
//                    contentPadding = PaddingValues(horizontal = 30.dp),
//                    elevation = ButtonDefaults.buttonElevation(
//                        defaultElevation = 0.dp,
//                        pressedElevation = 2.dp
//                    ),
//                    shape = Shapes.medium
//                ) {
//                    Text(
//                        text = "View",
//                        fontFamily = Poppins,
//                        color = SecondaryColor,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//            Image(
//                painter = painterResource(id = R.drawable.ic_profile_card_image),
//                contentDescription = "",
//                modifier = Modifier.height(120.dp)
//            )
//        }
//    }
//}

@Composable
fun ProfileCardUI(userEmail: State<String?>, googleSignInViewModel: GoogleSignInViewModel) {
    val user by googleSignInViewModel.user.observeAsState()
    val context = LocalContext.current
    var isViewDetailsClicked by remember { mutableStateOf(false) }

    // Format the current date
    val currentDate = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = Shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with date and greeting
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Welcome back,",
                        fontFamily = Poppins,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = user?.name?.split(" ")?.firstOrNull() ?: "User",
                        fontFamily = Poppins,
                        color = SecondaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = currentDate,
                    fontFamily = Poppins,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main content with profile image and details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile image with border
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(LightPrimaryColor)
                        .border(2.dp, PrimaryColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (user?.photoUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(user?.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = user?.name?.firstOrNull()?.toString()?.uppercase() ?: "U",
                            color = PrimaryColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

// User info
// User info
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = user?.name ?: "Check Your Profile",
                        fontFamily = Poppins,
                        color = SecondaryColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp  // Tighten line height
                    )
                    Spacer(modifier=Modifier.height(3.dp))
                    Text(
                        text = userEmail.value ?: "No email available",
                        fontFamily = Poppins,
                        color = Color.Gray,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 8.sp,  // Tighten line height
                        modifier = Modifier.offset(y = (-2).dp)  // Negative offset to pull up
                    )
                    Spacer(modifier=Modifier.height(3.dp))
                    Text(
                        text = "Last login: ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())}",
                        fontFamily = Poppins,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,  // Tighten line height
                        modifier = Modifier.offset(y = (-3).dp)  // Negative offset to pull up
                    )
                }

                // Profile decoration image
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_card_image),
                    contentDescription = "",
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Activity stats and view button
            if (isViewDetailsClicked) {
                ProfileStatsSection()

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { isViewDetailsClicked = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 2.dp
                    ),
                    shape = Shapes.medium
                ) {
                    Text(
                        text = "Hide Details",
                        fontFamily = Poppins,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AccountStatusChip()

                    Button(
                        onClick = { isViewDetailsClicked = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        ),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 2.dp
                        ),
                        shape = Shapes.medium
                    ) {
                        Text(
                            text = "View Details",
                            fontFamily = Poppins,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AccountStatusChip() {
//    Surface(
//        shape = Shapes.medium,
//        color = Color(0xFF4CAF50).copy(alpha = 0.2f),
//        modifier = Modifier
//            .height(26.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//            verticalAlignment = Alignment.CenterVertically
//
//        ){
//            Box(
//                modifier = Modifier
//                    .size(8.dp)
//                    .clip(CircleShape)
//                    .background(Color(0xFF4CAF50))
//            )
//
//            Spacer(modifier = Modifier.width(4.dp))
//
//            Text(
//                text = "Active",
//                fontFamily = Poppins,
//                fontSize = 12.sp,
//                color = Color(0xFF4CAF50),
//                fontWeight = FontWeight.Medium
//            )
//        }
//    }
//}

@Composable
fun AccountStatusChip() {
    Surface(
        shape = Shapes.medium,
        color = Color(0xFF4CAF50).copy(alpha = 0.2f),
        modifier = Modifier
            .height(26.dp)
            .wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Center the dot properly by using Box with contentAlignment
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Fix text alignment by using textAlign and removing vertical padding
            Text(
                text = "Active",
                fontFamily = Poppins,
                fontSize = 10.sp,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileStatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ProfileStatItem(
            value = "12",
            label = "Read",
            icon = painterResource(id = R.drawable.ic_about) // Replace with appropriate icon
        )

        ProfileStatItem(
            value = "3",
            label = "Shared",
            icon = painterResource(id = R.drawable.ic_feedback) // Replace with appropriate icon
        )

        ProfileStatItem(
            value = "5",
            label = "Bookmarked",
            icon = painterResource(id = R.drawable.ic_privacy_policy) // Replace with appropriate icon
        )
    }
}

@Composable
fun ProfileStatItem(value: String, label: String, icon: Painter) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(LightPrimaryColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryColor
        )

        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

//@Composable
//fun GeneralOptionsUI() {
//    Column(
//        modifier = Modifier
//            .padding(horizontal = 14.dp)
//            .padding(top = 10.dp)
//    ) {
//        Text(
//            text = "General",
//            fontFamily = Poppins,
//            color = SecondaryColor,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .padding(vertical = 8.dp)
//        )
//        GeneralSettingItem(
//            icon = R.drawable.ic_rounded_notification,
//            mainText = "Notifications",
//            subText = "Customize notifications",
//            onClick = {}
//        )
//        GeneralSettingItem(
//            icon = R.drawable.ic_more,
//            mainText = "More customization",
//            subText = "Customize it more to fit your usage",
//            onClick = {}
//        )
////        GeneralSettingItem()
//    }
//}


@Composable
fun GeneralOptionsUI() {
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "General",
            fontFamily = Poppins,
            color = SecondaryColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        // Enhanced Notification Setting with expandable content
        ExpandableSettingItem(
            icon = R.drawable.ic_rounded_notification,
            mainText = "Notifications",
            subText = "Customize notifications"
        ) {
            NotificationOptions()
        }

        // Enhanced More Customization with expandable content
        ExpandableSettingItem(
            icon = R.drawable.ic_more,
            mainText = "More customization",
            subText = "Customize it more to fit your usage"
        ) {
            CustomizationOptions()
        }
    }
}

@Composable
fun ExpandableSettingItem(
    icon: Int,
    mainText: String,
    subText: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Material3 approach for clickable with proper ripple
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 0.dp,
            onClick = { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween, // This ensures space is distributed properly
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // Add some padding to prevent content from touching screen edges
                ) {
                    // Left section with icon and text (wrapped in a Row)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(
                            1f,
                            fill = false
                        ) // Take available space but don't force expansion
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            tint = PrimaryColor,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mainText,
                                fontFamily = Poppins,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis // Add ellipsis for long text
                            )
                            Text(
                                text = subText,
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis // Add ellipsis for long text
                            )
                        }
                    }

                    // Add a small spacer to ensure some minimum distance
                    Spacer(modifier = Modifier.width(8.dp))

                    // Right arrow icon
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand",
                        tint = PrimaryColor,
                        modifier = Modifier
                            .rotate(rotationState)
                            .size(24.dp)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                content()
            }
        }
    }
}

@Composable
fun NotificationOptions() {
    // Remove verticalScroll modifier since parent handles scrolling
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Content remains the same
        NotificationToggleItem(
            title = "Push Notifications",
            description = "Receive alerts on your device",
            initialValue = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        NotificationToggleItem(
            title = "Email Notifications",
            description = "Receive alerts via email",
            initialValue = false
        )

        Spacer(modifier = Modifier.height(8.dp))

        NotificationToggleItem(
            title = "Sound",
            description = "Play sound with notifications",
            initialValue = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        NotificationToggleItem(
            title = "Vibration",
            description = "Vibrate with notifications",
            initialValue = true
        )
    }
}

@Composable
fun CustomizationOptions() {
    // Remove verticalScroll modifier since parent handles scrolling
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Content remains the same
        ThemeSelector()

        Spacer(modifier = Modifier.height(16.dp))

        TextSizeSelector()

        Spacer(modifier = Modifier.height(16.dp))

        AccentColorSelector()
    }
}
@Composable
fun NotificationToggleItem(
    title: String,
    description: String,
    initialValue: Boolean
) {
    var isEnabled by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontFamily = Poppins,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = PrimaryColor,
                checkedTrackColor = PrimaryColor.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun ThemeSelector() {
    var selectedTheme by remember { mutableStateOf(0) }
    val themes = listOf("Automatic", "Light", "Dark")

    Column {
        Text(
            text = "Theme",
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            themes.forEachIndexed { index, theme ->
                FilterChip(
                    selected = selectedTheme == index,
                    onClick = { selectedTheme = index },
                    label = {
                        Text(
                            text = theme,
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryColor.copy(alpha = 0.2f),
                        selectedLabelColor = PrimaryColor
                    )
                )
            }
        }
    }
}

@Composable
fun TextSizeSelector() {
    var selectedSize by remember { mutableStateOf(1) }
    val sizes = listOf("Small", "Medium", "Large")

    Column {
        Text(
            text = "Text Size",
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sizes.forEachIndexed { index, size ->
                FilterChip(
                    selected = selectedSize == index,
                    onClick = { selectedSize = index },
                    label = {
                        Text(
                            text = size,
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryColor.copy(alpha = 0.2f),
                        selectedLabelColor = PrimaryColor
                    )
                )
            }
        }
    }
}

@Composable
fun AccentColorSelector() {
    var selectedColor by remember { mutableStateOf(0) }

    Column {
        Text(
            text = "Accent Color",
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val colors = listOf(
                PrimaryColor,
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.error,
                MaterialTheme.colorScheme.secondary
            )

            colors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedColor = index
                        }
                        .then(
                            if (selectedColor == index) {
                                Modifier.border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = RoundedCornerShape(18.dp)
                                )
                            } else Modifier
                        )
                )
            }
        }
    }
}
//
//@Composable
//fun GeneralSettingItem(
//    icon: Int,
//    mainText: String,
//    subText: String,
//    onClick: () -> Unit
//) {
//    // Using Material3 Surface with onClick for proper ripple effect
//    Surface(
//        onClick = onClick,
//        color = MaterialTheme.colorScheme.surface,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Icon(
//                painter = painterResource(id = icon),
//                contentDescription = null,
//                tint = PrimaryColor,
//                modifier = Modifier.size(24.dp)
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column {
//                Text(
//                    text = mainText,
//                    fontFamily = Poppins,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = subText,
//                    fontFamily = Poppins,
//                    fontSize = 14.sp,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}
@Composable
fun GeneralSettingItem(icon: Int, mainText: String, subText: String, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(shape = Shapes.medium)
                        .background(LightPrimaryColor)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))
                Column(
                    modifier = Modifier.offset(y = (2).dp)
                ) {
                    Text(
                        text = mainText,
                        fontFamily = Poppins,
                        color = SecondaryColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = subText,
                        fontFamily = Poppins,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "",
                modifier = Modifier.size(16.dp)
            )

        }
    }
}

//@Composable
//fun SupportOptionsUI(navController: NavController,authViewModel: LoginViewModel) {
//    val authState = authViewModel.authState.observeAsState()
//    LaunchedEffect(authState.value) {
//        Log.d("SignOut","${authState.value}\n ${authState}")
//        when(authState.value){
//
//            is AuthState.Unauthenticated -> navController.navigate(Routes.Login) {
//                Log.d("SignOut","Inside unauthenticated")
//            }
//            else -> Unit
//        }
//    }
//
// LazyColumn(
//        modifier = Modifier
//            .padding(horizontal = 14.dp)
//            .padding(top = 10.dp)
//    ) {
//
//item{
//    Text(
//        text = "Support",
//        fontFamily = Poppins,
//        color = SecondaryColor,
//        fontSize = 14.sp,
//        fontWeight = FontWeight.Bold,
//        modifier = Modifier
//            .padding(vertical = 8.dp)
//    )
//}
//item{
//    SupportItem(
//        icon = R.drawable.ic_whatsapp,
//        mainText = "Contact",
//        onClick = {}
//    )
//}
//item{
//    SupportItem(
//        icon = R.drawable.ic_feedback,
//        mainText = "Feedback",
//        onClick = {}
//    )
//}
//item{
//    SupportItem(
//        icon = R.drawable.ic_privacy_policy,
//        mainText = "Privacy Policy",
//        onClick = {}
//    )
//}
//
//     item{
//         SupportItem(
//             icon = R.drawable.ic_about,
//             mainText = "About",
//             onClick = {}
//         )
//     }
//
//     item{
//         SupportItem(
//             icon = R.drawable.outline_logout_24,
//             mainText = "Logout",
//             onClick = {
//                 Log.d("SignOut","Click registered")
//                 authViewModel.signout()
//             }
//         )
//     }
//
//
//    }
//}
//
//
//@Composable
//fun SupportItem(icon: Int, mainText: String, onClick: () -> Unit) {
//
//
//    Card(
//        onClick = { onClick() },
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        modifier = Modifier
//            .padding(bottom = 8.dp)
//            .fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(),
//    ) {
//        Row(
//            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(34.dp)
//                        .clip(shape = Shapes.medium)
//                        .background(LightPrimaryColor)
//                ) {
//                    Icon(
//                        painter = painterResource(id = icon),
//                        contentDescription = "",
//                        tint = Color.Unspecified,
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(14.dp))
//
//                Text(
//                    text = mainText,
//                    fontFamily = Poppins,
//                    color = SecondaryColor,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//            Icon(
//                painter = painterResource(id = R.drawable.ic_right_arrow),
//                contentDescription = "",
//                modifier = Modifier.size(16.dp)
//            )
//
//        }
//    }
//}


@Composable
fun SupportOptionsUI(navController: NavController, authViewModel: LoginViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState.value) {
        Log.d("SignOut", "${authState.value}\n ${authState}")
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.Login) {
                Log.d("SignOut", "Inside unauthenticated")
            }
            else -> Unit
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    "Confirm Logout",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    "Are you sure you want to logout from your account?",
                    fontFamily = Poppins
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        authViewModel.signout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Changed from LazyColumn to regular Column since scrolling is handled by parent
    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Support",
                fontFamily = Poppins,
                color = SecondaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Niteshkrjhag", // Current user's login
                fontFamily = Poppins,
                color = PrimaryColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Enhanced Contact item with expandable section
        ExpandableSupportItem(
            icon = R.drawable.ic_whatsapp,
            mainText = "Contact"
        ) {
            ContactOptions()
        }

        // Enhanced Feedback item with expandable form
        ExpandableSupportItem(
            icon = R.drawable.ic_feedback,
            mainText = "Feedback"
        ) {
            FeedbackForm()
        }

        // Enhanced Privacy Policy with expandable content
        ExpandableSupportItem(
            icon = R.drawable.ic_privacy_policy,
            mainText = "Privacy Policy"
        ) {
            PrivacyPolicySummary()
        }

        // Enhanced About with expandable content
        ExpandableSupportItem(
            icon = R.drawable.ic_about,
            mainText = "About"
        ) {
            AboutContent()
        }

        // Logout item with confirmation dialog
        SupportItem(
            icon = R.drawable.outline_logout_24,
            mainText = "Logout",
            onClick = {
                Log.d("SignOut", "Click registered")
                showLogoutDialog = true
            }
        )
    }
}

@Composable
fun ExpandableSupportItem(
    icon: Int,
    mainText: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Card(
            onClick = { expanded = !expanded },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(),
        ) {
            Row(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(shape = Shapes.medium)
                            .background(LightPrimaryColor)
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = mainText,
                        fontFamily = Poppins,
                        color = SecondaryColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // Rotating arrow icon
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(rotationState),
                    tint = SecondaryColor
                )
            }
        }

        // Expandable content section
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = LightPrimaryColor.copy(alpha = 0.3f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SupportItem(icon: Int, mainText: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(shape = Shapes.medium)
                        .background(LightPrimaryColor)
                ) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = mainText,
                    fontFamily = Poppins,
                    color = SecondaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ContactOptions() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ContactOptionItem(
            title = "WhatsApp",
            subtitle = "+XX XXXXX XXXXX",
            icon = R.drawable.ic_whatsapp
        ) {
            uriHandler.openUri("https://wa.me/+XXXXXXXXXXX")
        }

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.Gray.copy(alpha = 0.2f)
        )

        ContactOptionItem(
            title = "Email",
            subtitle = "support@example.com",
            icon = R.drawable.ic_rounded_notification // Replace with email icon
        ) {
            uriHandler.openUri("mailto:support@example.com")
        }

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.Gray.copy(alpha = 0.2f)
        )

        ContactOptionItem(
            title = "Phone",
            subtitle = "+XX XXXXX XXXXX",
            icon = R.drawable.ic_rounded_notification // Replace with phone icon
        ) {
            uriHandler.openUri("tel:+XXXXXXXXXXX")
        }
    }
}

@Composable
fun ContactOptionItem(
    title: String,
    subtitle: String,
    icon: Int,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = SecondaryColor
                )

                Text(
                    text = subtitle,
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun FeedbackForm() {
    var feedbackText by remember { mutableStateOf("") }
    val maxCharCount = 500
    val characterCount = feedbackText.length
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "We'd love to hear your feedback!",
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = SecondaryColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = feedbackText,
            onValueChange = {
                if (it.length <= maxCharCount) feedbackText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("Enter your feedback here...") },
            maxLines = 5,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { /* Submit feedback */ }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                cursorColor = PrimaryColor
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$characterCount/$maxCharCount",
                fontSize = 12.sp,
                color = if (characterCount > maxCharCount * 0.8) MaterialTheme.colorScheme.error else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Handle feedback submission
                Toast.makeText(context, "Feedback submitted. Thank you!", Toast.LENGTH_SHORT).show()
                feedbackText = ""
            },
            modifier = Modifier.align(Alignment.End),
            enabled = feedbackText.isNotBlank()
        ) {
            Text("Submit Feedback")
        }
    }
}

@Composable
fun PrivacyPolicySummary() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Privacy Policy Summary",
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = SecondaryColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "We collect minimal personal information required to provide you with the best service. This includes account information and usage data to improve your experience. We do not sell your personal data to third parties.",
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Last updated: July 19, 2025",
            fontFamily = Poppins,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextButton(
            onClick = {
                uriHandler.openUri("https://example.com/privacy-policy")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Read Full Privacy Policy",
                color = PrimaryColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AboutContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App logo/icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryColor),
            contentAlignment = Alignment.Center
        ) {
            // Replace with your app logo
            Icon(
                painter = painterResource(id = R.drawable.ic_about),
                contentDescription = "App Logo",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "My App",
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = SecondaryColor
        )

        Text(
            text = "Version 1.0.0",
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Developed with ❤️ by Nitesh",
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "© 2025 YourCompany. All rights reserved.",
            fontFamily = Poppins,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialIconButton(icon = R.drawable.ic_whatsapp) {
                // Open social media link
            }

            Spacer(modifier = Modifier.width(24.dp))

            SocialIconButton(icon = R.drawable.ic_feedback) {
                // Open social media link
            }

            Spacer(modifier = Modifier.width(24.dp))

            SocialIconButton(icon = R.drawable.ic_about) {
                // Open social media link
            }
        }
    }
}

@Composable
fun SocialIconButton(icon: Int, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(LightPrimaryColor)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = PrimaryColor,
            modifier = Modifier.size(20.dp)
        )
    }
}