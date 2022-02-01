package app.trian.tudu.ui.pages.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.tudu.ui.component.ButtonPrimary
import app.trian.tudu.ui.component.task.FormInput
import app.trian.tudu.ui.theme.TuduTheme
import compose.icons.Octicons
import compose.icons.octicons.ArrowLeft24

/**
 * Page Password
 * author Trian Damai
 * created_at 01/02/22 - 09.40
 * site https://trian.app
 */

@Composable
fun PageChangePassword(
    modifier: Modifier = Modifier,
    router: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.background,
                navigationIcon = {
                    IconToggleButton(checked = false, onCheckedChange = {}) {
                        Icon(
                            imageVector = Octicons.ArrowLeft24,
                            contentDescription = ""
                        )
                    }
                },
                title = {
                    Text(text = "Change Password")
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(
                    horizontal = 30.dp
                )
        ) {
            FormInput(
                label = {
                    Text(text = "Current Password")
                },
                placeholder = "Input current password",
                shoPasswordObsecure = true
            )
            Spacer(modifier = modifier.height(16.dp))
            FormInput(
                label = {
                    Text(text = "New Password")
                },
                placeholder = "Input new password",
                shoPasswordObsecure = true
            )
            Spacer(modifier = modifier.height(16.dp))
            FormInput(
                label = {
                    Text(text = "Confirm Password")
                },
                placeholder = "Confirm new password",
                shoPasswordObsecure = true
            )
            Spacer(modifier = modifier.height(30.dp))
            ButtonPrimary(text = "Save Changes"){

            }
        }
    }
}

@Preview
@Composable
fun PreviewPageChangePassword(){
    TuduTheme {
        PageChangePassword(router = rememberNavController())
    }
}