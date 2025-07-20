package com.example.first_app.presentation.onboarding.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.first_app.R
import com.example.first_app.presentation.onboarding.Page
import com.example.first_app.presentation.Dimens
import com.example.first_app.presentation.Dimens.mediumPadding1
import com.example.first_app.presentation.Dimens.mediumPadding2
import com.example.first_app.presentation.onboarding.pages
import com.example.first_app.ui.theme.First_AppTheme

@Composable
fun OnBoardingPage(modifier: Modifier=Modifier,page: Page){
    Column(modifier=modifier) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(fraction = 0.6f),
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(mediumPadding1))
        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = mediumPadding2),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.display_small) // using colorResource to support day and night mode

        )
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = mediumPadding2),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.text_medium) // using (values->colors) colorResource to support day and night mode
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview( uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingPagePreview(){
    First_AppTheme{
        OnBoardingPage(page = pages[0])
    }

}